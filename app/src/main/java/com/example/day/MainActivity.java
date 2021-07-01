package com.example.day;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

/**
 * An application that displays how much time of the day does the person has left in percent
 */
public class MainActivity extends AppCompatActivity {

    /**
     * A handler that is required to implement runnable
     */
    Handler handler = new Handler();

    /**
     * The object that contains app functionality passed as a method run
     */
    Runnable runnable;

    /**
     * Time in milliseconds that will delay each call of run method
     */
    private static int DELAY = 10000;

    /**
     * Time in seconds from 00 to 7:00 am
     */
    private static int PREMORNING = 25200;

    /**
     * Total time on minutes that a person stays active throughout day
     */
    private static int TOTALACTIVETIME = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * A method that is called when the app is active
     */
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {

            /**
             * A method that uses local time in seconds and person's schedule to display
             * how much time of the day does a person has left in percent
             */
            public void run() {
                handler.postDelayed(runnable, DELAY);
                TextView d = findViewById(R.id.display);

                /* Current time of the day in seconds */
                long t =LocalTime.now(ZoneId.of( "America/Indiana/Indianapolis" )).truncatedTo(ChronoUnit.SECONDS).toSecondOfDay();

                /* Subtracting the time of sleep to get the time when the person is active */
                Long activeTM = (t-PREMORNING)/60; //turning seconds to minutes


                /*  Substracting from potal active time the time that has already passed
                Since one percent for a regular schedule is 9 minutes, division by nine gives the percentage  */
                Long periods = (TOTALACTIVETIME - activeTM)/9;

                d.setText(periods.toString());
            }
        }, DELAY);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }
}