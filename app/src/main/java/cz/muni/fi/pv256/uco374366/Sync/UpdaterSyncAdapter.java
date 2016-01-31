package cz.muni.fi.pv256.uco374366.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import cz.muni.fi.pv256.uco374366.Database.FilmDatabase;
import cz.muni.fi.pv256.uco374366.Activity.MainActivity;
import cz.muni.fi.pv256.uco374366.Misc.Logger;
import cz.muni.fi.pv256.uco374366.Model.Film;
import cz.muni.fi.pv256.uco374366.R;
import cz.muni.fi.pv256.uco374366.Service.DownloadService;

/**
 * Created by Zdenek Kanovsky on 29. 1. 2016.
 */
public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the server, in seconds.
    public static final int SYNC_INTERVAL = 60 * 60 * 24; //day
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private FilmDatabase mFilmDatabase;

    public UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY) //enter non null Bundle, otherwise on some phones it crashes sync
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver
                .requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one if the
     * fake account doesn't exist yet.  If we make a new account, we call the onAccountCreated
     * method so we can initialize things.
     *
     * @param context The context used to access the account service
     *
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {

        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        // Create the account type and default account
        Account newAccount =
                new Account(context.getString(R.string.app_id), context.getString(R.string.account_type));
        // If the password doesn't exist, the account doesn't exist
        accountManager.getPassword(newAccount);

        if (null == accountManager.getPassword(newAccount)) {
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Logger.log("sync_adapter", "onPerformSync");
        mFilmDatabase = new FilmDatabase(getContext());
        List<Film> favouriteFilms = mFilmDatabase.getAll();
        int updatedFilms = 0;

        for(Film favouriteFilm : favouriteFilms) {

            Logger.log("sync_adapter", "downloading film #" + favouriteFilm.getID());
            try {
                Film downloadedFilm = DownloadService.downloadFilm(favouriteFilm);
                if(!favouriteFilm.equals(downloadedFilm)) {
                    Logger.log("sync_adapter", "favouriteFilm (" + favouriteFilm + ") NOT EQUALS downloadedFilm (" + downloadedFilm + ")");
                    /*
                    Logger.log("sync_adapter", "id: " + (favouriteFilm.getID() == downloadedFilm.getID() ? "equal" : "NOT EQUAL - '"+favouriteFilm.getID()+"' vs. '"+downloadedFilm.getID()+"'"));
                    Logger.log("sync_adapter", "title: " + (favouriteFilm.getTitle().equals(downloadedFilm.getTitle()) ? "equal" : "NOT EQUAL - '"+favouriteFilm.getTitle()+"' vs. '"+downloadedFilm.getTitle()+"'"));
                    Logger.log("sync_adapter", "overview: " + (favouriteFilm.getOverview().equals(downloadedFilm.getOverview()) ? "equal" : "NOT EQUAL - '"+favouriteFilm.getOverview()+"' vs. '"+downloadedFilm.getOverview()+"'"));
                    Logger.log("sync_adapter", "release date: " + (favouriteFilm.getReleaseDay().equals(downloadedFilm.getReleaseDay()) ? "equal" : "NOT EQUAL - '"+favouriteFilm.getReleaseDay()+"' vs. '"+downloadedFilm.getReleaseDay()+"'"));
                    Logger.log("sync_adapter", "poster path: " + (favouriteFilm.getPosterPath().equals(downloadedFilm.getPosterPath()) ? "equal" : "NOT EQUAL - '"+favouriteFilm.getPosterPath()+"' vs. '"+downloadedFilm.getPosterPath()+"'"));
                    Logger.log("sync_adapter", "backdrop path: " + (favouriteFilm.getBackdropPath().equals(downloadedFilm.getBackdropPath()) ? "equal" : "NOT EQUAL - '"+favouriteFilm.getBackdropPath()+"' vs. '"+downloadedFilm.getBackdropPath()+"'"));
                    */

                    mFilmDatabase.updateFilm(downloadedFilm);
                    updatedFilms++;
                }
                else {
                    Logger.log("sync_adapter", "favouriteFilm #" + favouriteFilm.getID() + " ***EQUALS*** downloadedFilm");
                }

                mFilmDatabase.updateFilm(downloadedFilm);
            }
            catch (Exception e) {
                Logger.log("sync_adapter", "error when downloading film #" + favouriteFilm.getID() + ": " + e);
            }
        }

        if(updatedFilms > 0) {
            notifyUser(updatedFilms);
        }
        else {
            Logger.log("sync_adapter", "no updates...");
        }
    }

    private void notifyUser(int updatedFilms) {

        Context context = getContext();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.notification_icon_small)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.notification_text_1) +
                                " (" + updatedFilms + ") " +
                                context.getString(R.string.notification_text_2));

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        PendingIntent resultPendingIntent  =
        PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);


        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
