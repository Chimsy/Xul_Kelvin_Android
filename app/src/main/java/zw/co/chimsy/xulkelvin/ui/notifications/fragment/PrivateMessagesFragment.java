package zw.co.chimsy.xulkelvin.ui.notifications.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import java.util.Objects;

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
import zw.co.chimsy.xulkelvin.ui.home.fragment.HomeFragment;
import zw.co.chimsy.xulkelvin.ui.notifications.adapter.RecyclerVIewNotificationsAdapter;
import zw.co.chimsy.xulkelvin.ui.notifications.model.List_Data_Notifications;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_RESULT;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_COURSES_RESULTS;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.API_GET_MESSAGES;
import static zw.co.chimsy.xulkelvin.utils.AppUrls.WEB_HOOK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateMessagesFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private RecyclerView rv;
    private List<List_Data_Notifications> list_data_notifications;
    private RecyclerVIewNotificationsAdapter adapter;
    private ProgressDialog pDialog;
    private OkHttpClient client;
    private SQLiteHandler db;

    public PrivateMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_messages, container, false);

        // SqLite database handler
        db = new SQLiteHandler(Objects.requireNonNull(getActivity()).getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        //OkHttp Initialise
        client = new OkHttpClient();

        rv = view.findViewById(R.id.recycler_view_private_messages);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        list_data_notifications = new ArrayList<>();
        adapter = new RecyclerVIewNotificationsAdapter(list_data_notifications, getContext());

        fetchUserProfile();


        return view;

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

        pDialog.setMessage("Fetching Your Private Messages...");
        showDialog();

        // Making a Post Request
        JsonObject postData = new JsonObject();
        postData.addProperty("reg_num", reg_num);
        postData.addProperty("msg_type", "private");

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody postBody = RequestBody.create(postData.toString(), JSON);
        Request post = new Request.Builder()
                .url(API_GET_MESSAGES)
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
                        Toast.makeText(getContext(), "ERROR: Failed To Reach Servers To Get Your Messages", Toast.LENGTH_SHORT).show();
                        throw new IOException("Unexpected code " + response);
                    }

                    assert responseBody != null;

                    String JSON_STRING = responseBody.string();

                    // get JSONObject from JSON file
                    JSONObject jsonObject = new JSONObject(JSON_STRING);

                    // fetch JSONArray
                    JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);

                    JSONObject jsonObjectInnerLog = jsonArray.getJSONObject(0);
                    Log.i(TAG, "onResponse: " + jsonObjectInnerLog.getString("msg_title"));
                    Log.i(TAG, "onResponse: " + jsonObjectInnerLog.getString("msg_body"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectInner = jsonArray.getJSONObject(i);

                        List_Data_Notifications ld = new List_Data_Notifications(
                                jsonObjectInner.getString("msg_title"),
                                jsonObjectInner.getString("msg_body"));

                        list_data_notifications.add(ld);
                    }

                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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
