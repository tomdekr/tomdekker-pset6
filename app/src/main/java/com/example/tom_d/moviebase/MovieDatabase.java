package com.example.tom_d.moviebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tom_D on 11/30/2017.
 */

public class MovieDatabase extends SQLiteOpenHelper {
    private static MovieDatabase instance;
    private static String DBName = "todos";
    private static int version  = 1;
    private Context applicationContext;

    private MovieDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MovieDatabase(context.getApplicationContext(), DBName, null, version);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "todos");
        onCreate(sqLiteDatabase);
    }

    public Cursor selectAll() {
        SQLiteDatabase dataBase = getReadableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM todos", null);
        return cursor;
    }

    public void insert(String name, String price) {
        SQLiteDatabase dataBase = getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = dataBase.rawQuery("SELECT * FROM todos WHERE name = '"+ name+ "'",null
        );
        Log.d("cursor", "count: "+ cursor.getCount());
        if(cursor.getCount()>0){
            cursor.moveToFirst();


            Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
            values.put("name", name);

            Log.d("cursor", "id: "+ cursor.getString(cursor.getColumnIndex("_id")));
            Log.d("cursor", "id: "+ cursor.getString(cursor.getColumnIndex("name")));


            dataBase.update("todos", values,"_id="+id, null);
        }
        else{
            values.put("name", name);

            dataBase.insert("todos",null,values);
        }
        cursor.close();

    }
    public void deleteAll(){
        SQLiteDatabase dataBase = getWritableDatabase();
        dataBase.delete("todos",null,null);
    }

}