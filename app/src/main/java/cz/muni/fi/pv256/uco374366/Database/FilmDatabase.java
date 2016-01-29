package cz.muni.fi.pv256.uco374366.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Model.Film;

public class FilmDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_themovie_db";

    private static final String TABLE_FILM = "film";
    private static final String
        FILM_ID = "id",
        FILM_TITLE = "title",
        FILM_RELEASE_DATE = "release_date",
        FILM_OVERVIEW = "overview",
        FILM_POSTER_PATH = "poster_path",
        FILM_BACKDROP_PATH = "backdrop_path";


    public FilmDatabase(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.log("database", "creating connection");
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_FILM + " (" +
                        FILM_ID + " INTEGER PRIMARY KEY, " +
                        FILM_TITLE + " TEXT, " +
                        FILM_RELEASE_DATE + " TEXT, " +
                        FILM_OVERVIEW + " TEXT, " +
                        FILM_POSTER_PATH + " TEXT, " +
                        FILM_BACKDROP_PATH + " TEXT " +
                        ");");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILM);
        onCreate(db);
    }

    public void addFilm(Film film){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FILM_ID, film.getID());
        values.put(FILM_TITLE, film.getTitle());
        values.put(FILM_RELEASE_DATE, film.getReleaseDay());
        values.put(FILM_OVERVIEW, film.getOverview());
        values.put(FILM_POSTER_PATH, film.getPosterPath());
        values.put(FILM_BACKDROP_PATH, film.getBackdropPath());

        db.insert(TABLE_FILM, null, values);
        db.close();
    }

    public void updateFilm(Film film){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FILM_TITLE, film.getTitle());
        values.put(FILM_RELEASE_DATE, film.getReleaseDay());
        values.put(FILM_OVERVIEW, film.getOverview());
        values.put(FILM_POSTER_PATH, film.getPosterPath());
        values.put(FILM_BACKDROP_PATH, film.getBackdropPath());

        db.update(TABLE_FILM, values, FILM_ID + "=" + film.getID(), null);
        db.close();
    }

    public int removeFilm(Film film){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILM + " WHERE " + FILM_ID + " = " + film.getID(), null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        if (cursor.getCount() <= 0) {
            return -1;
        }

        db.delete(TABLE_FILM, FILM_ID + "=" + film.getID(), null);

        db.close();
        cursor.close();
        return 0;
    }

    public List<Film> getAll(){
        List<Film> films = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILM, null);

        if (cursor.moveToFirst()) {
            do {
                films.add(new Film(
                    cursor.getInt(cursor.getColumnIndex(FILM_ID)),
                    0,
                    cursor.getString(cursor.getColumnIndex(FILM_TITLE)),
                    cursor.getString(cursor.getColumnIndex(FILM_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(FILM_RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(FILM_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(FILM_BACKDROP_PATH))
                ));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return films;
    }

    public boolean isFilmFavorite(Film film){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILM + " WHERE " + FILM_ID + " = " + film.getID(), null);

        boolean isFavourite = false;
        if(cursor.getCount() > 0){
            isFavourite = true;
        }

        cursor.close();
        db.close();
        return isFavourite;
    }
}
