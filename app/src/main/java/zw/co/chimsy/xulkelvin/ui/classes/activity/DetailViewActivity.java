package zw.co.chimsy.xulkelvin.ui.classes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zw.co.chimsy.xulkelvin.R;

public class DetailViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        TextView nametxt = findViewById(R.id.name_txt);
        ImageView fullimg = findViewById(R.id.full_image);

        Intent i = getIntent();

        String name = i.getStringExtra("name");
        String imageurl = i.getStringExtra("imageurl");

        nametxt.setText(name);

        Picasso.with(this)
                .load(imageurl)
                .into(fullimg);

    }
}

