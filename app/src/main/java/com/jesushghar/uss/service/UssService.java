package com.jesushghar.uss.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
 * Created by pat on 25-04-2016.
 */
public class UssService extends IntentService {
    private String TAG = UssService.class.getSimpleName();

    public UssService() {
        super("Uss Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String tag_string_req = "notification_xml_data";
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
                            int number_of_items = getContentResolver().bulkInsert(DatabaseContract.NotificationTable.CONTENT_URI, contentValuesArray);

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

    static public class AlarmReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, UssService.class);
            context.startService(sendIntent);
        }
    }


    int NOTIFICATION_ID = 3005;

    private void notifyNotice(int rows) {
        if (rows > 0) {
            Context context = getApplicationContext();

            Cursor cursor = getContentResolver().query(
                    DatabaseContract.NotificationTable.CONTENT_URI,
                    new String[]{DatabaseContract.NotificationTable.COLUMN_DESCRIPTION, DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE},
                    null,
                    null,
                    DatabaseContract.NotificationTable._ID + " DESC"
            );

            //Log.e(TAG, "Rows in Cursor" + cursor.getCount());

            String title = context.getString(R.string.app_name);

            cursor.moveToFirst();
            int i = 0;
            String contentText = cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION))
                    + " on " + cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE));

            String contentBigText = contentText;

            while(cursor.moveToNext() && i<5 && i < rows) {
                contentBigText += ("\n" + cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION))
                        + " on " + cursor.getString(cursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE)) );
                i++;
            }
            Log.e(TAG, contentText);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ggsipu_logo)
                            .setContentText(contentText)
                            .setContentTitle(title)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contentBigText));

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
