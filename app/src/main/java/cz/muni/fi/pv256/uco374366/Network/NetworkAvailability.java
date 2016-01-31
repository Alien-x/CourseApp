package cz.muni.fi.pv256.uco374366.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Zdenek Kanovsky on 25. 1. 2016.
 */
public class NetworkAvailability {

    public static boolean isOnline(final Context context) {
        if(context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        else {
            return false;
        }
    }
}
