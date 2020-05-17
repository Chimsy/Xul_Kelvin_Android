package zw.co.chimsy.xulkelvin.ui.results.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.results.model.List_Data_Results;

public class RecyclerVIewResultsAdapter extends RecyclerView.Adapter<RecyclerVIewResultsAdapter.ViewHolder> {
    private static final String TAG = RecyclerVIewResultsAdapter.class.getSimpleName();

    private List<List_Data_Results> list_data_results;
    private Context context;

    public RecyclerVIewResultsAdapter(List<List_Data_Results> list_data_results, Context context) {
        this.list_data_results = list_data_results;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_results, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final List_Data_Results listData = list_data_results.get(position);

        holder.text_result_cource_id.setText(listData.getCource_id());
        holder.text_result_exam_grade.setText("Grade: " + listData.getCourse_exam_grade());
        holder.text_result_course_work.setText("Course WorkMark: " + listData.getCourse_work_mark() + "%");
        holder.text_result_exam_mark.setText("Exam Mark: " + listData.getCourse_exam_mark() + "%");

        int exam = Integer.parseInt(listData.getCourse_exam_mark());
        int courseWork = Integer.parseInt(listData.getCourse_work_mark());
        int overalMark = exam + courseWork;

        holder.text_result_overal_mark.setText("Overal Mark: " + overalMark + "%");


    }

    @Override
    public int getItemCount() {
        return list_data_results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView
                text_result_cource_id,
                text_result_exam_grade,
                text_result_course_work,
                text_result_exam_mark,
                text_result_overal_mark;

        ViewHolder(View itemView) {
            super(itemView);
            text_result_cource_id = itemView.findViewById(R.id.text_result_cource_id);
            text_result_exam_grade = itemView.findViewById(R.id.text_result_exam_grade);
            text_result_course_work = itemView.findViewById(R.id.text_result_course_work);
            text_result_exam_mark = itemView.findViewById(R.id.text_result_exam_mark);
            text_result_overal_mark = itemView.findViewById(R.id.text_result_overal_mark);
        }
    }
}