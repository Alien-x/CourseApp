package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // film list
        FragmentFilmList fragmentFilmList = new FragmentFilmList();

        fragmentTransaction.add(R.id.film_list_fragment, fragmentFilmList, "FILM_LIST_FRAGMENT");

        // film detail on right side (tablet)
        if(isTablet()) {
            Log.d("screen", "tablet");
            FragmentFilmDetail fragmentFilmDetail = new FragmentFilmDetail();
            fragmentFilmList.setFragmentFilmDetail(fragmentFilmDetail);

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
