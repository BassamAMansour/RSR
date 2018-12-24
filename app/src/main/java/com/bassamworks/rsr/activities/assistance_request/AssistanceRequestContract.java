package com.bassamworks.rsr.activities.assistance_request;

import android.location.Location;

public interface AssistanceRequestContract {

    interface View {
        void updateMap(Location currentLocation);

        void showDialogCallRSRNow();

        void dismissDialogCallRSRNow();

        void showMessage(String message);
    }

    interface Presenter {
        void handleBtnCallRSRNow();

        boolean handleOnRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

        void onResume();

        void onPause();
    }
}
