package cz.muni.fi.pv256.uco374366.Misc;

import android.util.Log;

import cz.muni.fi.pv256.uco374366.BuildConfig;

/**
 * Created by Zdenek Kanovsky on 7. 11. 2015.
 */
public class Logger {

    public static final void log(String tag, String msg) {
        if(BuildConfig.logging) {
            Log.i("logger:"+tag, msg);
        }
    }

}
