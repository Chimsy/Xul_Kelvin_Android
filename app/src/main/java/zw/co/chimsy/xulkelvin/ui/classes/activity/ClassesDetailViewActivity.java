package zw.co.chimsy.xulkelvin.ui.classes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import zw.co.chimsy.xulkelvin.R;

public class ClassesDetailViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_detail_view);

        TextView textview_course_id_name = findViewById(R.id.textview_course_id_name);
        TextView textview_course_description = findViewById(R.id.textview_course_description);

        Intent i = getIntent();

        String text_course_id = i.getStringExtra("text_course_id");
        String text_course_name = i.getStringExtra("text_course_name");
        String text_course_description = i.getStringExtra("text_course_description");

        textview_course_id_name.setText(text_course_id + " - " + text_course_name);
        textview_course_description.setText(text_course_description);

    }
}

