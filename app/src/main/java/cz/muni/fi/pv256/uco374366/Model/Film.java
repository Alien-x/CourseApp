package cz.muni.fi.pv256.uco374366.Model;

/**
 * Created by Z on 18. 10. 2015.
 */
public class Film {

    private long m_releaseDay;
    private String m_coverPath;
    private String m_title;


    public Film(String title, long releaseDay, String coverPath) {
        m_title = title;
        m_releaseDay = releaseDay;
        m_coverPath = coverPath;
    }
}
