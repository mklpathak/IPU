package com.jesushghar.uss.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by mukul on 10-04-2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper  {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "USSApp";

    private static final String TABLE_USER = "user";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_SCHOOL = "school";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                +KEY_ID + " INTEGER PRIMARY KEY, "
                +KEY_NAME + " TEXT, "
                +KEY_EMAIL + " TEXT UNIQUE, "
                +KEY_ROLE + " TEXT UNIQUE, "
                +KEY_SCHOOL + " TEXT UNIQUE, "
                +KEY_UID + " TEXT, "
                +KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "User table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(String name, String email,String role, String school, String uid, String created_at){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_ROLE, role);
        values.put(KEY_SCHOOL, school);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New User inserted in sqlite: " + id);
    }

    public HashMap<String, String > getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String >();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(KEY_NAME, cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put(KEY_EMAIL, cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put(KEY_ROLE,cursor.getString(cursor.getColumnIndex(KEY_ROLE)));
            user.put(KEY_SCHOOL, cursor.getString(cursor.getColumnIndex(KEY_SCHOOL)));
            user.put(KEY_UID, cursor.getString(cursor.getColumnIndex(KEY_UID)));
            user.put(KEY_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
        }

        cursor.close();
        db.close();
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "DELETED all user info from sqlite");
    }
}
