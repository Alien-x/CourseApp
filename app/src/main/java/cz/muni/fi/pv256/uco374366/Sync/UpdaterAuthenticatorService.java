package cz.muni.fi.pv256.uco374366.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Zdenek Kanovsky on 29. 1. 2016.
 */
public class UpdaterAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private UpdaterAuthenticator mUpdaterAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mUpdaterAuthenticator = new UpdaterAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mUpdaterAuthenticator.getIBinder();
    }
}