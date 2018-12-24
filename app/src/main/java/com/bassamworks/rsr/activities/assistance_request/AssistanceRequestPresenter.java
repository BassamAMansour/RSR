package com.bassamworks.rsr.activities.assistance_request;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bassamworks.rsr.R;
import com.bassamworks.rsr.utils.LocationFinder;

public class AssistanceRequestPresenter
        implements AssistanceRequestContract.Presenter, LocationFinder.LocationChangeCallbacks {

    private static final String LOG_TAG = AssistanceRequestPresenter.class.getSimpleName();
    private static final int REQUEST_CALL_PHONE_PERMISSION = 200;

    private final Activity context;
    private final AssistanceRequestContract.View view;
    private LocationFinder locationFinder;

    AssistanceRequestPresenter(Activity context, AssistanceRequestContract.View view) {
        this.context = context;
        this.view = view;

        this.locationFinder = new LocationFinder(context, this);
    }

    @Override
    public void onResume() {
        locationFinder.requestLocationUpdates();
    }

    @Override
    public void onPause() {
        locationFinder.cancelLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            view.updateMap(location);
        }
    }

    @Override
    public boolean handleOnRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case AssistanceRequestPresenter.REQUEST_CALL_PHONE_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleBtnCallRSRNow();
                } else {
                    view.showMessage(context.getString(R.string.allow_phone_call_message));
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public void handleBtnCallRSRNow() {
        if (hasCallPermission()) {
            callRSRAssistance();
            view.dismissDialogCallRSRNow();
        } else {
            requestCallPermission();
        }
    }

    private void callRSRAssistance() {
        Intent callIntent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + context.getResources().getString(R.string.rsr_netherland_phone_number)));

        try {
            context.startActivity(callIntent);
        } catch (SecurityException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(
                context,
                new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL_PHONE_PERMISSION);
    }

    private boolean hasCallPermission() {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }
}
