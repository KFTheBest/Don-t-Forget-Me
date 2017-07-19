package com.example.kyle.dfm;

/**
 * Created by Kyle on 4/2/2017.
 */

import android.provider.BaseColumns;


public class Task {
    public static final String  databaseName = " com.example.com.kyle.dfm";
    public static final int databaseVersion = 1;

    public class TaskEntry implements BaseColumns{
       public static final String item_data = "data";
        public static final String item_name = "name";
        public static final String item_id = "id";
    }


}
