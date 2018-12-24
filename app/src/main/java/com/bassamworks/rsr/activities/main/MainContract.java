package com.bassamworks.rsr.activities.main;

public interface MainContract {

    interface View {
        void showMessage(String message);
    }

    interface Presenter {
        void handleActionOpenInfoActivity();

        void handleActionOpenRequestAssistanceActivity();

        boolean handleOnRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
    }

}
