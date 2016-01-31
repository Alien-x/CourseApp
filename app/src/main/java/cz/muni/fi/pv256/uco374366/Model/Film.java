package cz.muni.fi.pv256.uco374366.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zdenek Kanovsky on 18. 10. 2015.
 */
public class Film implements Parcelable {

    @SerializedName("id")
    private long mID;

    private int mGroup;

    @SerializedName("original_title")
    private String mTitle;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("backdrop_path")
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

    public void setGroup(int group) {
        mGroup = group;
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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Film)) return false;
        Film otherFilm = (Film)other;
        try {
            return mID == otherFilm.mID
                && (mTitle == null ? mTitle == otherFilm.mTitle : mTitle.equals(otherFilm.mTitle))
                && (mOverview == null ? mOverview == otherFilm.mOverview : mOverview.equals(otherFilm.mOverview))
                && (mReleaseDate == null ? mReleaseDate == otherFilm.mReleaseDate : mReleaseDate.equals(otherFilm.mReleaseDate))
                && (mPosterPath == null ? mPosterPath == otherFilm.mPosterPath : mPosterPath.equals(otherFilm.mPosterPath))
                && (mBackdropPath == null ? mBackdropPath == otherFilm.mBackdropPath : mBackdropPath.equals(otherFilm.mBackdropPath));
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int)mID;
    }
}

