package cz.muni.fi.pv256.uco374366;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import java.util.List;

import cz.muni.fi.pv256.uco374366.Model.Film;

/**
 * Created by Zdenek Kanovsky on 18. 10. 2015.
 */
public class App extends Application {

    public List<Film> downloadedFilms = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            initStrictMode();
        }
    }

    private void initStrictMode() {
        StrictMode.ThreadPolicy.Builder tpb = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tpb.penaltyFlashScreen();
        }
        StrictMode.setThreadPolicy(tpb.build());

        StrictMode.VmPolicy.Builder vmpb = new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            vmpb.detectLeakedClosableObjects();
        }
        StrictMode.setVmPolicy(vmpb.build());
    }
}
