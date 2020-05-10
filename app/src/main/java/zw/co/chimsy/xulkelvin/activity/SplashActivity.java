package zw.co.chimsy.xulkelvin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import zw.co.chimsy.xulkelvin.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();

    }
}
