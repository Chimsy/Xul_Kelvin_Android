package zw.co.chimsy.xulkelvin.ui.results.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import zw.co.chimsy.xulkelvin.ui.results.adapter.RecyclerVIewResultsAdapter;
import zw.co.chimsy.xulkelvin.ui.results.model.List_Data_Results;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_COURSES_RESULTS;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_CURRENT_COURSES;

public class ResultsActivity extends AppCompatActivity {
    private static final String TAG = ResultsActivity.class.getSimpleName();

    private RecyclerView rv;
    private List<List_Data_Results> list_data_results;
    private RecyclerVIewResultsAdapter adapter;
    private ProgressDialog pDialog;
    public OkHttpClient client;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //OkHttp Initialise
        client = new OkHttpClient();

        rv = findViewById(R.id.recycler_view_results);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list_data_results = new ArrayList<>();
        adapter = new RecyclerVIewResultsAdapter(list_data_results, this);

        fetchUserProfile();
    }

    private void fetchUserProfile() {
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String actual_token = user.get("actual_token");
        String reg_num = user.get("reg_num");
        String program = user.get("program");

        getData(actual_token, reg_num, program);

        Log.i(TAG, "fetchUserProfile: " + actual_token);

    }

    /* Consume End-Points */
    private void getData(String token, String reg_num, String program) {

        pDialog.setMessage("Fetching Your Results Profile...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("reg_num", reg_num);
        postData.addProperty("program", program);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_COURSES_RESULTS)
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
                        Toast.makeText(ResultsActivity.this, "ERROR: Failed To Reach Servers To Get Your Results", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }

                    assert responseBody != null;

                    String JSON_STRING = responseBody.string();

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);

                    JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);
                    Log.i(TAG, "onResponse: " + jsonObjectInnerLog.getString("cource_id"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectInner = jsonArray.getJSONObject(i);

                        List_Data_Results ld = new List_Data_Results(
                                jsonObjectInner.getString("cource_id"),
                                jsonObjectInner.getString("course_work_mark"),
                                jsonObjectInner.getString("course_exam_mark"),
                                jsonObjectInner.getString("course_exam_grade"));

                        list_data_results.add(ld);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: tirikuwanikawo here muno");
                            hideDialog();
                            rv.setAdapter(adapter);
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
