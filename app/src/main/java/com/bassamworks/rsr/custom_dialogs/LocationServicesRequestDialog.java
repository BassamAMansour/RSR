package com.bassamworks.rsr.custom_dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.bassamworks.rsr.R;

public class LocationServicesRequestDialog {

    private final Context context;
    private AlertDialog.Builder dialogBuilder;

    public LocationServicesRequestDialog(@NonNull Context context) {
        this.context = context;

        setupDialog();
    }

    private void setupDialog() {
        this.dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setTitle(R.string.dialog_enable_gps_title);
        dialogBuilder.setMessage(R.string.dialog_enable_gps_msg);

        dialogBuilder.setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int paramInt) {
                //Launch location services in settings
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int paramInt) {
                dialogInterface.dismiss();
            }
        });
    }

    public void showDialog() {
        dialogBuilder.show();
    }

}
