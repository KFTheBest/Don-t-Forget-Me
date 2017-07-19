package com.example.kyle.dfm;

/**
 * Created by Kyle on 4/2/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TaskHelper extends SQLiteOpenHelper{
    public TaskHelper(Context context){
        super(context, Task.databaseName, null, Task.databaseVersion);

    }
    @Override
    public void onCreate(SQLiteDatabase database){
        String createTable = "Create Table " + Task.TaskEntry.item_data + " ( " + Task.TaskEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Task.TaskEntry.item_name + " TEXT NOT NULL);";
        database.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.item_data );
        onCreate(database);

    }
    public void insertEntry(String item, SQLiteDatabase database){
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item", item);
        database.insert("item", null, contentValues);
    }
    public Cursor getData(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor results = database.rawQuery("select * from items where id=" + id+"",null);
        return results;
    }
    public void updateList(Integer id, String item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item", item);
        database.update("items", contentValues, "id = ?", new String [] { Integer.toString(id)});

    }
    public Integer deleteList(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("items", "id = ?", new String[] {Integer.toString(id)});
    }
}
