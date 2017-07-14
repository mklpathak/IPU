package com.jesushghar.uss.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mukul on 25-04-2016.
 */
public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.jesushghar.uss";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTIFICATION = "notification";
    public static final String PATH_BOOKMARK= "bookmark";

    public static final class NotificationTable implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTIFICATION).build();

        //Returns a directory (tables with rows>0)
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" +
                        PATH_NOTIFICATION;

        //Returns an Item (row = 1)
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" +
                        PATH_NOTIFICATION;

        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION= "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_PUBLISH_DATE = "pub_date";

        public static Uri buildNotificationUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

    }

    public static final class BookmarkTable implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARK).build();

        //Returns a directory (tables with rows>0)
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" +
                        PATH_BOOKMARK;

        //Returns an Item (row = 1)
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" +
                        PATH_BOOKMARK;

        public static final String TABLE_NAME = "bookmarks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION= "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_PUBLISH_DATE = "pub_date";

        public static Uri buildNotificationUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

    }
}
