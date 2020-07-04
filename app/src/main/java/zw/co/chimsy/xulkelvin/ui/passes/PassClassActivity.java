package zw.co.chimsy.xulkelvin.ui.passes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
import zw.co.chimsy.xulkelvin.helper.SQLiteHandler;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_PAYMENT_PASS_CLASS;

public class PassClassActivity extends AppCompatActivity {
    private static final String TAG = PassClassActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    public OkHttpClient client;
    private SQLiteHandler db;

    TextView txt_class_p_reg_num, txt_class_p_student_name, txt_class_p_prgrm, txt_class_p_decision, txt_class_p_email;
    ImageView image_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_class);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //OkHttp Initialise
        client = new OkHttpClient();

        //Student Profile
        txt_class_p_reg_num = findViewById(R.id.text_class_p_reg_num);
        txt_class_p_student_name = findViewById(R.id.text_class_p_name);
        txt_class_p_email = findViewById(R.id.text_class_p_email);
        txt_class_p_prgrm = findViewById(R.id.text_class_p_program);
        txt_class_p_decision = findViewById(R.id.text_class_p_decision);
        image_reg = findViewById(R.id.imgv_class_p_reg_status);

        fetchUserProfile();
    }

    private void fetchUserProfile() {
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String actual_token = user.get("actual_token");
        String reg_num = user.get("reg_num");

        getData(actual_token, reg_num);

        Log.i(TAG, "fetchUserProfile: " + actual_token);

    }

    /* Consume End-Points */
    private void getData(String token, String reg_num) {

        pDialog.setMessage("Checking Student Payment & Registration Records...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("reg_num", reg_num);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_PAYMENT_PASS_CLASS)
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
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        hideDialog();
                        Toast.makeText(PassClassActivity.this, "ERROR: Failed To Reach Servers To Get Your Pass Status", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }

                    assert responseBody != null;

                    String JSON_STRING = responseBody.string();
                    Log.i(TAG, "JSON_STRING" + JSON_STRING);

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);
                    Log.i(TAG, "jsonObject" + jsonObject);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);
                    Log.i(TAG, "jsonArray" + jsonArray);

                    final int reg_status;
                    String reg_num = null;
                    String name = null;
                    String email = null;
                    String program = null;

                    // Evaluate Registration
                    if (jsonArray.length() != 0) {
                        reg_status = 1;

                        JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);
                        Log.i(TAG, "jsonObjectInnerLog" + jsonObjectInnerLog);

                        reg_num = jsonObjectInnerLog.getString("reg_num");
                        name = jsonObjectInnerLog.getString("name");
                        email = jsonObjectInnerLog.getString("email");
                        program = jsonObjectInnerLog.getString("program");

                        Log.i(TAG, "Registered");
                    } else {
                        reg_status = 0;
                        Log.i(TAG, "Not Registered");
                    }

                    final String finalRegNum = reg_num;
                    final String finalName = name;
                    final String finalEmail = email;
                    final String finalProgram = program;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Run On UI Thread");
                            hideDialog();

                            if (reg_status == 1) {
                                txt_class_p_reg_num.setText(finalRegNum);
                                txt_class_p_student_name.setText(finalName);
                                txt_class_p_email.setText(finalEmail);
                                txt_class_p_prgrm.setText(finalProgram);
                                txt_class_p_decision.setText(R.string.Can_Attend_Lecture);
                                txt_class_p_decision.setTextColor(Color.GREEN);
                                image_reg.setImageResource(R.drawable.image_approved);
                            } else {
                                txt_class_p_reg_num.setText("");
                                txt_class_p_student_name.setText("");
                                txt_class_p_email.setText("");
                                txt_class_p_prgrm.setText("");
                                txt_class_p_decision.setText(R.string.Deny_Access);
                                txt_class_p_decision.setTextColor(Color.RED);
                                image_reg.setImageResource(R.drawable.image_denied);
                            }
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
}