package com.example.guillem.mycalendar;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewMeeting extends Activity {

    private static final String TAG = "NewMeeting";
    private CalendarDbHelper cdbh = new CalendarDbHelper(this,"MeetingsDB",null,1);
    private SQLiteDatabase db;
    private DatePicker datePicker;
    private NumberPicker hourPickerIni, hourPickerEnd;
    private EditText descEditText;
    private Calendar calendar;
    private TextView dateView, timeIniView, timeEndView;
    private int year, month, day;
    private int hourIni, hourEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        // DATABASE START
        db = cdbh.getWritableDatabase();

        hourPickerIni = (NumberPicker) findViewById(R.id.hourPickerIni);
        hourPickerEnd = (NumberPicker) findViewById(R.id.hourPickerEnd);
        //inicialitzam els hourPicker
        hourPickerIni.setMinValue(0);
        hourPickerIni.setMaxValue(23);
        hourPickerEnd.setMaxValue(24);

        descEditText = (EditText) findViewById(R.id.descEditText);
        timeIniView = (TextView) findViewById(R.id.textView2);
        timeEndView = (TextView) findViewById(R.id.textView3);
        dateView = (TextView) findViewById(R.id.textView1);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hourIni = calendar.get(Calendar.HOUR_OF_DAY);
        hourEnd = calendar.get(Calendar.HOUR_OF_DAY);

        hourPickerIni.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                timeIniView.setText("Hora d'Inici : "+Integer.toString(newVal)+":00");
                hourPickerEnd.setMinValue(newVal+1);
                hourPickerEnd.setMaxValue(24);
                hourIni = newVal;
            }
        });

        hourPickerEnd.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                timeEndView.setText("Hora de finalitzacio : "+Integer.toString(newVal)+":00");
                hourEnd = newVal;
            }
        });


        showDate(year, month + 1, day);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
    }


    public void saveNewEvent(View view){
        // Comprovar que totes les dades insertades son correctes
        if(descEditText.getText().length() != 0){
            // Guardar dades a la base de dades
            String desc = descEditText.getText().toString();
            String date = dateView.getText().toString();
            String hIni = timeIniView.getText().toString();
            String hEnd = timeEndView.getText().toString();
            if(db != null){
                try {
                    db.execSQL("INSERT INTO Meetings(day,startTime,endTime,description) VALUES ('"+date+"','"+hourIni+"','"+hourEnd+"','"+desc+"')");
                    db.close();
                    // Tornem a mostrar la setmana
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.d(TAG,"Meeting added");
                } catch (Exception c){
                    Log.e(TAG,"Error adding meeting to DB");
                }

            }
        } else {
            //Falta omplir la descripcio del meeting
        }

    }


}