package com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Pol on 21/10/2016.
 */
public class CheckInternetConnection {

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
