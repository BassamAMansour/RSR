package com.bassamworks.rsr.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import com.bassamworks.rsr.R;
import com.bassamworks.rsr.activities.assistance_request.AssistanceRequestActivity;
import com.bassamworks.rsr.activities.info.InfoActivity;
import com.bassamworks.rsr.custom_dialogs.InternetRequestDialog;
import com.bassamworks.rsr.custom_dialogs.LocationServicesRequestDialog;

public class MainPresenter
        implements MainContract.Presenter {

    private static final int REQUEST_CODE_PERMISSION_GPS = 100;

    private final Activity context;
    private final MainContract.View view;

    MainPresenter(Activity context, MainContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void handleActionOpenInfoActivity() {
        context.startActivity(new Intent(context, InfoActivity.class));
    }

    @Override
    public void handleActionOpenRequestAssistanceActivity() {
        if (hasGPSPermission()) {
            startAssistanceRequestActivityOpeningProcedure();
        } else {
            requestGPSPermission();
        }
    }

    @Override
    public boolean handleOnRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case MainPresenter.REQUEST_CODE_PERMISSION_GPS:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleActionOpenRequestAssistanceActivity();
                } else {
                    view.showMessage(context.getString(R.string.allow_access_location_message));
                }
                return true;

            default:
                return false;
        }
    }

    /**
     * Makes the checks required to make sure the internet and gps are enabled prior opening the activity.
     * <p>
     * Asks the user to enable the internet and/or gps.
     */
    private void startAssistanceRequestActivityOpeningProcedure() {

        if (isConnectedToInternet() && isGPSEnabled()) {
            startAssistanceRequestActivity();
        } else if (!isConnectedToInternet()) {
            askToConnectToInternet();
        } else if (!isGPSEnabled()) {
            askToEnableGPS();
        }
    }

    private boolean hasGPSPermission() {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGPSPermission() {
        ActivityCompat.requestPermissions(
                context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_PERMISSION_GPS);
    }

    private boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void askToEnableGPS() {
        new LocationServicesRequestDialog(context).showDialog();
    }

    private void askToConnectToInternet() {
        new InternetRequestDialog(context).showDialog();
    }

    private void startAssistanceRequestActivity() {
        context.startActivity(new Intent(context, AssistanceRequestActivity.class));
    }
}
