package com.example.guillem.mycalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OneDayActivity extends Activity {
    private static final String TAG = "OneDayActivity";

    private CalendarDbHelper cdbh = new CalendarDbHelper(this,"MeetingsDB",null,1);
    private SQLiteDatabase db;
    private Calendar myCalendar;

    private String dayDate;

    // *TextView
    private TextView textViewPrevDate;
    private TextView textViewDate;
    private TextView textViewNextDate;
    private TextView textViewSun;



    // * Views
    private RelativeLayout relativeLayoutSunday;


    // *IMges
    public String[] NextPreWeekday;
    public String firstDayOfWeek;
    public String lastDayOfWeek;

    public static ArrayList<Entity1> arrayListEntity = new ArrayList<Entity1>();
    public static ArrayList<Entity1> arrayListEButtonId = new ArrayList<Entity1>();

    public int weekDaysCount = 0;
    public ArrayList<WeekSets> weekDatas;
    String tapMargin ;
    String buttonHight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day);
        Intent intent = getIntent();
        String dayInfo = intent.getStringExtra("dayInfo");
        dayDate = intent.getStringExtra("dayDate");
        Log.d(TAG, "dayInfo ->" + dayInfo);

        //Start Calendar
        myCalendar = Calendar.getInstance();

        // DATABASE START
        db = cdbh.getReadableDatabase();

        textViewPrevDate = (TextView) findViewById(R.id.textViewPrevDate);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewNextDate = (TextView) findViewById(R.id.textViewNextDate);

        relativeLayoutSunday = (RelativeLayout) findViewById(R.id.relativeLayoutOneDay);
        textViewDate.setClickable(false);
        textViewDate.setText(dayDate);


        try
        {
            Log.d(TAG,"Dins el try");
            new LoadViewsInToWeekView2().execute("");
        } catch (Exception e)
        {
            Log.d(TAG,"Dins el catch");
            Log.getStackTraceString(e);
        }



    }

    public void addNewEvent(View view){
        Log.d(TAG,"Add new event clicked");
        Intent intent = new Intent(this, NewMeeting.class);
        startActivity(intent);
    }

    public void addNewTask(View view) {
        Log.d(TAG, "Add new event clicked");
        Intent intent = new Intent(this, NewTask.class);
        startActivity(intent);
    }

    public void openHelp(View view) {
        Log.d(TAG, "Add new event clicked");
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }


    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textViewDate.setText(sdf.format(myCalendar.getTime()));
    }


    public String[] getWeekDay() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(Calendar.DAY_OF_WEEK)+2;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    public String[] getWeekOfDay(Calendar cal) {

        Calendar now = cal;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(Calendar.DAY_OF_WEEK)+2;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayNext() {

        weekDaysCount++;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayPrev() {

        weekDaysCount--;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    public Button getButtonToLayout(int higth, int marginTop,
                                    String buttonText, String jobID, int buttonID) {

        @SuppressWarnings("deprecation")
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, higth);
        Button button = new Button(getApplicationContext());
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.parseColor("#51b460"));
        button.setText(buttonText);
        button.setOnClickListener(buttonOnclckForAllWeekButton);
        button.setTextSize(9);
        button.setId(buttonID);
        params.setMargins(0, marginTop, 0, 0);
        arrayListEntity.add(getEntity(jobID, buttonText));

        return button;

    }

    public OnClickListener buttonOnclckForAllWeekButton = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            String buttonText = b.getText().toString();
            int position = 0;

            for (int j = 0; j < arrayListEntity.size(); j++) {
                Entity1 itemOne = arrayListEntity.get(j);

                String arryJobRefID = itemOne.JobRefID;
                if (arryJobRefID.equals(buttonText)) {
                    position = j;
                    break;
                }
            }

            Entity1 itemOne1 = arrayListEntity.get(position);
            Toast.makeText(getApplicationContext() , "  " + itemOne1.JobRefID , Toast.LENGTH_SHORT).show();

        }
    };

    public static Entity1 getEntity(String jobID, String jobRefID) {
        Entity1 E = new Entity1();
        E.JobIDForButton = jobID;
        E.JobRefID = jobRefID;
        return E;

    }

    public static Entity1 getButton(int layoutView, int buttonTag) {
        Entity1 E = new Entity1();
        E.layoutView = layoutView;
        E.buttonTag = buttonTag;
        return E;

    }

    public static WeekSets getWeekValues(String weekDay, String jobId,
                                         String jobRefId, String tapMargina, String buttonHighta) {
        WeekSets W = new WeekSets();
        W.day = weekDay;
        W.jobID = jobId;
        W.jobRefID = jobRefId;
        W.tapMargin = tapMargina;
        W.buttonHight = buttonHighta;

        return W;
    }

    public String getWithAndHigthToButton(int startTime) {

        int time;
        String size = "0";
        try {
            time = startTime;
            return Integer.toString(time * 80);

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

        return size;

    }

    public String getHightOfButton(int startTime, int endTime) {
        String hight = "0";
        try {
            int subHigth = endTime - startTime;

            hight = String.valueOf(subHigth * 80);
            Log.d(TAG,"hight ->"+hight);

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

        return hight;

    }

    public void showTasks(View view) {
        Log.d(TAG,"Add new event clicked");
        Intent intent = new Intent(this, ListTasks.class);
        startActivity(intent);
    }

    public void changeViewToDay(View view) {
        Log.d(TAG,"Add new event clicked");
        TextView t = (TextView)view;
        String day = t.getText().toString();
        Intent intent = new Intent(this, OneDayActivity.class);
        intent.putExtra("dayInfo",day);
        startActivity(intent);
    }


    public class LoadViewsInToWeekView2 extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            try {
                //Fem les crides necessaries a la BD (una per dia)
                Log.d(TAG,"Anem a fer les queries pertinents per la setmana");
                int dayPosition = 0;
                weekDatas = new ArrayList<WeekSets>();
                Log.d(TAG,"dayDate = "+dayDate);
                String formData = formatData(dayDate);
                Cursor c = db.query(false, "Meetings", null, "day='" + formData + "'", null, null, null, null, null);
                if(c.moveToFirst()){
                    do {
                        int hIni = c.getInt(c.getColumnIndexOrThrow("startTime"));
                        int hEnd = c.getInt(c.getColumnIndexOrThrow("endTime"));
                        Log.d(TAG,"hIni = "+Integer.toString(hIni));
                        Log.d(TAG,"hEnd = "+Integer.toString(hEnd));
                        String description = c.getString(c.getColumnIndexOrThrow("description"));
                        tapMargin = getWithAndHigthToButton(hIni);
                        buttonHight = getHightOfButton(hIni, hEnd);
                        //dayPosition = (i != 0 ) ? i -1 : i + 6;
                        weekDatas.add(getWeekValues(String.valueOf(0),"12",description,tapMargin,buttonHight));
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private String formatData(String dataOrg){
            String [] parts = dataOrg.split("-");
            if(parts[2].indexOf('0') == 0){
                StringBuilder sb = new StringBuilder(parts[2]);
                sb.deleteCharAt(0);
                parts[2] = sb.toString();
            }
            if(parts[1].indexOf('0') == 0){
                StringBuilder sb1 = new StringBuilder(parts[1]);
                sb1.deleteCharAt(0);
                parts[1] = sb1.toString();
            }
            return parts[2]+"/"+parts[1]+"/"+parts[0];
        }

        @Override
        protected void onPostExecute(String str) {

            try {
                relativeLayoutSunday.removeAllViews();
                WeekSets weekToDay;
                int length = weekDatas.size();
                Log.i("length===>", String.valueOf(length));

                if (length != 0) {
                    for (int k = 0; k < weekDatas.size(); k++) {
                        weekToDay = weekDatas.get(k);

                        int day = Integer.parseInt(weekToDay.day);
                        switch (day) {
                            case 0:
                                int sunday = 100;
                                relativeLayoutSunday
                                        .addView(getButtonToLayout(
                                                Integer.parseInt(weekToDay.buttonHight),
                                                Integer.parseInt(weekToDay.tapMargin),
                                                weekToDay.jobRefID,
                                                weekToDay.jobID, sunday));
                                arrayListEButtonId.add(getButton(0, sunday));
                                sunday++;
                                break;
                            default:
                                break;
                        }

                    }

                }

            } catch (Exception e) {
                Log.getStackTraceString(e);
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            try {

                dialog = ProgressDialog.show(OneDayActivity.this, null, null,
                        true, false);
                dialog.setContentView(R.layout.progress_layout);
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }

        }

    }

}

