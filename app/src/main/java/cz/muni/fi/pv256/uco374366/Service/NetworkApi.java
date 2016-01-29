package cz.muni.fi.pv256.uco374366.Service;

import cz.muni.fi.pv256.uco374366.Model.FilmGson;
import cz.muni.fi.pv256.uco374366.Model.FilmListGson;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Call;

public interface NetworkApi {
    @GET(Url.URL_PART_MOST_POPULAR)
    Call<FilmListGson> loadMostPopular();

    @GET(Url.URL_PART_IN_THEATERS)
    Call<FilmListGson> loadInTheaters(
            @Query("primary_release_date.gte") String thisMonth,
            @Query("primary_release_date.lte") String nextMonth
    );

    @GET(Url.URL_PART_FILM)
    Call<FilmGson> loadFilm(
            @Path("id") long id
    );
}
