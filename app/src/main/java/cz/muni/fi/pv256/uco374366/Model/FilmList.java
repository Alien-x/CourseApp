package cz.muni.fi.pv256.uco374366.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zdenek Kanovsky on 18. 10. 2015.
 */
public class FilmList {

    @SerializedName("results")
    public List<Film> films;
}
