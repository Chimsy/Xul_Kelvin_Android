package zw.co.chimsy.xulkelvin.ui.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_PROGRAM_IDS;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.WEB_HOOK;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister, btnLinkToLogin;
    private EditText inputFullName, inputEmail, inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Spinner inputSpinner;
    private OkHttpClient client;


    List<String> usersList;
    ArrayAdapter<String> catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        client = new OkHttpClient();

        // Fetch Program Spinner Data
        inputSpinner = findViewById(R.id.spinner_programs);
        usersList = new ArrayList<>();

        catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        inputSpinner.setAdapter(catAdapter);


        inputFullName = findViewById(R.id.editText_name);
        inputEmail = findViewById(R.id.editText_email);
        inputPassword = findViewById(R.id.editText_password);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getPrograms();

        // SQLite database handler
        db = new SQLiteHandler(this);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String role = "student";

                Random r = new Random();
                int reg_num_integers = r.nextInt(411855 - 311855) + 65;
                String reg_num = "R" + reg_num_integers + "Q";

                int year = 1;
                int semester = 1;
                String program = inputSpinner.getSelectedItem().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password, role, reg_num, year, semester, program);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * @param name
     * @param email
     * @param password
     * @param role
     * @param reg_num
     * @param year
     * @param semester
     * @param program
     */
    private void registerUser(String name, String email, String password, String role,
                              String reg_num, int year, int semester, String program) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("name", name);
        postData.addProperty("email", email);
        postData.addProperty("password", password);
        postData.addProperty("role", role);
        postData.addProperty("reg_num", reg_num);
        postData.addProperty("year", year);
        postData.addProperty("semester", semester);
        postData.addProperty("program", program);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(WEB_HOOK)
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
                        Toast.makeText(RegisterActivity.this, "ERROR: Failed To Reach Servers To Authenticate", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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


    /*
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);*/
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void getPrograms() {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_PROGRAM_IDS)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage();
                assert mMessage != null;
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String mMessage = Objects.requireNonNull(response.body()).string();

                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Response", mMessage);
                        try {
                            JSONObject users = new JSONObject(mMessage);
                            JSONArray usersArr = users.getJSONArray("result");

                            for (int i = 0; i < usersArr.length(); i++) {
                                JSONObject user = usersArr.getJSONObject(i);
                                Log.i("program_code", user.getString("program_code"));
                                usersList.add(user.getString("program_code"));
                            }

                            catAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }


}
