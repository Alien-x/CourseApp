package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.support.v4.util.LongSparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.muni.fi.pv256.uco374366.Model.Film;
import cz.muni.fi.pv256.uco374366.Model.FilmJson;
import cz.muni.fi.pv256.uco374366.Model.ResultJson;

public class FragmentFilmList extends Fragment {

    private FragmentFilmDetail mFragmentFilmDetail = null;
    private List<Film> mFilms;

    private LongSparseArray<String> mHeaders = new LongSparseArray<>();
    private static final int GROUP_MOST_POPULAR = 1;
    private static final int GROUP_IN_THEATERS = 2;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TMD_API_KEY = "&api_key=" + BuildConfig.tmd_api_key;
    private static final String TMD_BASE_URL = "http://api.themoviedb.org/3/";
    private static final String URL_MOST_POPULAR = TMD_BASE_URL + "discover/movie?sort_by=popularity.desc" + TMD_API_KEY;
    private static final String URL_IN_THEATERS = TMD_BASE_URL + "discover/movie?primary_release_date.gte=" + dateNow() + "&primary_release_date.lte=" + dateNextMonth() + TMD_API_KEY;

    private static final String dateNow() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    private static final String dateNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); //minus number would decrement the days

        return sdf.format(cal.getTime());
    }

    private FilmAdapter mFilmAdapter = null;
    private StickyGridHeadersGridView mGridview = null;
    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.film_list_fragment, container, false);

        Logger.log("FragmentFilmList", "onCreateView");

        mHeaders.append(GROUP_MOST_POPULAR, getActivity().getResources().getString(R.string.film_group_most_popular));
        mHeaders.append(GROUP_IN_THEATERS, getActivity().getResources().getString(R.string.film_group_in_theaters));

        mFilmAdapter = new FilmAdapter(getActivity().getApplicationContext(), mFilms, mHeaders);

        mGridview = (StickyGridHeadersGridView) mView.findViewById(R.id.gridViewFilms);
        mGridview.setAdapter(mFilmAdapter);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.log("film_list", "ItemClick");

                // tablet
                if(mFragmentFilmDetail != null) {
                    Logger.log("film_list", "set fragment detail");
                    mFragmentFilmDetail.setFilm(mFilms.get(position));
                    mFragmentFilmDetail.refreshLayout();
                }
                // mobile
                else {
                    Logger.log("film_list", "new activity detail");
                    Intent intent = new Intent(getActivity(), FilmDetailFragmentActivity.class);
                    intent.putExtra("FILM", mFilms.get(position));
                    startActivity(intent);
                }

            }
        });

        mGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), mFilms.get(position).getTitle() + " ("+mFilms.get(position).getGroup()+")",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if(isOnline()) {
            mGridview.setEmptyView(mView.findViewById(R.id.loading));
        }
        else {
            mGridview.setEmptyView(mView.findViewById(R.id.noConnectionView));
        }

        return mView;
    }

    public void loadFilms(List<Film> films) {

        mFilms = films;

        getSpecificFilms(URL_MOST_POPULAR, GROUP_MOST_POPULAR);
        getSpecificFilms(URL_IN_THEATERS, GROUP_IN_THEATERS);
    }

    class FilmCallback implements Callback {

        private int mFilmGroup;

        public FilmCallback(int filmGroup) {
            mFilmGroup = filmGroup;
        }


        @Override
        public void onFailure(Request request, IOException e) {
            Logger.log("film_http", "onFailure: " + e);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {

                Logger.log("film_http", "onResponse success");

                Gson gson = new GsonBuilder().create();
                ResultJson results = gson.fromJson(response.body().string(), ResultJson.class);



                SimpleDateFormat formater = new SimpleDateFormat(DATE_FORMAT);
                Locale locale = Locale.getDefault();

                for (FilmJson filmJson : results.films) {

                    String releaseDate = null;
                    try {
                        releaseDate = SimpleDateFormat
                                .getDateInstance(SimpleDateFormat.LONG, locale)
                                .format(formater.parse(filmJson.release_date));
                    }
                    catch(Exception e) {}

                    mFilms.add(new Film(
                            filmJson.id,
                            mFilmGroup,
                            filmJson.title,
                            filmJson.overview,
                            releaseDate,
                            filmJson.poster_path,
                            filmJson.backdrop_path
                    ));

                }
                Logger.log("film_list", mFilms.size() + " films added");
                //mFilmAdapter.notifyDataSetChanged();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mGridview != null) {
                            mGridview.setEmptyView(mView.findViewById(R.id.loading));
                        }
                        mFilmAdapter.notifyDataSetChanged();

                        // tablet
                        if (mFragmentFilmDetail != null) {
                            Logger.log("film_list", "setting fragment detail (tablet)");
                            mFragmentFilmDetail.setFilm(mFilms.get(0));
                            mFragmentFilmDetail.refreshLayout();
                        }
                    }
                });

                // error http
            } else {
                Logger.log("film_http", "onResponse error: " + response);
            }


        }
    }


    private void getSpecificFilms(String url, int group) {

        if(isOnline()) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new FilmCallback(group));
        }
    }

    public void setFragmentFilmDetail(FragmentFilmDetail fragmentFilmDetail) {
        mFragmentFilmDetail = fragmentFilmDetail;
    }



    private boolean isOnline() {
        /*ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();*/
        return true;
    }
}