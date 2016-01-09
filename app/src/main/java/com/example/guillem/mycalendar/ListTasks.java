package com.example.guillem.mycalendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListTasks extends Activity {
    private static final String TAG = "ListTasks";
    private ListView lv;
    private CalendarDbHelper cdbh = new CalendarDbHelper(this,"MeetingsDB",null,1);
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks);
        lv = (ListView)findViewById(R.id.listTasksView);

        // DATABASE START
        db = cdbh.getReadableDatabase();

        fillListView();
    }

    private void fillListView() {
        String query = "SELECT description FROM Tasks";
        ArrayList<String> tasks = new ArrayList<>();

        if(db != null){
            Log.d(TAG, "Busquem totes les Tasks de la BD");
            Cursor c = db.query(false, "Tasks", new String[]{"description"}, null, null, null, null, null, null);
            if(c.moveToFirst()){
                do {
                    String desc = c.getString(c.getColumnIndexOrThrow("description"));
                    Log.d(TAG, "Description  = " + desc);
                    String description = c.getString(c.getColumnIndexOrThrow("description"));
                    tasks.add(description);
                } while (c.moveToNext());
                lv.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_item, tasks));
            }
        }else {
            //error al conectar amb la bd

        }

    }

}
