package cz.muni.fi.pv256.uco374366.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Z on 18. 10. 2015.
 */
public class Film implements Parcelable {

    private static long sIDcounter = 0;

    private long mReleaseDay;
    private String mCoverPath;
    private String mTitle;
    private int mCoverResource;
    private long mID;




    private long mHeader;


    public Film(long header, String title, long releaseDay, String coverPath) {
        mID = sIDcounter++;
        mHeader = header;
        mTitle = title;
        mReleaseDay = releaseDay;
        mCoverPath = coverPath;
    }

    public Film(long header, String title, long releaseDay, int coverResource) {
        mID = sIDcounter++;
        mHeader = header;
        mTitle = title;
        mReleaseDay = releaseDay;
        mCoverResource = coverResource;
    }

    public long getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public long getHeader() {
        return mHeader;
    }

    public void setHeader(long header) {
        mHeader = header;
    }

    public void setReleaseDay(long releaseDay) {
        mReleaseDay = releaseDay;
    }

    public void setCoverPath(String coverPath) {
        mCoverPath = coverPath;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setCoverResource(int coverResource) {
        mCoverResource = coverResource;
    }

    public long getReleaseDay() {
        return mReleaseDay;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getCoverResource() {
        return mCoverResource;
    }

    @Override
    public String toString() {
        return mTitle + " (" + mReleaseDay + ")";
    }

    @Override
    public int hashCode() {
        return (int)mReleaseDay * 47 + mTitle.hashCode();
    }

    /* PARCELABLE */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeInt(mCoverResource);
        dest.writeLong(mReleaseDay);
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
        mCoverPath = in.readString();
        mTitle = in.readString();
        mCoverResource = in.readInt();
        mReleaseDay = in.readLong();
    }
}
