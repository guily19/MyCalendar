package com.example.guillem.mycalendar;

import android.provider.BaseColumns;

/**
 * Created by guillem on 10/12/15.
 */
public class CalendarDataSource {
    public static final String MEETINGS_TABLE_NAME = "Meetings";
    public static final String TASKS_TABLE_NAME = "Tasks";
    public static final String STRING_TYPE = "text";
    public static final String DATE_TYPE = "text";

    public static class MeetingsColumns{
        public static final String ID_MEETINGS = BaseColumns._ID;
        public static final String DATE_MEETINGS = "date";
        public static final String DESCRIPTION_MEETINGS = "description";
        public static final String START_HOUR_MEETINGS = "start_hour";
        public static final String END_HOUR_MEETINGS = "end_hour";
    }

    public static class TasksColumns{
        public static final String ID_TASKS = BaseColumns._ID;
        public static final String DESCRIPTION_TASKS = "description";
        public static final String START_DATE_TASK = "start_day";
        public static final String END_DATE_TASK = "end_day";
        public static final String ID_MEETING = "id_meeting";
    }
}
