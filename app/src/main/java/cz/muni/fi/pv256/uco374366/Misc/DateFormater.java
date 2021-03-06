package cz.muni.fi.pv256.uco374366.Misc;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.muni.fi.pv256.uco374366.Network.Url;

/**
 * Created by Zdenek Kanovsky on 05. 11. 2015.
 */
public class DateFormater {

    public static String toLocalFormat(String date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(Url.DATE_FORMAT);
        Locale locale = Locale.getDefault();

        String localeDate;
        try {
            localeDate = SimpleDateFormat
                    .getDateInstance(SimpleDateFormat.LONG, locale)
                    .format(dateFormater.parse(date));
        }
        catch(Exception e) {
            localeDate = date;
        }

        return localeDate;
    }
}
