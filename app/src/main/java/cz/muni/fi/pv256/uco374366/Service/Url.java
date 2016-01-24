package cz.muni.fi.pv256.uco374366.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.muni.fi.pv256.uco374366.BuildConfig;

public final class Url {
    private static final String API_KEY = "&api_key=" + BuildConfig.tmd_api_key;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String URL_BASE = "http://api.themoviedb.org/3/";
    public static final String URL_PART_MOST_POPULAR = "discover/movie?sort_by=popularity.desc" + API_KEY;
    public static final String URL_PART_IN_THEATERS = "discover/movie?sort_by=popularity.desc" + API_KEY;

    public static final String dateNow() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    public static final String dateNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); //minus number would decrement the days

        return sdf.format(cal.getTime());
    }
}
