package com.example.reto1_android.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.example.reto1_android.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Util{
    /**
     * Comprueba si hay conexion a internet
     * @return true si hay conexion a internet
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showErrorInternetAlert(Context context){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.noInternet))
                .setCancelText(context.getString(R.string.btnOk))
                .setConfirmText(context.getString(R.string.conf))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        context.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                        sDialog.dismiss();

                    }
                }).show();
    }
}
