package cz.muni.fi.pv256.uco374366.Model;

/**
 * Created by Z on 18. 10. 2015.
 */
public class Film {

    private long mReleaseDay;
    private String mCoverPath;
    private String mTitle;
    private int mCoverResource;


    public Film(String title, long releaseDay, String coverPath) {
        mTitle = title;
        mReleaseDay = releaseDay;
        mCoverPath = coverPath;
    }

    public Film(String title, long releaseDay, int coverResource) {
        mTitle = title;
        mReleaseDay = releaseDay;
        mCoverResource = coverResource;
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
}
