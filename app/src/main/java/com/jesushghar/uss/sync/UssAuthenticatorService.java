package com.jesushghar.uss.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mukul on 25-04-2016.
 */
public class UssAuthenticatorService extends Service {
    private UssAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new UssAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
