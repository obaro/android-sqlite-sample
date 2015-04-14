package com.sample.foo.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by obaro on 02/04/2015.
 */
public class ExampleDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_GENDER = "gender";
    public static final String PERSON_COLUMN_AGE = "age";

    public ExampleDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + PERSON_TABLE_NAME +
                        "(" + PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PERSON_COLUMN_NAME + " TEXT, " +
                        PERSON_COLUMN_GENDER + " TEXT, " +
                        PERSON_COLUMN_AGE + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPerson(String name, String gender, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_GENDER, gender);
        contentValues.put(PERSON_COLUMN_AGE, age);

        db.insert(PERSON_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSON_TABLE_NAME);
        return numRows;
    }

    public boolean updatePerson(Integer id, String name, String gender, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_GENDER, gender);
        contentValues.put(PERSON_COLUMN_AGE, age);
        db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deletePerson(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME,
                PERSON_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getPerson(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME + " WHERE " +
                PERSON_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PERSON_TABLE_NAME, null );
        return res;
    }
}