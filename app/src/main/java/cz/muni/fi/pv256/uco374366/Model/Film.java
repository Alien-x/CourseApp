package cz.muni.fi.pv256.uco374366.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Z on 18. 10. 2015.
 */
public class Film implements Parcelable {

    private long mID;
    private int mGroup;

    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mPosterPath;
    private String mBackdropPath;

    public Film(long id, int group, String title, String overview, String releaseDate,
                String posterPath, String backdropPath) {
        mID = id;
        mGroup = group;
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
    }


    public long getID() {
        return mID;
    }

    public long getGroup() {
        return mGroup;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDay() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }



    @Override
    public String toString() {
        return "#" + mID + " : " + mTitle;
    }

    @Override
    public int hashCode() {
        return (int)mID;
    }

    /* PARCELABLE */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mID);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    private Film(Parcel in) {
        mID = in.readLong();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
    }
}
