package zw.co.chimsy.xulkelvin.ui.classes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
import zw.co.chimsy.xulkelvin.ui.classes.adapter.RecyclerVIewClassesAdapter;
import zw.co.chimsy.xulkelvin.ui.classes.model.List_Data_Classes;
import zw.co.chimsy.xulkelvin.ui.home.MainActivity;
import zw.co.chimsy.xulkelvin.ui.startup.LoginActivity;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ACTUAL_TOKEN;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_DATA;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_EMAIL;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ERROR;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ID;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_MESSAGE;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_NAME;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_PROGRAM;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_REG_NUMBER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_SEMESTER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_USER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_USER_TOKEN;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_YEAR;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_CURRENT_COURSES;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_LOGIN;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.WEB_HOOK;

public class ClassesActivity extends AppCompatActivity {
    //    private static final String DATA_URL = "https://uniqueandrocode.000webhostapp.com/hiren/androidweb.php";
    private RecyclerView rv;
    private List<List_Data_Classes> list_datum_classes;
    private RecyclerVIewClassesAdapter adapter;
    private ProgressDialog pDialog;
    public OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //OkHttp Initialise
        client = new OkHttpClient();


        rv = findViewById(R.id.recycler_view_classes);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list_datum_classes = new ArrayList<>();
        adapter = new RecyclerVIewClassesAdapter(list_datum_classes, this);

        getData();
    }

    /* Consume End-Points */
    private void getData() {

        pDialog.setMessage("Fetching Your Current Classes...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("year", 4);
        postData.addProperty("semester", 1);
        postData.addProperty("program", "BMIS");

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_CURRENT_COURSES)
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
                        Toast.makeText(ClassesActivity.this, "ERROR: Failed To Reach Servers To Get Your Classes", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }
                    hideDialog();

                    assert responseBody != null;

                    String JSON_STRING = responseBody.string();

                    // get JSONObject from JSON file
                    JSONObject obj = new JSONObject(JSON_STRING);

                    // fetch JSONArray
                    JSONArray array = obj.getJSONArray(KEY_DATA);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject ob = array.getJSONObject(i);
                        List_Data_Classes ld = new List_Data_Classes(
                                ob.getString("name"),
                                ob.getString("imageurl"));
                        list_datum_classes.add(ld);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

    /*
    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject ob = array.getJSONObject(i);
                        List_Data_Classes ld = new List_Data_Classes(ob.getString("name"), ob.getString("imageurl"));
                        list_datum_classes.add(ld);
                    }
                    rv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
     */


    /* Progress Dialog*/
    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
    /* Progress Dialog End*/
}
