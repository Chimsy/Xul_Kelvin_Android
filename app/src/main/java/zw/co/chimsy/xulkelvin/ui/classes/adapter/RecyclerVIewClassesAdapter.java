package zw.co.chimsy.xulkelvin.ui.classes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.classes.activity.ClassesDetailViewActivity;
import zw.co.chimsy.xulkelvin.ui.classes.model.List_Data_Classes;

public class RecyclerVIewClassesAdapter extends RecyclerView.Adapter<RecyclerVIewClassesAdapter.ViewHolder> {
    private static final String TAG = RecyclerVIewClassesAdapter.class.getSimpleName();

    private List<List_Data_Classes> list_data_classes;
    private Context context;

    public RecyclerVIewClassesAdapter(List<List_Data_Classes> list_data_classes, Context context) {
        this.list_data_classes = list_data_classes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_classes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final List_Data_Classes listData = list_data_classes.get(position);

        holder.text_cource_id.setText(listData.getCource_id());
        holder.text_cource_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClassesDetailViewActivity.class);
                intent.putExtra("text_cource_id", listData.getCource_id());
                intent.putExtra("text_course_name", listData.getCourse_name());
                intent.putExtra("text_course_description", listData.getCourse_description());
                context.startActivity(intent);
            }
        });
        holder.text_course_name.setText(listData.getCourse_name());
        holder.text_course_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClassesDetailViewActivity.class);
                intent.putExtra("text_cource_id", listData.getCource_id());
                intent.putExtra("text_course_name", listData.getCourse_name());
                intent.putExtra("text_course_description", listData.getCourse_description());
                context.startActivity(intent);
            }
        });
        holder.text_course_description.setText(listData.getCourse_description());
        holder.text_course_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClassesDetailViewActivity.class);
                intent.putExtra("text_cource_id", listData.getCource_id());
                intent.putExtra("text_course_name", listData.getCourse_name());
                intent.putExtra("text_course_description", listData.getCourse_description());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_data_classes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_cource_id;
        private TextView text_course_name;
        private TextView text_course_description;

        ViewHolder(View itemView) {
            super(itemView);
            text_cource_id = itemView.findViewById(R.id.text_cource_id);
            text_course_name = itemView.findViewById(R.id.text_course_name);
            text_course_description = itemView.findViewById(R.id.text_course_description);
        }
    }
}