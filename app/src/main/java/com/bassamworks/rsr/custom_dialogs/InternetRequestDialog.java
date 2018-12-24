package com.bassamworks.rsr.custom_dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.bassamworks.rsr.R;

public class InternetRequestDialog {
    private final Context context;
    private AlertDialog.Builder dialogBuilder;

    public InternetRequestDialog(Context context) {
        this.context = context;

        setupDialog();
    }

    private void setupDialog() {
        this.dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.dialog_enable_inernet_title);
        dialogBuilder.setMessage(R.string.dialog_enable_internet_msg);

        dialogBuilder.setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int paramInt) {
                //Launch internet options in settings
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
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
