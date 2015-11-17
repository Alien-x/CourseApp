package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class FragmentFilmDetail extends Fragment{

    private View mView = null;
    private Film mFilm = null;

    public void setFilm(Film film) {
        mFilm = film;
    }

    public void refreshLayout() {

        Logger.log("film detail", "refreshLayout");

        // set film info
        if (mView != null && mFilm != null) {
            TextView textViewTitle = (TextView) mView.findViewById(R.id.title);
            textViewTitle.setText(mFilm.getTitle());

            TextView textViewReleaseDate = (TextView) mView.findViewById(R.id.releaseDate);
            textViewReleaseDate.setText(mFilm.getReleaseDay());

            //ImageView imageViewCover = (ImageView) mView.findViewById(R.id.cover);
            //imageViewCover.setImageResource(mFilm.getCoverResource());

            //ImageView imageViewBackground = (ImageView) mView.findViewById(R.id.background);
            //imageViewBackground.setImageResource(mFilm.getCoverResource());
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