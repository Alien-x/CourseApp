package cz.muni.fi.pv256.uco374366.Service;

import cz.muni.fi.pv256.uco374366.Model.FilmListJson;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.Call;

public interface NetworkApi {
    @GET(Url.URL_PART_MOST_POPULAR)
    Call<FilmListJson> loadMostPopular();

    @GET(Url.URL_PART_IN_THEATERS)
    Call<FilmListJson> loadInTheaters(
            @Query("primary_release_date.gte") String thisMonth,
            @Query("primary_release_date.lte") String nextMonth
    );
}
