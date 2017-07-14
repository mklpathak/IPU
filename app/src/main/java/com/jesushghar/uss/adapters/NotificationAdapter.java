package com.jesushghar.uss.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jesushghar.uss.R;
import com.jesushghar.uss.data.DatabaseContract;
import com.jesushghar.uss.pojo.Notice;

import java.util.List;

/**
 * Created by mukul on 22-04-2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static final String TAG = NotificationAdapter.class.getSimpleName();

    private Cursor mCursor;
    Context context;

    final private NotificationAdapterOnClickHandler clickHandler;

    public NotificationAdapter(Context context, NotificationAdapterOnClickHandler clickHandler){
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notification, parent, false);
        itemView.setFocusable(true);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.notice.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION)));
        holder.date.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE)));
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void clear() {
        mCursor = null;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView notice, date;
        public ViewHolder(View itemView) {
            super(itemView);
            notice = (TextView) itemView.findViewById(R.id.notification_list_title);
            date = (TextView) itemView.findViewById(R.id.notification_list_date);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            Notice notice = new Notice(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_TITLE)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_LINK)));
            clickHandler.onClick(notice);
        }


        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            Notice notice = new Notice(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_TITLE)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_PUBLISH_DATE)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_DESCRIPTION)),
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.NotificationTable.COLUMN_LINK)));
            clickHandler.onLongClick(notice);

            return false;
        }
    }

    public static interface NotificationAdapterOnClickHandler {
        void onClick(Notice notice);
        void onLongClick(Notice notice);
    }

}
