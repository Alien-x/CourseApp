package cz.muni.fi.pv256.uco374366.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cz.muni.fi.pv256.uco374366.Database.FilmDatabase;
import cz.muni.fi.pv256.uco374366.Misc.DateFormater;
import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Model.Film;
import cz.muni.fi.pv256.uco374366.R;
import cz.muni.fi.pv256.uco374366.Network.Url;

/**
 * Created by Zdenek Kanovsky on 10. 18. 2015.
 */
public class FragmentFilmDetail extends Fragment{


    private View mView = null;
    private Film mFilm = null;
    private FilmDatabase mDatabase = null;

    public void setFilm(Film film) {
        mFilm = film;
    }

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public  ImageLoadedCallback(ProgressBar progBar){
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {}

        @Override
        public void onError() {}
    }

    public void refreshLayout() {

        Logger.log("film detail", "refreshLayout");

        // set film info
        if (mView != null && mFilm != null) {
            TextView textViewTitle = (TextView) mView.findViewById(R.id.title);
            textViewTitle.setText(mFilm.getTitle());

            TextView textViewReleaseDate = (TextView) mView.findViewById(R.id.releaseDate);
            textViewReleaseDate.setText(DateFormater.toLocalFormat(mFilm.getReleaseDay()));

            TextView textViewDescription = (TextView) mView.findViewById(R.id.description);
            textViewDescription.setText(mFilm.getOverview());

            ImageView imageViewPoster = (ImageView) mView.findViewById(R.id.poster);
            if(mFilm.getPosterPath() == null) {
                imageViewPoster.setVisibility(View.GONE);
            }
            else {
                Picasso
                    .with(getActivity().getApplicationContext())
                    .load(Url.URL_BASE_POSTER + mFilm.getPosterPath())
                    .into(imageViewPoster);
            }



            ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            ImageView imageViewBackdrop = (ImageView) mView.findViewById(R.id.backdrop);
            if(mFilm.getBackdropPath() == null) {
                imageViewBackdrop.setImageResource(R.drawable.no_backdrop);
            }
            else {
                Picasso
                    .with(getActivity().getApplicationContext())
                    .load(Url.URL_BASE_BACKDROP + mFilm.getBackdropPath())
                    .into(imageViewBackdrop, new ImageLoadedCallback(progressBar) {
                        @Override
                        public void onSuccess() {
                            if (this.progressBar != null) {
                                this.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
            }

            // favourite
            FloatingActionButton favouriteBtn = (FloatingActionButton) mView.findViewById(R.id.favourite);

            if(mDatabase.isFilmFavorite(mFilm)) {
                favouriteBtn.setImageResource(R.drawable.button_favourite_filled);
            }
            else {
                favouriteBtn.setImageResource(R.drawable.button_favourite_empty);
            }

            favouriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FloatingActionButton favouriteBtn = (FloatingActionButton) mView.findViewById(R.id.favourite);

                    if(mDatabase.isFilmFavorite(mFilm)) {
                        mDatabase.removeFilm(mFilm);
                        favouriteBtn.setImageResource(R.drawable.button_favourite_empty);
                        Toast.makeText(getActivity(), R.string.favourite_remove, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mDatabase.addFilm(mFilm);
                        favouriteBtn.setImageResource(R.drawable.button_favourite_filled);
                        Toast.makeText(getActivity(), R.string.favourite_add, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Logger.log("film detail", "could not refresh");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = new FilmDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.film_detail_fragment, container, false);

        refreshLayout();

        return mView;
    }

}