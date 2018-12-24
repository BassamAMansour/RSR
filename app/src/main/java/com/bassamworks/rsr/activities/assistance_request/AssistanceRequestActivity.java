package com.bassamworks.rsr.activities.assistance_request;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bassamworks.rsr.R;
import com.bassamworks.rsr.adapters.CustomInfoWindowAdapter;
import com.bassamworks.rsr.utils.AddressFinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AssistanceRequestActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, AssistanceRequestContract.View {

    //For Logging
    private static final String LOG_TAG = AssistanceRequestActivity.class.getSimpleName();

    //Constants
    private static final float DEFAULT_MAP_ZOOM = 15f;

    //Views
    private GoogleMap googleMap;
    private AlertDialog alertDialog;
    private ImageView ivMapLoading;
    private Button btnOpenCallNowDialog;
    private boolean isTablet;

    //Presenter
    private AssistanceRequestContract.Presenter presenter;

    private MapStatus currentMapStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance_request);

        //Setup presenter
        this.presenter = new AssistanceRequestPresenter(this, this);

        //Will deal with the device as a tablet if its screen width is more than 600dp
        this.isTablet = getResources().getBoolean(R.bool.is_tablet);

        setupActivityViews();
        setupCallRSRNowDialog();
    }

    private void setupActivityViews() {

        updateIvMapLoading(MapStatus.LOADING_GENERIC);
        setupMapFragment();

        if (!isTablet) {
            setupBtnOpenCallNowDialog();
        }
    }

    private void setupMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);

        updateIvMapLoading(MapStatus.LOADING_INTERNET);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.ivMapLoading.setImageResource(R.drawable.loading_gps);

        updateIvMapLoading(MapStatus.LOADING_GPS);

        setupMapInfoWindow();
    }

    private void updateGoogleMap(final Location location) {
        if (location != null && googleMap != null) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            updateIvMapLoading(MapStatus.READY);

            googleMap.clear();
            addMapMarkerWithLocation(currentLatLng);
            moveCameraToLocation(currentLatLng);
        }
    }

    private void moveCameraToLocation(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_MAP_ZOOM));
    }

    private void addMapMarkerWithLocation(LatLng latLng) {
        Marker marker = googleMap.addMarker(getMarkerFromLatLng(latLng));
        marker.showInfoWindow();
    }

    private MarkerOptions getMarkerFromLatLng(LatLng latLng) {
        String address = new AddressFinder(this).getFormattedAddressFromLatLng(latLng);
        return new MarkerOptions()
                .snippet(address)
                .position(latLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(isTablet ? R.drawable.map_marker : R.drawable.map_marker_mini));
    }

    private void setupMapInfoWindow() {
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
    }

    private void setupBtnOpenCallNowDialog() {
        this.btnOpenCallNowDialog = (Button) findViewById(R.id.btn_open_call_now_dialog);
        btnOpenCallNowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCallRSRNow();
            }
        });
    }

    private void setupCallRSRNowDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //Inflate and set custom view to AlertDialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_call_now, null);
        alertDialogBuilder.setView(dialogView);

        this.alertDialog = alertDialogBuilder.create();

        //Setup custom dialog view
        setupDialogView(dialogView);

        //Set dialog window background to transparent
        Window dialogWindow = alertDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }

    private void setupDialogView(View dialogView) {
        setupCloseDialogLayout(dialogView.findViewById(R.id.ll_close_dialog));
        setupBtnCallNow((Button) dialogView.findViewById(R.id.btn_call_RSR_now));
    }

    private void setupCloseDialogLayout(View layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialogCallRSRNow();
            }
        });
    }

    private void setupBtnCallNow(Button btnCallNow) {
        btnCallNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleBtnCallRSRNow();
            }
        });
    }

    public void setBtnOpenDialogCallNowVisibility(int visibility) {
        this.btnOpenCallNowDialog.setVisibility(visibility);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean requestHandled = presenter.handleOnRequestPermissionResult(requestCode, permissions, grantResults);

        if (!requestHandled) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showDialogCallRSRNow() {
        if (!alertDialog.isShowing()) {
            alertDialog.show();
            setBtnOpenDialogCallNowVisibility(View.GONE);
        }
    }

    @Override
    public void dismissDialogCallRSRNow() {
        alertDialog.dismiss();
        setBtnOpenDialogCallNowVisibility(View.VISIBLE);
    }

    private void updateIvMapLoading(MapStatus mapStatus) {

        if (ivMapLoading == null) {
            this.ivMapLoading = (ImageView) findViewById(R.id.iv_loading_map);
        }

        if (currentMapStatus != null && currentMapStatus == MapStatus.READY) {
            return;
        }

        currentMapStatus = mapStatus;

        switch (mapStatus) {
            case LOADING_GENERIC:
                ivMapLoading.setVisibility(View.VISIBLE);
                ivMapLoading.setImageResource(R.drawable.loading_generic);
                break;
            case LOADING_GPS:
                ivMapLoading.setVisibility(View.VISIBLE);
                ivMapLoading.setImageResource(R.drawable.loading_gps);
                break;
            case LOADING_INTERNET:
                ivMapLoading.setVisibility(View.VISIBLE);
                ivMapLoading.setImageResource(R.drawable.loading_internet);
                break;
            default:
                ivMapLoading.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void updateMap(Location location) {
        updateGoogleMap(location);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    private enum MapStatus {LOADING_GENERIC, LOADING_INTERNET, LOADING_GPS, READY}

}
