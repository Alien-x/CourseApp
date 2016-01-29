package cz.muni.fi.pv256.uco374366.Database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;


public class FilmProvider  extends ContentProvider {

    private static final String TAG = FilmProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int FILMS = 100;
    public static final int FILM_ID = 110;
    //private FilmDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
       /* final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FilmContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FilmContract.PATH_FILM, FILMS);
        matcher.addURI(authority, FilmContract.PATH_FILM + "/#", FILM_ID);

        return matcher;*/
        return null;
    }

    @Override
    public boolean onCreate() {
        /*mOpenHelper = new FilmDbHelper(getContext());*/
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /*Log.d(TAG, Arrays.toString(selectionArgs));
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmContract.FilmEntry.TABLE_NAME,
                        projection,
                        FilmContract.FilmEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FILMS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmContract.FilmEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;*/
        return null;
    }

    @Override
    public String getType(Uri uri) {
        /*final int match = sUriMatcher.match(uri);

        switch (match) {
            case FILMS:
                return FilmContract.FilmEntry.CONTENT_TYPE;
            case FILM_ID:
                return FilmContract.FilmEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }*/
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /*Log.d(TAG, values.toString());
        Uri returnUri;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id = db.insert(FilmContract.FilmEntry.TABLE_NAME, null, values);
        if (_id > 0) {
            returnUri = FilmContract.FilmEntry.buildWorkTimeUri(_id);
        }
        else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;*/
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        /*final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = db.delete(FilmContract.FilmEntry.TABLE_NAME, selection, selectionArgs);

        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
        */
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        /*final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = db.update(FilmContract.FilmEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;*/
        return 0;
    }
}
