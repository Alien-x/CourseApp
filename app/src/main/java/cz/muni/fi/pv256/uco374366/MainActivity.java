package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Model.Film;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class MainActivity extends AppCompatActivity {

    private List<Film> mFilms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("MainActivity", "onCreate");

        if(BuildConfig.secondary) {
            setTheme(R.style.AppTheme_Secondary);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // film list
        FragmentFilmList fragmentFilmList = new FragmentFilmList();
        fragmentFilmList.loadFilms(mFilms);

        fragmentTransaction.replace(R.id.film_list_fragment, fragmentFilmList, "FILM_LIST_FRAGMENT");

        // film detail on right side (tablet)
        if(isTablet()) {
            Logger.log("screen", "tablet");
            FragmentFilmDetail fragmentFilmDetail = new FragmentFilmDetail();
            fragmentFilmList.setFragmentFilmDetail(fragmentFilmDetail);

            // film list margin
            ViewGroup.LayoutParams lp = ((ViewGroup) findViewById(R.id.film_list_fragment)).getLayoutParams();
            if( lp instanceof ViewGroup.MarginLayoutParams)
            {
                ((ViewGroup.MarginLayoutParams) lp).rightMargin = 5;
            }

            fragmentTransaction.add(R.id.film_detail_fragment, fragmentFilmDetail, "FILM_DETAIL_FRAGMENT");
        }
        else {
            setFrameLayoutWeight((FrameLayout) findViewById(R.id.film_list_fragment), 1.0f);
            setFrameLayoutWeight((FrameLayout) findViewById(R.id.film_detail_fragment), 0.0f);
        }


        fragmentTransaction.commit();

    }

    private void setFrameLayoutWeight(FrameLayout frameLayout, float weight) {
        LinearLayout.LayoutParams filmDetailLayoutParams = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
        filmDetailLayoutParams.weight = weight;
        frameLayout.setLayoutParams(filmDetailLayoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isTablet() {
        return  getResources().getBoolean(R.bool.isTablet);
    }

}
