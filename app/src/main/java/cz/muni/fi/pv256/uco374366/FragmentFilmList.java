package cz.muni.fi.pv256.uco374366;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.util.LongSparseArray;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.List;
import cz.muni.fi.pv256.uco374366.Model.Film;

import cz.muni.fi.pv256.uco374366.Service.DownloadService;
import cz.muni.fi.pv256.uco374366.Service.DownloadServiceReceiver;

public class FragmentFilmList extends Fragment implements DownloadServiceReceiver.Receiver {

    private DownloadServiceReceiver mReceiver = null;

    private FragmentFilmDetail mFragmentFilmDetail = null;
    private List<Film> mFilms = new ArrayList<>();

    private LongSparseArray<String> mHeaders = new LongSparseArray<>();
    public static final int GROUP_MOST_POPULAR = 1;
    public static final int GROUP_IN_THEATERS = 2;

    private FilmAdapter mFilmAdapter = null;
    private GridView mGridView = null;
    private View mView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Starting Download Service
        mReceiver = new DownloadServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadService.class);

        // Send optional extras to Download IntentService
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("groups", new int[] {
            GROUP_IN_THEATERS,
            GROUP_MOST_POPULAR
        });

        mHeaders.append(GROUP_IN_THEATERS, getActivity().getResources().getString(R.string.film_group_in_theaters));
        mHeaders.append(GROUP_MOST_POPULAR, getActivity().getResources().getString(R.string.film_group_most_popular));

        getActivity().startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DownloadService.STATUS_RUNNING:
                Logger.log("download_service", "STATUS_RUNNING");
                break;
            case DownloadService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                List<Film> downFilms = resultData.getParcelableArrayList("films");
                mFilms.addAll(downFilms);

                if (mGridView != null) {
                    mGridView.setEmptyView(mView.findViewById(R.id.loading));
                    mFilmAdapter.notifyDataSetChanged();
                }

                // tablet
                if (mFragmentFilmDetail != null) {
                    Logger.log("film_list", "setting fragment detail (tablet)");
                    mFragmentFilmDetail.setFilm(mFilms.get(0));
                    mFragmentFilmDetail.refreshLayout();
                }
                /* Update ListView with result */
                Logger.log("download_service", "STATUS_FINISHED");
                break;
            case DownloadService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Logger.log("download_service", "STATUS_ERROR: "+error);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.film_list_fragment, container, false);

        mFilmAdapter = new FilmAdapter(getActivity().getApplicationContext(), mFilms, mHeaders);

        mGridView = (StickyGridHeadersGridView) mView.findViewById(R.id.gridViewFilms);
        mGridView.setAdapter(mFilmAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.log("film_list", "ItemClick");

                // tablet
                if (mFragmentFilmDetail != null) {
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

        /*mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), mFilms.get(position).getTitle() + " (" + mFilms.get(position).getGroup() + ")",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        if (isOnline()) {
            mGridView.setEmptyView(mView.findViewById(R.id.loading));
        } else {
            mGridView.setEmptyView(mView.findViewById(R.id.noConnectionView));
        }

        return mView;
    }

    public void setFragmentFilmDetail(FragmentFilmDetail fragmentFilmDetail) {
        mFragmentFilmDetail = fragmentFilmDetail;
    }

    private boolean isOnline() {

        /*ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();*/
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}