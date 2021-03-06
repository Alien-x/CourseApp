package cz.muni.fi.pv256.uco374366.Misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.support.v4.util.LongSparseArray;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Model.Film;
import cz.muni.fi.pv256.uco374366.R;

/**
 * Created by Zdenek Kanovsky on 18. 10. 2015.
 */
public class FilmAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private static final String TMD_POSTER_URL = "http://image.tmdb.org/t/p/w150/";

    private Context mContext;
    private List<Film> mFilms;
    private LongSparseArray<String> mHeaders;
    //private LayoutInflater mInflater;

    public FilmAdapter(Context context, List<Film> films, LongSparseArray<String> headers) {
        mContext = context;
        //mInflater = LayoutInflater.from(context);

        // film list
        if(films != null) {
            mFilms = films;
        }
        else {
            mFilms = new ArrayList<>();
        }

        // headers list
        if(headers != null) {
            mHeaders = headers;
        }
        else {
            mHeaders = new LongSparseArray<>();
        }
    }

    @Override
    public int getCount() {
        return mFilms.size();
    }

    @Override
    public Object getItem(int i) {
        return mFilms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mFilms.get(i).getID();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private static class ViewHolder {
        ImageView poster;
    }

    private class ImageLoadedCallback implements Callback {
        TextView textViewTitle;

        public  ImageLoadedCallback(TextView textView){
            textViewTitle = textView;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.film_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.poster = (ImageView) convertView.findViewById(R.id.poster);
            convertView.setTag(holder);
            Logger.log("viewholder", "inflate pozice " + position);
        } else {
            Logger.log("viewholder", "recyklace pozice " + position);
        }

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.title);
        textViewTitle.setVisibility(View.VISIBLE);
        textViewTitle.setText(mFilms.get(position).getTitle());
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if(mFilms.get(position).getPosterPath() == null) {
            holder.poster.setImageResource(R.drawable.no_poster);
        }
        else {
            Picasso
                    .with(convertView.getContext())
                    .load(TMD_POSTER_URL + mFilms.get(position).getPosterPath())
                    .into(holder.poster, new ImageLoadedCallback(textViewTitle) {
                        @Override
                        public void onSuccess() {
                            if (this.textViewTitle != null) {
                                this.textViewTitle.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        // mFilms.get(position).getCoverPath()
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mFilms.get(position).getGroup();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.film_group_header, parent, false);
        }

        TextView headerText = (TextView) convertView.findViewById(R.id.film_group_header);
        headerText.setText(mHeaders.get(getHeaderId(position)));

        return convertView;

    }
}
