package com.example.autocompleteplacesfragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtVw;
    double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtVw = findViewById(R.id.placeName);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                txtVw.setText(place.getName());
                lat = place.getLatLng().latitude;
                longi = place.getLatLng().longitude;

                GteData(lat, longi);
            }

            @Override
            public void onError(Status status) {
                txtVw.setText(status.toString());
            }
        });
    }

    private void GteData(double lat, double longi) {

        HashMap<String, String> data = new HashMap<>();

        data.put("latlng", lat + "," + longi);
        data.put("key", "AIzaSyDELpqMi27VwVMB44JliiQG3wSDAYEuG_c");

        Api api = ApiClient.apiclient().create(Api.class);
        api.placedata(data).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                int size=((ArrayList) ((LinkedTreeMap)((ArrayList)response.body().get("results")).get(0)).get("address_components")).size()-1;


                String code = ((LinkedTreeMap) ((ArrayList) ((LinkedTreeMap) ((ArrayList) response.body().get("results")).get(0)).get("address_components")).get(size)).get("long_name").toString();
                txtVw.setText(code);
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {

            }
        });
    }
}
