package cz.muni.fi.pv256.uco374366.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.muni.fi.pv256.uco374366.Fragment.FragmentFilmList;
import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Model.FilmGson;
import cz.muni.fi.pv256.uco374366.Model.FilmListGson;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Call;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final int[] groups = intent.getIntArrayExtra("groups");

        Bundle bundle = new Bundle();

        if (groups.length > 0) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {

                ArrayList<Film> films = new ArrayList<>();

                for(int i = 0; i < groups.length; i++) {
                    films.addAll(downloadData(groups[i]));
                }

                /* Sending result back to activity */
                if (null != films && films.size() > 0) {
                    bundle.putParcelableArrayList("films", films);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private ArrayList<Film> downloadData(int group) throws IOException, DownloadException {


        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        NetworkApi api = retrofit.create(NetworkApi.class);
        Call<FilmListGson> call;

        switch(group) {
            case FragmentFilmList.GROUP_MOST_POPULAR:
                call = api.loadMostPopular();
                break;
            case FragmentFilmList.GROUP_IN_THEATERS:
                call = api.loadInTheaters(Url.dateNow(), Url.dateNextMonth());
                break;
            default:
                throw new DownloadException("Unknown film group");
        }




        FilmListGson filmsJson = call.execute().body();

        SimpleDateFormat dateFormater = new SimpleDateFormat(Url.DATE_FORMAT);
        Locale locale = Locale.getDefault();

        ArrayList<Film> films = new ArrayList<>();

        for (FilmGson filmGson : filmsJson.films) {

            String releaseDate = null;
            try {
                releaseDate = SimpleDateFormat
                        .getDateInstance(SimpleDateFormat.LONG, locale)
                        .format(dateFormater.parse(filmGson.release_date));
            }
            catch(Exception e) {}

            films.add(new Film(
                    filmGson.id,
                    group,
                    filmGson.title,
                    filmGson.overview,
                    releaseDate,
                    filmGson.poster_path,
                    filmGson.backdrop_path
            ));

        }
        Logger.log("film_list", films.size() + " films added");

        return films;
    }


    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}