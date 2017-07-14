package com.jesushghar.uss.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jesushghar.uss.data.DatabaseContract.NotificationTable;
import com.jesushghar.uss.data.DatabaseContract.BookmarkTable;

/**
 * Created by mukul on 25-04-2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "uss.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTIFICATION_TABLE =
                "CREATE TABLE " + NotificationTable.TABLE_NAME + " (" +
                        NotificationTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NotificationTable.COLUMN_TITLE + " TEXT, " +
                        NotificationTable.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        NotificationTable.COLUMN_PUBLISH_DATE + " TEXT NOT NULL, " +
                        NotificationTable.COLUMN_LINK + " TEXT, " +
                        "UNIQUE (" +
                        NotificationTable.COLUMN_TITLE + ", " +
                        NotificationTable.COLUMN_DESCRIPTION + ", " +
                        NotificationTable.COLUMN_PUBLISH_DATE + ", " +
                        NotificationTable.COLUMN_LINK + ") ON CONFLICT IGNORE );";

        final String SQL_CREATE_BOOKMARKS_TABLE =
                "CREATE TABLE " + BookmarkTable.TABLE_NAME + " (" +
                        BookmarkTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BookmarkTable.COLUMN_TITLE + " TEXT, " +
                        BookmarkTable.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        BookmarkTable.COLUMN_PUBLISH_DATE + " TEXT NOT NULL, " +
                        BookmarkTable.COLUMN_LINK + " TEXT, " +
                        "UNIQUE (" +
                        BookmarkTable.COLUMN_TITLE + ", " +
                        BookmarkTable.COLUMN_DESCRIPTION + ", " +
                        BookmarkTable.COLUMN_PUBLISH_DATE + ", " +
                        BookmarkTable.COLUMN_LINK + ") ON CONFLICT IGNORE );";


        db.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
        db.execSQL(SQL_CREATE_BOOKMARKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationTable.TABLE_NAME);
        onCreate(db);
    }
}
