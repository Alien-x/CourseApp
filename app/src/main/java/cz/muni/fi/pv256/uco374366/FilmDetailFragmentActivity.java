package cz.muni.fi.pv256.uco374366;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class FilmDetailFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(BuildConfig.secondary) {
            setTheme(R.style.AppTheme_Secondary);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);


        Intent i = getIntent();
        Film film = i.getParcelableExtra("FILM");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentFilmDetail fragmentFilmDetail = new FragmentFilmDetail();
        fragmentFilmDetail.setFilm(film);

        fragmentTransaction.replace(R.id.film_detail_fragment, fragmentFilmDetail, "FILM_DETAIL_FRAGMENT");
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
