package com.example.guillem.mycalendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListTasks extends Activity {
    private static final String TAG = "ListTasks";
    private ListView lv,lv2;
    private CalendarDbHelper cdbh = new CalendarDbHelper(this,"MeetingsDB",null,1);
    private SQLiteDatabase db;
    private Map<String,String> maper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks);
        lv = (ListView)findViewById(R.id.listTasksView);
        lv2 = (ListView)findViewById(R.id.listViewDone);

        //MAPER
        maper = new HashMap<>();

        // DATABASE START
        db = cdbh.getReadableDatabase();

        fillListView();
        fillListView2();

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lv2.getItemAtPosition(position);
                String desc = listItem.toString();
                String toSelect = maper.get(desc);
                db.execSQL("UPDATE Tasks SET isDone=0 WHERE description='" + toSelect + "' ");
                fillListView();
                fillListView2();

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lv.getItemAtPosition(position);
                String desc = listItem.toString();
                String toSelect = maper.get(desc);
                db.execSQL("UPDATE Tasks SET isDone=1 WHERE description='"+toSelect+"' ");
                fillListView();
                fillListView2();

            }
        });
    }

    private void fillListView2() {
        String query = "SELECT description FROM Tasks";
        ArrayList<String> tasks = new ArrayList<>();

        if(db != null){
            Log.d(TAG, "Busquem totes les Tasks de la BD");
            Cursor c = db.query(false, "Tasks", new String[]{"description","startDay","endDay","isDone","hasMeeting","meetingID"}, null, null, null, null, null, null);
            if(c.moveToFirst()){
                do {
                    String desc = c.getString(c.getColumnIndexOrThrow("description"));
                    String key = desc;
                    String startDay = c.getString(c.getColumnIndexOrThrow("startDay"));
                    String endDay = c.getString(c.getColumnIndexOrThrow("endDay"));
                    if(endDay == null || endDay.equals("null")){
                        desc = desc + " ("+startDay+")";
                    }else{
                        desc = desc + " ("+startDay+" - "+endDay+")";
                    }
                    int teRelacio = c.getInt(c.getColumnIndexOrThrow("hasMeeting"));
                    if(teRelacio == 1){
                        String meeting = c.getString(c.getColumnIndexOrThrow("meetingID"));
                        desc = desc + "\nReunió: \n     - " + meeting;
                    }
                    Log.d(TAG, "Description  = " + desc);
                    maper.put(desc,key);
                    int isDone = c.getInt(c.getColumnIndexOrThrow("isDone"));
                    if(isDone == 1){
                        tasks.add(desc);
                    }

                } while (c.moveToNext());
                lv2.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_item, tasks));
            }
        }else {
            //error al conectar amb la bd

        }

    }

    private void fillListView() {
        String query = "SELECT description FROM Tasks";
        ArrayList<String> tasks = new ArrayList<>();

        if(db != null){
            Log.d(TAG, "Busquem totes les Tasks de la BD");
            Cursor c = db.query(false, "Tasks", new String[]{"description","startDay","endDay","isDone","hasMeeting","meetingID"}, null, null, null, null, null, null);
            if(c.moveToFirst()){
                do {
                    String desc = c.getString(c.getColumnIndexOrThrow("description"));
                    String key = desc;
                    String startDay = c.getString(c.getColumnIndexOrThrow("startDay"));
                    String endDay = c.getString(c.getColumnIndexOrThrow("endDay"));
                    if(endDay == null || endDay.equals("null")){
                        desc = desc + " ("+startDay+")";
                    }else{
                        desc = desc + " ("+startDay+" - "+endDay+")";
                    }
                    int teRelacio = c.getInt(c.getColumnIndexOrThrow("hasMeeting"));
                    if(teRelacio == 1){
                        String meeting = c.getString(c.getColumnIndexOrThrow("meetingID"));
                        desc = desc + "\nReunió: \n     - " + meeting;
                    }
                    Log.d(TAG, "Description  = " + desc);
                    maper.put(desc,key);
                    int isDone = c.getInt(c.getColumnIndexOrThrow("isDone"));
                    if(isDone == 0){
                        tasks.add(desc);
                    }

                } while (c.moveToNext());
                lv.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_item, tasks));
            }
        }else {
            //error al conectar amb la bd

        }

    }

}
