package zw.co.chimsy.xulkelvin.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.helper.SQLiteHandler;
import zw.co.chimsy.xulkelvin.helper.SessionManager;
import zw.co.chimsy.xulkelvin.ui.classes.activity.ClassesActivity;
import zw.co.chimsy.xulkelvin.ui.notifications.activity.NotificationsActivity;
import zw.co.chimsy.xulkelvin.ui.faq.FrequentlyAskedQuestionsActivity;
import zw.co.chimsy.xulkelvin.ui.helpdesk.HelpDeskActivity;
import zw.co.chimsy.xulkelvin.ui.passes.PassClassActivity;
import zw.co.chimsy.xulkelvin.ui.passes.PassExamActivity;
import zw.co.chimsy.xulkelvin.ui.payment.PaymentsActivity;
import zw.co.chimsy.xulkelvin.ui.results.activity.ResultsActivity;
import zw.co.chimsy.xulkelvin.ui.startup.LoginActivity;
import zw.co.chimsy.xulkelvin.ui.timetable.activity.TimeTableActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private ViewFlipper v_flipper;// For The SlideShow
    private SQLiteHandler db;
    private SessionManager session;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // SqLite database handler
        db = new SQLiteHandler(Objects.requireNonNull(getActivity()).getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());


        //View Flipper SlideShows
        int[] images = {R.drawable.slideshow_image_1, R.drawable.slideshow_image_2, R.drawable.slideshow_image_3};

        v_flipper = view.findViewById(R.id.v_flipper);

        //for loop Dzokororo
        for (int image : images) {
            flipperImages(image);
        }

        //Defining My Cards
        CardView results = view.findViewById(R.id.cardV_Results);
        CardView classes = view.findViewById(R.id.cardV_Classes);
        CardView enrollment = view.findViewById(R.id.cardV_Enrollment);
        CardView payments = view.findViewById(R.id.cardV_Payments);
        CardView pass_class = view.findViewById(R.id.cardV_Pass_Class);
        CardView pass_exam = view.findViewById(R.id.cardV_Pass_Exam);
        CardView faq = view.findViewById(R.id.cardV_FAQ);
        CardView helpdesk = view.findViewById(R.id.cardV_HelpDesk);
        CardView timetable = view.findViewById(R.id.cardV_ExamTimeTable);
        CardView exit = view.findViewById(R.id.cardV_Exit);

        TextView poweredByObj = view.findViewById(R.id.textViewHomePoweredBy); //Powered By Chimsy®

        // Adding Click Listeners To The Cards
        results.setOnClickListener(this);
        classes.setOnClickListener(this);
        enrollment.setOnClickListener(this);
        timetable.setOnClickListener(this);
        exit.setOnClickListener(this);
        payments.setOnClickListener(this);
        pass_class.setOnClickListener(this);
        pass_exam.setOnClickListener(this);
        faq.setOnClickListener(this);
        helpdesk.setOnClickListener(this);
        poweredByObj.setOnClickListener(this); // Powered By Chimsy®

        return view;
    }

    //    Slide Show Function
    private void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        //animation
        v_flipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {

            case R.id.cardV_Classes:
                i = new Intent(getContext(), ClassesActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Results:
                i = new Intent(getContext(), ResultsActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Enrollment:
                i = new Intent(getContext(), NotificationsActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Pass_Class:
                i = new Intent(getContext(), PassClassActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Pass_Exam:
                i = new Intent(getContext(), PassExamActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Payments:
                i = new Intent(getContext(), PaymentsActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_FAQ:
                i = new Intent(getContext(), FrequentlyAskedQuestionsActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_HelpDesk:
                i = new Intent(getContext(), HelpDeskActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_ExamTimeTable:
                i = new Intent(getContext(), TimeTableActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Exit:
                logoutUser();
                break;

            default:
                break;
        }

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }
}


