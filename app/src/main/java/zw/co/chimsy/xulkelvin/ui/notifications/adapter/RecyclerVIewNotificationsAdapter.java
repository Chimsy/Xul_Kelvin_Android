package zw.co.chimsy.xulkelvin.ui.notifications.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.notifications.model.List_Data_Notifications;

public class RecyclerVIewNotificationsAdapter extends RecyclerView.Adapter<RecyclerVIewNotificationsAdapter.ViewHolder> {
    private static final String TAG = RecyclerVIewNotificationsAdapter.class.getSimpleName();

    private List<List_Data_Notifications> list_data_results;
    private Context context;

    public RecyclerVIewNotificationsAdapter(List<List_Data_Notifications> list_data_results, Context context) {
        this.list_data_results = list_data_results;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final List_Data_Notifications listData = list_data_results.get(position);

        holder.text_msg_title.setText(listData.getMsg_title());
        holder.text_msg_body.setText(listData.getMsg_body());

    }

    @Override
    public int getItemCount() {
        return list_data_results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_msg_title, text_msg_body;

        ViewHolder(View itemView) {
            super(itemView);
            text_msg_title = itemView.findViewById(R.id.text_msg_title);
            text_msg_body = itemView.findViewById(R.id.text_msg_body);
        }
    }
}