package cz.muni.fi.pv256.uco374366;

import android.util.Log;

/**
 * Created by Z on 7. 11. 2015.
 */
public class Logger {

    public static final void log(String tag, String msg) {
        if(BuildConfig.logging) {
            Log.i(tag, msg);
        }
    }

}
