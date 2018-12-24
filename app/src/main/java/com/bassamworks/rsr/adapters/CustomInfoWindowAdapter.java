package com.bassamworks.rsr.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.bassamworks.rsr.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter
        implements GoogleMap.InfoWindowAdapter {

    private final Activity context;

    public CustomInfoWindowAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View container = context.getLayoutInflater().inflate(R.layout.map_custom_info_window, null);

        setupTvAddress(container, marker.getSnippet());

        return container;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void setupTvAddress(View parent, String address) {
        TextView tvAddress = parent.findViewById(R.id.tv_address);
        tvAddress.setText(address);
    }
}
