package com.jesushghar.uss.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jesushghar.uss.R;
import com.jesushghar.uss.activities.HomeActivity;
import com.jesushghar.uss.activities.LoginActivity;
import com.jesushghar.uss.data.DatabaseContract;
import com.jesushghar.uss.helper.XmlParser;
import com.jesushghar.uss.utils.AppConfig;
import com.jesushghar.uss.utils.AppController;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Vector;

/**
 * Created by mukul on 25-04-2016.
 */
public class UssSyncAdapter extends AbstractThreadedSyncAdapter {
    String TAG = UssSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60 * 15; //15 min
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    public UssSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String tag_string_req = "req_notification_xml_data";
        Log.e(TAG, "Peroforming Sync");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                AppConfig.URL_XML_FOR_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        XmlParser parser = new XmlParser();
                        //Log.e(TAG, response.toString());
                        Document document = parser.getDomElement(response);
                        NodeList nodeList = document.getElementsByTagName("item");
                        Log.e(TAG, "Total XML item : " + nodeList.getLength());

                        Vector<ContentValues> contentValuesVector = new Vector<>(nodeList.getLength());
                        for(int i = nodeList.getLength()-1 ; i >= 0; i--) {
                            Element e = (Element) nodeList.item(i);

                            ContentValues value = new ContentValues();
                            value.put(DatabaseContract.NotificationTable.COLUMN_TITLE, parser.getValue(e,"title"));
                            value.put(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION, parser.getValue(e,"description"));
                            value.put(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE, parser.getValue(e, "pubDate").substring(5,16));
                            value.put(DatabaseContract.NotificationTable.COLUMN_LINK, parser.getValue(e, "link"));
                            contentValuesVector.add(value);
                        }

                        if (contentValuesVector.size() > 0) {
                            ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
                            contentValuesVector.toArray(contentValuesArray);
                            int number_of_items = getContext().getContentResolver().bulkInsert(DatabaseContract.NotificationTable.CONTENT_URI, contentValuesArray);

                            notifyNotice(number_of_items);
                        }
                        //Log.e(TAG, "Finished Getting Notification Feed." + contentValuesVector.size());

                        // notificationAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    public static Account getSyncAccount(Context context) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(context.getString(R.string.app_name),
                context.getString(R.string.sync_account_type));

        if (accountManager.getPassword(newAccount) == null) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        // Since we've created an account
        UssSyncAdapter.configurePeriodSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        // Without calling setSyncAutomatically, our periodic sync will not be enabled
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        // Finally, do a sync to get things started
        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void configurePeriodSync(Context context, int syncInterval, int flexTime) {

        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest.Builder builder = new SyncRequest.Builder();
            Bundle extras = new Bundle();
            builder.setExtras(extras);

            SyncRequest request = builder
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }




    //Notification Function

    int NOTIFICATION_ID = 3005;

    private void notifyNotice(int rows) {
        Context context = getContext();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationKey = context.getString(R.string.pref_enable_notification_key);
        boolean displayNotifications = preferences.getBoolean(displayNotificationKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notification_default)));


        if (rows > 0 && displayNotifications) {

            Cursor cursor = context.getContentResolver().query(
                    DatabaseContract.NotificationTable.CONTENT_URI,
                    new String[]{DatabaseContract.NotificationTable.COLUMN_DESCRIPTION, DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE},
                    null,
                    null,
                    DatabaseContract.NotificationTable._ID + " DESC"
            );

            //Log.e(TAG, "Rows in Cursor" + cursor.getCount());

            String title = context.getString(R.string.app_name);

            cursor.moveToFirst();

            String contentText = cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION))
                    + " on " + cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE));

            int i=0;
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(title);
            while(cursor.moveToNext() && i<5 && i < rows) {
                inboxStyle.addLine(cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION))
                        + " on " + cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE)));
                i++;
            }

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ggsipu_logo)
                            .setContentTitle(title)
                            .setContentText(contentText)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setStyle(inboxStyle);

            Intent resultIntent = new Intent(context, HomeActivity.class);
            resultIntent.putExtra("Fragment" , 2);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}
