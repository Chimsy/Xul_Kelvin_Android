package zw.co.chimsy.xulkelvin.ui.helpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import zw.co.chimsy.xulkelvin.ui.home.MainActivity;
import zw.co.chimsy.xulkelvin.ui.startup.LoginActivity;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_CURRENT_COURSES;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_POST_MESSAGES;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.WEB_HOOK;

public class HelpDeskActivity extends AppCompatActivity {
    private static final String TAG = HelpDeskActivity.class.getSimpleName();

    EditText txtMsgTitle, txtMsgBody;
    Button submit;
    public OkHttpClient client;
    private SQLiteHandler db;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //OkHttp Initialise
        client = new OkHttpClient();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        txtMsgTitle = findViewById(R.id.editText_msg_title);
        txtMsgBody = findViewById(R.id.editText_msg_body);
        submit = findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMsgTitle.length() == 0 || txtMsgBody.length() == 0) {
                    Toast.makeText(HelpDeskActivity.this, "Message Title & Body Cannot Be Empty For You To Submit Your Request", Toast.LENGTH_LONG).show();
                }
                if (txtMsgTitle.length() > 3 && txtMsgBody.length() > 8) {
                    postStudentHelpdeskRequest(txtMsgTitle.getText().toString(), txtMsgBody.getText().toString());
                } else {
                    Toast.makeText(HelpDeskActivity.this, "Message Title & Body To Short", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void postStudentHelpdeskRequest(String MessageTitle, String MessageBody) {
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String actual_token = user.get("actual_token");
        String reg_num = user.get("reg_num");

        postData(actual_token, reg_num, MessageTitle, MessageBody);

        Log.i(TAG, "fetchUserProfile: " + actual_token);

    }

    /* Consume End-Points */
    private void postData(String token, String reg_num, String msg_title, String msg_body) {

        pDialog.setMessage("Connecting To Messaging Server...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("reg_num", reg_num);
        postData.addProperty("msg_title", msg_title);
        postData.addProperty("msg_body", msg_body);
        postData.addProperty("msg_state", 0);
        postData.addProperty("msg_type", "private");

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_POST_MESSAGES)
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
                        Toast.makeText(HelpDeskActivity.this, "ERROR: Failed To Reach Servers To Post Your Request", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }


                    assert responseBody != null;
                    String JSON_STRING = responseBody.string();

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);

                    JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);
                    Log.i(TAG, "onResponse: " + jsonObjectInnerLog.getString("course_id"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            // Launching the login activity
                            Intent intent = new Intent(HelpDeskActivity.this, Notification.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(HelpDeskActivity.this, "Request Send Please Check Your Notification, For Your Response.", Toast.LENGTH_SHORT).show();
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