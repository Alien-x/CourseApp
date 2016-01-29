package cz.muni.fi.pv256.uco374366;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cz.muni.fi.pv256.uco374366.Fragment.FragmentFilmDetail;
import cz.muni.fi.pv256.uco374366.Fragment.FragmentFilmList;
import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Sync.UpdaterSyncAdapter;


public class MainActivity extends AppCompatActivity {

    private static int mSource = R.id.action_discover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("MainActivity", "onCreate");

        if(BuildConfig.secondary) {
            setTheme(R.style.AppTheme_Secondary);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragments(mSource);

        UpdaterSyncAdapter.initializeSyncAdapter(this);


    }

    private void setFragments(int source) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // film list
        FragmentFilmList fragmentFilmList = new FragmentFilmList();
        fragmentFilmList.setSource(source);

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

        int id = item.getItemId();

        if(id == R.id.action_discover) {
            Logger.log("action", "discover");
            mSource = R.id.action_discover;
            setFragments(mSource);
        }
        else if(id == R.id.action_favourites) {
            Logger.log("action", "favourites");
            mSource = R.id.action_favourites;
            setFragments(mSource);
        }
        else if(id == R.id.action_refresh) {
            Logger.log("action", "favourites");
            UpdaterSyncAdapter.syncImmediately(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isTablet() {
        return  getResources().getBoolean(R.bool.isTablet);
    }
}
