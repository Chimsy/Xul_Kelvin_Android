package zw.co.chimsy.xulkelvin.ui.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.timetable.model.List_Data_Timetable;

public class RecyclerVIewTimetableAdapter extends RecyclerView.Adapter<RecyclerVIewTimetableAdapter.ViewHolder> {
    private static final String TAG = RecyclerVIewTimetableAdapter.class.getSimpleName();

    private List<List_Data_Timetable> list_data_timetables;
    private Context context;

    public RecyclerVIewTimetableAdapter(List<List_Data_Timetable> list_data_timetables, Context context) {
        this.list_data_timetables = list_data_timetables;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final List_Data_Timetable listData = list_data_timetables.get(position);

        holder.text_timetable_course_id.setText(listData.getCourse_id());

        int year = listData.getYear();
        int semester = listData.getSemester();
        holder.text_timetable_level.setText("Level: " + year + "." + semester);
        holder.text_timetable_venue.setText("Exam Venue: " + listData.getExam_venue());
        holder.text_timetable_start_time.setText("Start Time: " + listData.getExam_time());
        holder.text_timetable_exam_duration.setText("Exam Duration: " + listData.getExam_duration());
        holder.text_timetable_exam_date.setText("Exam Date: " + listData.getExam_date());

    }

    @Override
    public int getItemCount() {
        return list_data_timetables.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView
                text_timetable_course_id,
                text_timetable_level,
                text_timetable_venue,
                text_timetable_start_time,
                text_timetable_exam_duration,
                text_timetable_exam_date;

        ViewHolder(View itemView) {
            super(itemView);
            text_timetable_course_id = itemView.findViewById(R.id.text_timetable_course_id);
            text_timetable_level = itemView.findViewById(R.id.text_timetable_level);
            text_timetable_venue = itemView.findViewById(R.id.text_timetable_venue);
            text_timetable_start_time = itemView.findViewById(R.id.text_timetable_start_time);
            text_timetable_exam_duration = itemView.findViewById(R.id.text_timetable_exam_duration);
            text_timetable_exam_date = itemView.findViewById(R.id.text_timetable_exam_date);
        }
    }
}