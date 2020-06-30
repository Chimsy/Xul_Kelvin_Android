package zw.co.chimsy.xulkelvin.helper;

import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class SourceReferences {

    private static final String TAG = SourceReferences.class.getSimpleName();

    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
    int day = now.get(Calendar.DAY_OF_MONTH);
    int hour = now.get(Calendar.HOUR_OF_DAY);
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);
    int millis = now.get(Calendar.MILLISECOND);


/*
    LocalDateTime now = LocalDateTime.now();
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();
    int hour = now.getHour();
    int minute = now.getMinute();
    int second = now.getSecond();
    int millis = now.get(ChronoField.MILLI_OF_SECOND); // Note: no direct getter available.
*/


    public String generate_source_ref() {
        Log.d(TAG, "Source References Generated");
        return "OCSS-" + year + month + day + hour + minute + second + millis;
    }
}
