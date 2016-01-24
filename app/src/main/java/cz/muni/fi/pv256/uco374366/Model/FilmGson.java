package cz.muni.fi.pv256.uco374366.Model;


import com.google.gson.annotations.SerializedName;

public class FilmGson {

    @SerializedName("id")
    public int id;

    @SerializedName("original_title")
    public String title;

    @SerializedName("overview")
    public String overview;

    @SerializedName("release_date")
    public String release_date;

    @SerializedName("poster_path")
    public String poster_path;

    @SerializedName("backdrop_path")
    public String backdrop_path;
}
