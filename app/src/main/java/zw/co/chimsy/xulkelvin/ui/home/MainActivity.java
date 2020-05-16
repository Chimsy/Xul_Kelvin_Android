package zw.co.chimsy.xulkelvin.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.home.fragment.HomeFragment;
import zw.co.chimsy.xulkelvin.helper.SQLiteHandler;
import zw.co.chimsy.xulkelvin.helper.SessionManager;
import zw.co.chimsy.xulkelvin.ui.startup.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        TextView txtToken = findViewById(R.id.token);
        TextView txtName = findViewById(R.id.name);
        TextView txtEmail = findViewById(R.id.email);
        TextView txtRegNum = findViewById(R.id.reg_num);
        TextView txtYear = findViewById(R.id.year);
        TextView txtSemester = findViewById(R.id.semester);
        TextView txtProgram = findViewById(R.id.program);

        Button btnLogout = findViewById(R.id.btnLogout);*/

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String actual_token = user.get("actual_token");
        String name = user.get("name");
        String email = user.get("email");
        String reg_num = user.get("reg_num");
        String year = user.get("year");
        String semester = user.get("semester");
        String program = user.get("program");
/*
        // Displaying the user details on the screen
        txtToken.setText(actual_token);
        txtName.setText(name);
        txtEmail.setText(email);
        txtRegNum.setText(reg_num);
        txtYear.setText(year);
        txtSemester.setText(semester);
        txtProgram.setText(program);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });*/

        // YAZIPO Home Page a.k.a DashBoard (Tora Fragment)
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, homeFragment, homeFragment.getTag()).commit();

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
