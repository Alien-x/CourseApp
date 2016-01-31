package cz.muni.fi.pv256.uco374366.Network;

import cz.muni.fi.pv256.uco374366.Model.Film;
import cz.muni.fi.pv256.uco374366.Model.FilmList;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Call;

/**
 * Created by Zdenek Kanovsky on 25. 1. 2016.
 */
public interface NetworkApi {
    @GET(Url.URL_PART_MOST_POPULAR)
    Call<FilmList> loadMostPopular();

    @GET(Url.URL_PART_IN_THEATERS)
    Call<FilmList> loadInTheaters(
            @Query("primary_release_date.gte") String thisMonth,
            @Query("primary_release_date.lte") String nextMonth
    );

    @GET(Url.URL_PART_FILM)
    Call<Film> loadFilm(
            @Path("id") long id
    );
}
