package zw.co.chimsy.xulkelvin.ui.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

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
import zw.co.chimsy.xulkelvin.helper.SessionManager;
import zw.co.chimsy.xulkelvin.ui.home.MainActivity;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ACTUAL_TOKEN;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_DATA;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_EMAIL;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ERROR;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ID;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_MESSAGE;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_NAME;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_PROGRAM;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_REG_NUMBER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_SEMESTER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_USER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_USER_TOKEN;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_YEAR;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_LOGIN;

public class LoginActivity extends AppCompatActivity {

    private static final String APP_TAG = LoginActivity.class.getSimpleName();

    private MySweetAlerts alerts;
    private Activity activity;
    private OkHttpClient client;
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String ResponseValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        activity = LoginActivity.this;
        alerts = new MySweetAlerts(this);

        client = new OkHttpClient();

        inputEmail = findViewById(R.id.editText_email);
        inputPassword = findViewById(R.id.editText_password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(this);

        // Session manager
        session = new SessionManager(this);

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(LoginActivity.this, "Please Enter Your Correct Credentials To Login", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                /*
                //@TODO Dont forget to remove this session
                session.setLogin(true);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                */
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("email", email);
        postData.addProperty("password", password);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_LOGIN)
                .post(postBody)
                .build();

        client.newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                hideDialog();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {

                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        hideDialog();
                        Toast.makeText(LoginActivity.this, "ERROR: Failed To Reach Servers To Authenticate", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }
                    hideDialog();

                    assert responseBody != null;

                    String JSON_STRING = responseBody.string();

                    // get JSONObject from JSON file
                    JSONObject obj = new JSONObject(JSON_STRING);

                    // fetch JSONObject named user
                    JSONObject jsonObject = obj.getJSONObject(KEY_DATA);

                    JSONObject jsonObjectInnerError = jsonObject.getJSONObject(KEY_MESSAGE);
                    boolean error = jsonObjectInnerError.getBoolean(KEY_ERROR);

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        JSONObject user = jsonObject.getJSONObject(KEY_USER);
                        JSONObject access_Token = jsonObject.getJSONObject(KEY_USER_TOKEN);

                        // Now store the user in SQLite
                        int id = user.getInt(KEY_ID);
                        String token = access_Token.getString(KEY_ACTUAL_TOKEN);
                        String name = user.getString(KEY_NAME);
                        String email = user.getString(KEY_EMAIL);
                        String reg_num = user.getString(KEY_REG_NUMBER);
                        int year = user.getInt(KEY_YEAR);
                        int semester = user.getInt(KEY_SEMESTER);
                        String program = user.getString(KEY_PROGRAM);


                        // Inserting row in users table
                        db.addUser(token, name, email, reg_num, year, semester, program);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jsonObjectInnerError.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


    /* Progress Dialog*/
    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
    /* Progress Dialog End*/

}
