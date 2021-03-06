package com.myfavplaces.virgileflorianfrancois.myfavoriteplaces;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FavPlaceDataSource datasource;
    ListView listAddr;
    MonAdapteur Monadapter;
    List<FavPlace> l;
    private String idFavPlace ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idFavPlace= null;
            } else {
                idFavPlace= extras.getString("FavPlaceSelected");
            }
        } else {
            idFavPlace= (String) savedInstanceState.getSerializable("FavPlaceSelected");
        }





    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // Add a marker in Sydney and move the camera


        datasource = new FavPlaceDataSource(this);
        datasource.open();

        if(idFavPlace == null) {
            List<FavPlace> values = datasource.getAllFavPlaces("");
            this.l = new ArrayList(Arrays.asList(new FavPlace[]{}));

            for (int i = 0; i < values.size(); i++) {
                this.l.add(values.get(i));
                LatLng pos = new LatLng(values.get(i).getLatitude(), values.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(pos).title(values.get(i).getAdresseFavPlace() + " - " + values.get(i).getVilleAdresse()));
            }
        }  else {
            System.out.println("extra : "+idFavPlace);
            List<FavPlace> values = datasource.getFavPlace(idFavPlace);
            this.l = new ArrayList(Arrays.asList(new FavPlace[]{}));
            for (int i = 0; i < values.size(); i++) {
                this.l.add(values.get(i));
                LatLng pos = new LatLng(values.get(i).getLatitude(), values.get(i).getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,13));
                mMap.addMarker(new MarkerOptions().position(pos).title(values.get(i).getAdresseFavPlace() + " - " + values.get(i).getVilleAdresse()));
            }
        }

    }
}
