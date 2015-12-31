package com.example.guillem.mycalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends ActionBarActivity {
    TableLayout tl;
    TableRow tr;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int number = c.get(Calendar.DAY_OF_MONTH);
        int weekPos = c.get(Calendar.DAY_OF_WEEK);
        tl = new TableLayout(getApplicationContext());

        super.onCreate(savedInstanceState);
        getWeekFromToday();
        //fillWeekTable(tl,number,weekPos);
        setContentView(R.layout.activity_main);
    }

    private String[] getWeekFromToday(){
        Log.d(TAG,"getWeekFromToday");
        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG,Arrays.toString(days));
        return days;

    }

    private void fillWeekTable(TableLayout tl, int number, int weekPos){
        tl = (TableLayout)findViewById(R.id.tableLayout);
        String[] days = getWeekFromToday();
        for(int i = 0; i < days.length; ++i){
            View child = tl.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                for (int x = 0; x < row.getChildCount(); x++) {
                    View view = row.getChildAt(x);
                    view.setEnabled(false);
                }
            }
        }
        /*
        for (int i = 0; i < tl.getChildCount(); i++) {
            View child = tl.getChildAt(i);
            Log.d(TAG,"num row = "+i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    View view = row.getChildAt(x);
                    view.setEnabled(false);
                }
            }
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
