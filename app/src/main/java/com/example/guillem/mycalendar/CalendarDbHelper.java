package com.example.guillem.mycalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guillem on 10/12/15.
 */
public class CalendarDbHelper extends SQLiteOpenHelper {

    String sqlCreateMeetings = "CREATE TABLE Meetings(day TEXT, startTime INTEGER, endTime INTEGER, description TEXT)";
    String sqlCreateTasks = "CREATE TABLE Tasks(startDay TEXT, endDay TEXT, description TEXT, isDone INTEGER, hasMeeting INTEGER, meetingID TEXT)";
    String deleteMeetingsTable = "DROP TABLE IF EXISTS Meetings";
    String deleteTasksTable = "DROP TABLE IF EXISTS Tasks";

    public CalendarDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la Base de Dades
        db.execSQL(sqlCreateMeetings);
        db.execSQL(sqlCreateTasks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualitzar la Base de Dades

        //Eliminim les taules
        db.execSQL(deleteMeetingsTable);
        db.execSQL(deleteTasksTable);

        //Crear la Base de Dades
        db.execSQL(sqlCreateMeetings);
        db.execSQL(sqlCreateTasks);
    }
}
