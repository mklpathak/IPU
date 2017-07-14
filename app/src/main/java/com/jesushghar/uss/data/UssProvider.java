package com.jesushghar.uss.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mukul on 25-04-2016.
 */
public class UssProvider extends ContentProvider {
    String TAG = UssProvider.class.getSimpleName();
    public static final int NOTIFICATION = 100;
    public static final int BOOKMARK = 300;

    private DbHelper mOpenHelper;


    public static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return false;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DatabaseContract.PATH_NOTIFICATION, NOTIFICATION);
        matcher.addURI(authority, DatabaseContract.PATH_BOOKMARK, BOOKMARK);

        return matcher;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATION:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.NotificationTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case BOOKMARK:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.BookmarkTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri : " +uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTIFICATION:
                return DatabaseContract.NotificationTable.CONTENT_TYPE;
            case BOOKMARK:
                return DatabaseContract.NotificationTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTIFICATION: {
                long _id = db.insert(DatabaseContract.NotificationTable.TABLE_NAME,
                        null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.NotificationTable.buildNotificationUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row info " + uri);
                }
                break;
            }

            case BOOKMARK: {
                long _id = db.insert(DatabaseContract.BookmarkTable.TABLE_NAME,
                        null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.BookmarkTable.buildNotificationUri(_id);
                } else {
                    //throw new android.database.SQLException("Failed to insert row info " + uri);
                    Toast.makeText(getContext(), "Row Already Exist", Toast.LENGTH_SHORT).show();
                    return  null;
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case NOTIFICATION:
                rowsDeleted = db.delete(DatabaseContract.NotificationTable.TABLE_NAME, selection, selectionArgs );
                break;
            case BOOKMARK:
                rowsDeleted = db.delete(DatabaseContract.BookmarkTable.TABLE_NAME, selection, selectionArgs );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (null == selection || 0 != rowsDeleted) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NOTIFICATION:
                rowsUpdated = db.update(DatabaseContract.NotificationTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case BOOKMARK:
                rowsUpdated = db.update(DatabaseContract.BookmarkTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        if (0 != rowsUpdated)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTIFICATION:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        long _id = db.insert(DatabaseContract.NotificationTable.TABLE_NAME, null, value);
                        if (-1 != _id)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                Log.e(TAG, "INSERTS LIKE : " + returnCount);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
