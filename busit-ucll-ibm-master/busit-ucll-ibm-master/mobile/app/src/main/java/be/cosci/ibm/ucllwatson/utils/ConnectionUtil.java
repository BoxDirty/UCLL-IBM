package be.cosci.ibm.ucllwatson.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import be.cosci.ibm.ucllwatson.R;

/**
 * Created by Petr on 29-Mar-18.
 */
public final class ConnectionUtil {

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * @param activity
     * @param layout
     * @return
     */
    public static boolean showNetworkInfoSnackbar(Activity activity, int layout) {
        if (!isNetworkAvailable(activity)) {
            Snackbar snackbar = Snackbar
                    .make(activity.findViewById(layout), R.string.main_not_connected, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            TextView tv = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.BLACK);
            snackbar.show();
            return true;
        } else {
            return false;
        }
    }

}