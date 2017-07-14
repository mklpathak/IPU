package com.jesushghar.uss.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mukul on 25-04-2016.
 */
public class UssSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static UssSyncAdapter ussSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (ussSyncAdapter == null) {
                ussSyncAdapter = new UssSyncAdapter(getApplicationContext(), true);
            }
        }
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ussSyncAdapter.getSyncAdapterBinder();
    }
}
