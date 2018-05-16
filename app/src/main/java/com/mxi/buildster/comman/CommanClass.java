package com.mxi.buildster.comman;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


/**
 * Created by admin1 on 21/3/16.
 */
public class CommanClass {

    private Context _context;
    SharedPreferences pref;


    public CommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("Goalkeeper",
                _context.MODE_PRIVATE);


    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String text) {
        // TODO Auto-generated method stub

        final Toast t = Toast.makeText(_context, text, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);

        new CountDownTimer(Math.max(100,100),200) {
            @Override
            public void onFinish() {
                t.show();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                t.show();
            }
        }.start();

       /* Toast.makeText(_context, text, Toast.LENGTH_LONG).show();*/
    }

    public void showSnackbar(View coordinatorLayout, String text) {

        Snackbar
                .make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public void savePrefInt(String key, int value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //REtrieve String data from SharedPreferences
    public int loadPrefInt(String key) {
        // TODO Auto-generated method stub
        int strSaved = pref.getInt(key, 0);
        return strSaved;
    }


    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
