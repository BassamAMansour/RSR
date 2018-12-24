package com.bassamworks.rsr.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.bassamworks.rsr.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressFinder {

    private final Context context;

    public AddressFinder(Context context) {
        this.context = context;
    }

    public String getFormattedAddressFromLatLng(LatLng latLng) {

        StringBuilder addressBuilder = new StringBuilder("");

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            Address firstAddress = addresses.get(0);

            String streetName = firstAddress.getThoroughfare();
            String streetNumber = firstAddress.getSubThoroughfare();
            String postalCode = firstAddress.getPostalCode();
            String city = firstAddress.getLocality();

            addressBuilder = getFormattedAddressBuilder(streetName, streetNumber, postalCode, city);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressBuilder.length() == 0) {
            return context.getString(R.string.address_not_found);
        }

        return addressBuilder.toString().trim();
    }

    private StringBuilder getFormattedAddressBuilder(String streetName, String streetNumber, String postalCode, String city) {
        StringBuilder addressBuilder = new StringBuilder("");

        addressBuilder.append(streetName == null ? "" : streetName);
        addressBuilder.append(streetNumber == null ? "" : ", " + streetNumber);
        addressBuilder.append(postalCode == null ? "" : (", " + postalCode));
        addressBuilder.append(city == null ? "" : ", " + city);

        if (addressBuilder.charAt(0) == ',') {
            addressBuilder.deleteCharAt(0);
        }

        return addressBuilder;
    }
}
