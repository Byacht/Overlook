package com.byacht.overlook.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dn on 2017/8/5.
 */

public class MyUtils {
    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void Snackbar(View view, String message, String actionMessage, View.OnClickListener listener) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionMessage, listener)
                .show();
    }
}
