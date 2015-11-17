package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class FragmentFilmDetail extends Fragment{

    private static final String TMD_POSTER_URL = "http://image.tmdb.org/t/p/w150/";
    private static final String TMD_BACKDROP_URL = "http://image.tmdb.org/t/p/w500/";

    private View mView = null;
    private Film mFilm = null;

    public void setFilm(Film film) {
        mFilm = film;
    }

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public  ImageLoadedCallback(ProgressBar progBar){
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    public void refreshLayout() {

        Logger.log("film detail", "refreshLayout");

        // set film info
        if (mView != null && mFilm != null) {
            TextView textViewTitle = (TextView) mView.findViewById(R.id.title);
            textViewTitle.setText(mFilm.getTitle());

            TextView textViewReleaseDate = (TextView) mView.findViewById(R.id.releaseDate);
            textViewReleaseDate.setText(mFilm.getReleaseDay());

            ImageView imageViewPoster = (ImageView) mView.findViewById(R.id.poster);
            Picasso
                .with(getActivity().getApplicationContext())
                    .load(TMD_POSTER_URL + mFilm.getPosterPath())
                .into(imageViewPoster);



            ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            ImageView imageViewBackdrop = (ImageView) mView.findViewById(R.id.backdrop);
            Picasso
                    .with(getActivity().getApplicationContext())
                    .load(TMD_BACKDROP_URL + mFilm.getBackdropPath())
                    .into(imageViewBackdrop, new ImageLoadedCallback(progressBar) {
                        @Override
                        public void onSuccess() {
                            if (this.progressBar != null) {
                                this.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

        } else {
            Logger.log("film detail", "could not refresh");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.film_detail_fragment, container, false);

        refreshLayout();

        return mView;
    }

}