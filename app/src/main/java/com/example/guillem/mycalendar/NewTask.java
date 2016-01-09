package com.example.guillem.mycalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class NewTask extends Activity {
    private static final String TAG = "NewTask";
    private EditText descEditText;
    private TextView textView8, textView9;
    private CheckBox cb, cb2;
    private Button bs, ba, addButton;
    private ListView lv;
    private Task task;
    private String dataIni, dataEnd, meeting;
    int day,month,year;

    private CalendarDbHelper cdbh = new CalendarDbHelper(this,"MeetingsDB",null,1);
    private SQLiteDatabase db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        descEditText = (EditText) findViewById(R.id.descEditText);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        Calendar calendar = Calendar.getInstance();

        lv = (ListView)findViewById(R.id.meetingsListView);
        lv.setEnabled(false);

        bs = (Button) findViewById(R.id.bs);
        ba = (Button) findViewById(R.id.ba);
        addButton = (Button) findViewById(R.id.addButton3);

        ba.setEnabled(false);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        cb = (CheckBox) findViewById(R.id.checkBox);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        meeting = "***";

        // DATABASE START
        db = cdbh.getReadableDatabase();
        //LLegim els meetings que tenim a la base de dades;
        fillListView();

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ba.isEnabled()) {
                    ba.setEnabled(false);
                } else {
                    ba.setEnabled(true);
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lv.isEnabled()) {
                    lv.setEnabled(false);
                } else {
                    lv.setEnabled(true);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lv.getItemAtPosition(position);
                String desc = listItem.toString();
                TextView ms = (TextView) findViewById(R.id.meetingSelected);
                meeting = desc;
                ms.setText("Te relació amb la seguent reunió: " + desc);
            }
        });

    }

    private void fillListView() {
        String query = "SELECT description FROM Meetings";
        ArrayList<String> meetings = new ArrayList<>();

        if(db != null){
            Log.d(TAG, "Busquem tots els meetings de la BD");
            Cursor c = db.query(false, "Meetings", new String[]{"description"}, null, null, null, null, null, null);
            if(c.moveToFirst()){
                do {
                    String desc = c.getString(c.getColumnIndexOrThrow("description"));
                    Log.d(TAG, "Description  = " + desc);
                    String description = c.getString(c.getColumnIndexOrThrow("description"));
                    meetings.add(description);
                } while (c.moveToNext());
                lv.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_item, meetings));
            }
        }else {
            //error al conectar amb la bd

        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        } else if (id == 2)
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate1(arg1, arg2 + 1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate2(arg1, arg2 + 1, arg3);
        }
    };

    public void onClickDataInici(View view){
        showDialog(1);
    }

    public void onClickDataFi(View view){
        showDialog(2);
    }

    public void showDate1(int year, int month, int day) {
        dataIni = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
        textView8.setText("Data inici de la tasca: " + Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year));
    }

    public void showDate2(int year, int month, int day) {
        dataEnd = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
        textView9.setText("Data fi de la tasca: "+Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
    }

    public void addNewTask(View view) {
        task = new Task();
        String des = descEditText.getText().toString();
        if(des.equals("")){
            Toast.makeText(getApplicationContext(), "Afegeix una descripció de la tasca", Toast.LENGTH_LONG).show();
        } else {
            task.setDescripcio(des);
            if(dataIni == null){
                Toast.makeText(getApplicationContext(), "Introdueix una data d'inici", Toast.LENGTH_LONG).show();
            }else{
                task.setDataIni(dataIni);
                if(cb.isChecked()) {
                    if(dataEnd == null){
                        Toast.makeText(getApplicationContext(), "Introdueix una data de fi", Toast.LENGTH_LONG).show();
                    }else{
                        task.setDataFi(dataEnd);
                        if(cb2.isChecked()){
                            if(meeting.equals("***")){
                                Toast.makeText(getApplicationContext(), "Prem una reunio de la llista per establir una relació", Toast.LENGTH_LONG).show();
                            } else {
                                task.setTeRelacio(1);
                                task.setMeeting(meeting);
                                try {
                                    String query = "INSERT INTO Tasks (startDay,endDay, description, isDone, hasMeeting, meetingID) VALUES ('"+task.getDataIni()+"','"+task.getDataFi()+"','"+task.getDescripcio()+"',0,0,'"+task.getMeeting()+"')";
                                    Log.d(TAG,query);
                                    db.execSQL(query);
                                    db.close();
                                    // Tornem a mostrar la setmana
                                    Toast.makeText(getApplicationContext(), "La tasca s'ha afegit correctament", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(this, MainActivity.class);
                                    startActivity(intent);
                                    Log.d(TAG,"Task added");
                                } catch (Exception e){
                                    Log.e(TAG,e.toString());
                                }
                            }
                        }else{
                            task.setTeRelacio(0);
                            task.setMeeting("-");
                            if(db != null){


                            }
                        }
                    }

                } else {
                    if(cb2.isChecked()){
                        if(meeting.equals("***")){
                            Toast.makeText(getApplicationContext(), "Prem una reunio de la llista per establir una relació", Toast.LENGTH_LONG).show();
                        } else {
                            task.setTeRelacio(1);
                            task.setMeeting(meeting);
                            try {
                                String query = "INSERT INTO Tasks (startDay,endDay, description, isDone, hasMeeting, meetingID) VALUES ('"+task.getDataIni()+"','"+task.getDataFi()+"','"+task.getDescripcio()+"',0,"+task.getTeRelacio()+",'"+task.getMeeting()+"')";
                                Log.d(TAG,query);
                                db.execSQL(query);
                                db.close();
                                // Tornem a mostrar la setmana
                                Toast.makeText(getApplicationContext(), "La tasca s'ha afegit correctament", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                Log.d(TAG,"Task added");
                            } catch (Exception e){
                                Log.e(TAG,e.toString());
                            }
                        }
                    }

                }
            }
        }
    }
}
