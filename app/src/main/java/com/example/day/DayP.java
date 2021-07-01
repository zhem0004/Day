package com.example.day;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class DayP extends AppWidgetProvider {

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

    /**
     * An object that is passed as parameter to widget updating methods, set as static variable to
     * make the updating fumctionality more extendable
     */
    private static int appWidgId;

    /**
     * An object that is passed as parameter to widget updating methods, set as static variable to
     * make the updating fumctionality more extendable
     */
    private static AppWidgetManager am;

    /**
     * A method that is called when the widget is placed on homescreen
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        appWidgId = appWidgetId;
        am = appWidgetManager;

        long t = LocalTime.now(ZoneId.of("America/Indiana/Indianapolis")).truncatedTo(ChronoUnit.SECONDS).toSecondOfDay();

        Long activeTM = (t - PREMORNING) / 60;
        Long periods = (TOTALACTIVETIME - activeTM)/9;
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.day_p);
        views.setTextViewText(R.id.appwidget_text, periods.toString());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * A method that is called to update the widget, by default it is once i half an hour,
     * but with timer widget is updated once a minute
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.day_p);

            final Handler handler = new Handler();
            Timer timer = new Timer();

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    handler.post(new Runnable() {

                        /**
                         * A method that uses local time in seconds and person's schedule to display
                         * how much time of the day does a person has left in percent
                         */
                        public void run() {

                            /* Current time of the day in seconds */
                            long t =LocalTime.now(ZoneId.of( "America/Indiana/Indianapolis" )).truncatedTo(ChronoUnit.SECONDS).toSecondOfDay();

                            /* Subtracting the time of sleep to get the time when the person is active */
                            Long activeTM = (t-PREMORNING)/60; //turning seconds to minutes


                            /*  Substracting from potal active time the time that has already passed
                            Since one percent for a regular schedule is 9 minutes, division by nine gives the percentage  */
                            Long periods = (TOTALACTIVETIME - activeTM)/9;

                            views.setTextViewText(R.id.appwidget_text, periods.toString());
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(task, 0, 60000);

        }
    }

}
