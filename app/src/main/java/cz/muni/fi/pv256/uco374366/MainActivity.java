package cz.muni.fi.pv256.uco374366;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Model.Film;


public class MainActivity extends ActionBarActivity {

    private List<Film> mFilms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // gridview
        GridView gridview = (GridView) findViewById(R.id.gridViewFilms);

        if(isOnline()) {
            // fake data
            mFilms.add(new Film("Alien vs. Predator", 0, R.drawable.avp));
            mFilms.add(new Film("American Pie", 0, R.drawable.american_pie));
            mFilms.add(new Film("Rocky", 0, R.drawable.rocky));
            mFilms.add(new Film("How to train your dragon", 0, R.drawable.dragon));
            mFilms.add(new Film("Starship troopers", 0, R.drawable.starship_troopers));

            gridview.setEmptyView(findViewById(R.id.emptyView));
            gridview.setAdapter(new FilmAdapter(this, mFilms));
        }
        else {
            gridview.setEmptyView(findViewById(R.id.noConnectionView));
        }


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FilmActivity.class);

                intent.putExtra("TITLE", mFilms.get(position).getTitle());
                intent.putExtra("RELEASE_DATE", mFilms.get(position).getReleaseDay());
                intent.putExtra("COVER", mFilms.get(position).getCoverResource());

                startActivity(intent);
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, mFilms.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });


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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
