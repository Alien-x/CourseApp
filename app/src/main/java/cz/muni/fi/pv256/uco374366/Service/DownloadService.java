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
import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Fragment.FragmentFilmList;
import cz.muni.fi.pv256.uco374366.Model.FilmList;
import cz.muni.fi.pv256.uco374366.Network.NetworkApi;
import cz.muni.fi.pv256.uco374366.Network.Url;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Call;

import cz.muni.fi.pv256.uco374366.Model.Film;

/**
 * Created by Zdenek Kanovsky on 29. 1. 2016.
 */
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
                    films.addAll(downloadFilms(groups[i]));
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

    private List<Film> downloadFilms(int group) throws IOException, DownloadException {


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
        Call<FilmList> call;

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


        List<Film> films = call.execute().body().films;
        for(Film film : films) {
            film.setGroup(group);
        }
        return films;
    }

    public static Film downloadFilm(Film film) throws IOException, DownloadException {

        if(film == null) {
            throw new NullPointerException("film cannot be null");
        }

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

        //Logger.log("sync_adapter down", "url = " + Url.URL_BASE + Url.URL_PART_FILM);

        NetworkApi api = retrofit.create(NetworkApi.class);
        Call<Film> call;
        call = api.loadFilm(film.getID());

        film = call.execute().body();
        film.setGroup(0);
        return film;
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