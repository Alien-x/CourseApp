package cz.muni.fi.pv256.uco374366.Service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Zdenek Kanovsky on 29. 1. 2016.
 */
public class DownloadServiceReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public DownloadServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}