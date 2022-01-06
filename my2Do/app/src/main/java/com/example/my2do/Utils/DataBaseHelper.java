package com.example.my2do.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.my2do.Model.ToDoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 4;
    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_TODO = "TODO_TABLE";
    private static  final String COL_ID = "ID";
    private static  final String COL_TASK_TITLE = "TASK_TITLE";
    private static  final String COL_TASK_DESCRIPTION = "TASK_DESCRIPTION";
    private static  final String COL_TASK_CATEGORY = "TASK_CATEGORY";
    private static  final String COL_TASK_DATE = "TASK_DATE";
    private static  final String COL_TASK_STATUS = "TASK_STATUS";


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TASK_TITLE + " TEXT,"
                + COL_TASK_CATEGORY + " TEXT,"
                + COL_TASK_DATE + " TEXT,"
                + COL_TASK_STATUS + " INTEGER,"
                + COL_TASK_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    public void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
//        String taskDateStr = db.execSQL("SELECT datetime"+"(" + "now"+ ")");

        ContentValues values = new ContentValues();
        values.put(COL_TASK_TITLE , model.getTaskTitle());
        values.put(COL_TASK_DESCRIPTION , model.getTaskDescription());
        if(!model.getTaskCategory().equals("Task Category...")){
            values.put(COL_TASK_CATEGORY , model.getTaskCategory());
        }
        values.put(COL_TASK_DATE , model.getTaskDate());
        values.put(COL_TASK_STATUS , model.getTaskStatus());
        db.insert(TABLE_TODO , null , values);
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_TODO , "ID=?" , new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateTask(int id , String taskTitle, String taskDate, String taskDescription, String taskCategory, int taskStatus){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_TITLE , taskTitle);
        values.put(COL_TASK_DESCRIPTION , taskDescription);
        if(!taskCategory.equals("Task Category...")){
            values.put(COL_TASK_CATEGORY , taskCategory);
        }
        values.put(COL_TASK_DATE , taskDate);
        values.put(COL_TASK_STATUS , taskStatus);
        db.update(TABLE_TODO , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<ToDoModel> getAllTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_TODO , null , null , null , null , null , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
//                        String taskDateStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DATE));
//                        Date taskDate = null;
//                        try {
//                            taskDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskDateStr);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }

                        ToDoModel task = new ToDoModel();
                        task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                        task.setTaskStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_STATUS)));
                        task.setTaskTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_TITLE)));
                        task.setTaskDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DESCRIPTION)));
                        task.setTaskCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_CATEGORY)));
                        task.setTaskDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DATE)));
//                        Log.i("Format", cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DATE)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
