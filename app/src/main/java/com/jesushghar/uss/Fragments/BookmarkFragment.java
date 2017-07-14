package com.jesushghar.uss.Fragments;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jesushghar.uss.R;
import com.jesushghar.uss.adapters.NotificationAdapter;
import com.jesushghar.uss.data.DatabaseContract;
import com.jesushghar.uss.pojo.Notice;
import com.jesushghar.uss.sync.UssSyncAdapter;
import com.jesushghar.uss.utils.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private static final int BOOKMARK_LOADER = 0;

    private static final String[] NOTICE_COLUMNS = {
            DatabaseContract.BookmarkTable.COLUMN_TITLE,
            DatabaseContract.BookmarkTable.COLUMN_DESCRIPTION,
            DatabaseContract.BookmarkTable.COLUMN_PUBLISH_DATE,
            DatabaseContract.BookmarkTable.COLUMN_LINK
    };

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    SwipeRefreshLayout swipeContainer;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BOOKMARK_LOADER, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateNotificationData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDarker));

        notificationAdapter = new NotificationAdapter(getActivity(), new NotificationAdapter.NotificationAdapterOnClickHandler() {
            @Override
            public void onClick(Notice notice) {
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getLink()));
                startActivity(link_intent);
            }

            @Override
            public void onLongClick(final Notice notice) {
                new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                        .setTitle("Bookmark")
                        .setMessage("Remove this link?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues value = new ContentValues();
                                value.put(DatabaseContract.BookmarkTable.COLUMN_TITLE, notice.getTitle());
                                value.put(DatabaseContract.BookmarkTable.COLUMN_DESCRIPTION, notice.getDescription());
                                value.put(DatabaseContract.BookmarkTable.COLUMN_PUBLISH_DATE, notice.getPubDate());
                                value.put(DatabaseContract.BookmarkTable.COLUMN_LINK, notice.getLink());
                               // getActivity().getContentResolver().insert(DatabaseContract.BookmarkTable.CONTENT_URI, value);
                                getActivity().getContentResolver().delete(DatabaseContract.BookmarkTable.CONTENT_URI,
                                        DatabaseContract.BookmarkTable.COLUMN_LINK + " = ?",
                                        new String[] {notice.getLink()});
                                Toast.makeText(getActivity(), "Link Removed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(notificationAdapter);
        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                DatabaseContract.BookmarkTable.CONTENT_URI,
                NOTICE_COLUMNS,
                null,
                null,
                DatabaseContract.NotificationTable._ID + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "Total rows in Cursor: " + data.getCount());
        notificationAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notificationAdapter.swapCursor(null);
    }

}
