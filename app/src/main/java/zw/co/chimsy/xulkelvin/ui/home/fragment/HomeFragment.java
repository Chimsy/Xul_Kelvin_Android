package zw.co.chimsy.xulkelvin.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.classes.activity.ClassesActivity;
import zw.co.chimsy.xulkelvin.ui.enrollment.EnrollmentActivity;
import zw.co.chimsy.xulkelvin.ui.helpdesk.HelpDeskActivity;
import zw.co.chimsy.xulkelvin.ui.payment.PaymentsActivity;
import zw.co.chimsy.xulkelvin.ui.results.ResultsActivity;
import zw.co.chimsy.xulkelvin.ui.timetable.TimeTableActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  implements View.OnClickListener {
    private ViewFlipper v_flipper;// For The SlideShow

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


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
        CardView helpdesk = view.findViewById(R.id.cardV_HelpDesk);
        CardView timetable = view.findViewById(R.id.cardV_ExamTimeTable);

        TextView poweredByObj = view.findViewById(R.id.textViewHomePoweredBy); //Powered By Chimsy®

        // Adding Click Listeners To The Cards
        results.setOnClickListener(this);
        classes.setOnClickListener(this);
        enrollment.setOnClickListener(this);
        timetable.setOnClickListener(this);
        payments.setOnClickListener(this);
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
                i = new Intent(getContext(), EnrollmentActivity.class);
                startActivity(i);
                break;

            case R.id.cardV_Payments:
                i = new Intent(getContext(), PaymentsActivity.class);
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

            default:
                break;
        }

    }
}


