package com.bassamworks.rsr.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bassamworks.rsr.R;

public class MainActivity
        extends AppCompatActivity
        implements MainContract.View {

    private boolean isTablet;
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup presenter
        this.presenter = new MainPresenter(this, this);

        //Will deal with the device as a tablet if its screen width is more than 600dp
        this.isTablet = getResources().getBoolean(R.bool.is_tablet);


        setupViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isTablet) {
            getMenuInflater().inflate(R.menu.menu_main_activity, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!isTablet) {
            switch (item.getItemId()) {
                case R.id.action_open_info_activity:
                    presenter.handleActionOpenInfoActivity();
                    return true;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean resultHandled = presenter.handleOnRequestPermissionResult(requestCode, permissions, grantResults);

        if (!resultHandled) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupViews() {
        setupBtnRequestAssistance();

        if (isTablet) {
            setupBtnRSRInfo();
        }
    }

    private void setupBtnRequestAssistance() {
        Button btnRequestAssistance = (Button) findViewById(R.id.btn_request_assistance);
        btnRequestAssistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleActionOpenRequestAssistanceActivity();

            }
        });
    }

    private void setupBtnRSRInfo() {
        Button btnRSRInfo = (Button) findViewById(R.id.btn_RSR_info);
        btnRSRInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleActionOpenInfoActivity();
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


