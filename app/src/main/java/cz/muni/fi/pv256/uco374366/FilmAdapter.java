package cz.muni.fi.pv256.uco374366;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cz.muni.fi.pv256.uco374366.Model.Film;

/**
 * Created by Z on 18. 10. 2015.
 */
public class FilmAdapter extends BaseAdapter {

    private Context mContext;
    private List<Film> mFilms;

    public FilmAdapter(Context context, List<Film> films) {
        mContext = context;
        mFilms = films;
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
        return (long)mFilms.get(i).getTitle().hashCode();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private static class ViewHolder {
        ImageView cover;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.film_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            convertView.setTag(holder);
            Log.d("viewholder", "inflate pozice " + position);
        } else {
            Log.d("viewholder", "recyklace pozice " + position);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.cover.setImageResource(mFilms.get(position).getCoverResource());                   // mFilms.get(position).getCoverPath()
        return convertView;
    }
}
