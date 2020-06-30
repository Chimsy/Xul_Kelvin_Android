package zw.co.chimsy.xulkelvin.ui.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.helper.MySweetAlerts;
import zw.co.chimsy.xulkelvin.helper.SQLiteHandler;
import zw.co.chimsy.xulkelvin.helper.SourceReferences;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_PAYMENT_KYC;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_PAYMENT_PROCESS;

public class PaymentsActivity extends AppCompatActivity {
    private static final String TAG = PaymentsActivity.class.getSimpleName();

    private MySweetAlerts alerts;


    EditText txtRegNum, txtAmount;
    Spinner spinnerPMethod;
    Button pay;

    int amount;

    public OkHttpClient client;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);


        alerts = new MySweetAlerts(this);

        /* Progress Dialog */
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        /* Progress Dialog End*/

        /* OkHttp Initialise */
        client = new OkHttpClient();
        /* OkHttp Initialise End */

        /* SqLite database Handler */
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        final String reg_num = user.get("reg_num");
        final String actual_token_a = user.get("actual_token");

        /* SqLite database Handler End*/

        txtRegNum = findViewById(R.id.editText_regnum);
        txtRegNum.setText(reg_num);

        txtAmount = findViewById(R.id.editText_amount);

        /* Payment Methods */
        spinnerPMethod = findViewById(R.id.spinner_payment_method);
        final String paymentMethod = spinnerPMethod.getSelectedItem().toString();
        /* Payment Methods End*/

        /* Pay Button */
        pay = findViewById(R.id.btnPay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(txtAmount.getText().toString());
                Log.i(TAG, "" + amount);

                if (txtRegNum.length() == 0 || txtAmount.length() == 0) {
                    Toast.makeText(PaymentsActivity.this, "Registration Number & Amount " +
                            "Cannot Be Empty For You To Submit Your Request", Toast.LENGTH_LONG).show();
                } else if (amount < 10 || amount > 20000) {
                    Toast.makeText(PaymentsActivity.this, "You Can Only Pay Your Fees Between RTGS$10 & RTGS$20 000", Toast.LENGTH_LONG).show();
                } else if (txtRegNum.length() < 7) {
                    Toast.makeText(PaymentsActivity.this, "Invalid Registration Number", Toast.LENGTH_LONG).show();
                } else {
                    checkStudentDetails(actual_token_a, reg_num, paymentMethod, amount);
//                    processPayment(reg_num, paymentMethod, amount);
                }

            }
        });
        /* Pay Button End*/

    }

    private void checkStudentDetails(String token, final String regNum, final String paymentMethod, int amount) {

        pDialog.setMessage("Checking Student Details...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("reg_num", regNum);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_PAYMENT_KYC)
                .post(postBody)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                hideDialog();
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                //@TODO Logging
                Log.d(TAG, "onResponse " + response);

                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {

                        //@TODO Logging
                        Log.d(TAG, "Not Successful Response: " + response);

                        hideDialog();
                        Toast.makeText(PaymentsActivity.this, "ERROR: Failed To Reach Servers To Process Your Payment", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }


                    assert responseBody != null;
                    String JSON_STRING = responseBody.string();

                    //@TODO Logging
                    Log.d(TAG, "JSON String: " + JSON_STRING);

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);

                    //@TODO Logging
                    Log.d(TAG, "JSON Object: " + jsonObject);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);

                    //@TODO Logging
                    Log.d(TAG, "JSON Array: " + jsonArray);

                    JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);

                    //@TODO Logging
                    Log.i(TAG, "jsonObjectInnerLog [name]: " + jsonObjectInnerLog.getString("name"));
                    Log.i(TAG, "jsonObjectInnerLog [email]: " + jsonObjectInnerLog.getString("email"));
                    Log.i(TAG, "jsonObjectInnerLog [program]: " + jsonObjectInnerLog.getString("program"));
                    Log.i(TAG, "jsonObjectInnerLog [year]: " + jsonObjectInnerLog.getString("year"));
                    Log.i(TAG, "jsonObjectInnerLog [semester]: " + jsonObjectInnerLog.getString("semester"));

                    String name = jsonObjectInnerLog.getString("name");
                    String email = jsonObjectInnerLog.getString("email");
                    String program = jsonObjectInnerLog.getString("program");
                    String year = jsonObjectInnerLog.getString("year");
                    String semester = jsonObjectInnerLog.getString("semester");

                    final String Body = "Name: "+name + " \nEmail: " + email + " \nProgram: " + program + " \nLevel: " + year + "." + semester;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            sweetAlertConfirmKYC(regNum, paymentMethod, Body);
                        }
                    });

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private void processPayment(String reg_num, String payment_method, int amount) {
        txtRegNum.setText("");
        txtAmount.setText("");

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String actual_token = user.get("actual_token");

        SourceReferences sourceReferences = new SourceReferences();
        String source_ref = sourceReferences.generate_source_ref();

        makePayment(actual_token, source_ref, reg_num, payment_method, amount);

    }

    /* Consume End-Points */
    private void makePayment(String token, String source_ref, String reg_num, String payment_method, int amnt_paid) {
        txtRegNum.setText("");
        txtAmount.setText("");

        pDialog.setMessage("Processing Payment...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("source_ref", source_ref);
        postData.addProperty("reg_num", reg_num);
        postData.addProperty("payment_method", payment_method);
        postData.addProperty("amount", amnt_paid);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_PAYMENT_PROCESS)
                .post(postBody)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                hideDialog();
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                //@TODO Logging
                Log.d(TAG, "onResponse " + response);

                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {

                        //@TODO Logging
                        Log.d(TAG, "Not Successful Response: " + response);

                        hideDialog();
                        Toast.makeText(PaymentsActivity.this, "ERROR: Failed To Reach Servers To Process Your Payment", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }


                    assert responseBody != null;
                    String JSON_STRING = responseBody.string();

                    //@TODO Logging
                    Log.d(TAG, "JSON String: " + JSON_STRING);

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);

                    //@TODO Logging
                    Log.d(TAG, "JSON Object: " + jsonObject);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);

                    //@TODO Logging
                    Log.d(TAG, "JSON Array: " + jsonArray);

                    JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);

                    //@TODO Logging
                    Log.i(TAG, "jsonObjectInnerLog [status_code]: " + jsonObjectInnerLog.getString("status_code"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();

                            alerts.sweetAlertSuccess("Payment Completed. For Exam Registration, Contact Admin");
                        }
                    });

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    /* Consume End-Points End */

    /* Progress Dialog*/
    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
    /* Progress Dialog End*/

    /* Confirm KYC Details */
    public void sweetAlertConfirmKYC(final String regNum, final String paymentMethod, String contentBody) {
        txtRegNum.setText("");
        txtAmount.setText("");

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirm Student Details Below?")
                .setContentText(contentBody)
                .setConfirmText("Pay")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        // Process Payment After Confirming Account Details
                        processPayment(regNum, paymentMethod, amount);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("Cancel")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

}
