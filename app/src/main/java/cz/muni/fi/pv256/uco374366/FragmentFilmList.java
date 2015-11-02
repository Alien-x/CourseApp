package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Model.Film;

public class FragmentFilmList extends Fragment{

    private FragmentFilmDetail mFragmentFilmDetail = null;
    private List<Film> mFilms = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.film_list_fragment, container, false);

        // gridview
        StickyGridHeadersGridView gridview = (StickyGridHeadersGridView) view.findViewById(R.id.gridViewFilms);

        if(isOnline()) {
            // fake data
            mFilms.add(new Film("Alien vs. Predator", 0, R.drawable.avp));
            mFilms.add(new Film("American Pie", 0, R.drawable.american_pie));
            mFilms.add(new Film("Rocky", 0, R.drawable.rocky));
            mFilms.add(new Film("How to train your dragon", 0, R.drawable.dragon));
            mFilms.add(new Film("Starship troopers", 0, R.drawable.starship_troopers));

            gridview.setEmptyView(view.findViewById(R.id.emptyView));
            gridview.setAdapter(new FilmAdapter(getActivity().getApplicationContext(), mFilms));
        }
        else {
            gridview.setEmptyView(view.findViewById(R.id.noConnectionView));
        }


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("film list", "ItemClick");

                // tablet
                if(mFragmentFilmDetail != null) {
                    Log.d("film list", "set fragment detail");
                    mFragmentFilmDetail.setFilm(mFilms.get(position));
                    mFragmentFilmDetail.refreshLayout();
                }
                // mobile
                else {
                    Log.d("film list", "new activity detail");


                    Intent intent = new Intent(getActivity(), FilmDetailFragmentActivity.class);
                    intent.putExtra("FILM", mFilms.get(position));
                    startActivity(intent);
                }

            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), mFilms.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }

    public void setFragmentFilmDetail(FragmentFilmDetail fragmentFilmDetail) {
        mFragmentFilmDetail = fragmentFilmDetail;
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}