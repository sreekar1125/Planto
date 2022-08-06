package com.dev.planto.bottomsheet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dev.planto.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

public class LocationMapsBS extends BottomSheetDialogFragment implements OnMapReadyCallback {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    private TextView txtLocation;

    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.location_map_bs,container,true);
        mapView = v.findViewById(R.id.mapView);

        txtLocation =  v.findViewById(R.id.txt_location);



        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.map_key));
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        fetchLocation();


        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(LocationMapsBS.this);

        return v;
    }


    private void fetchLocation() {

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            this.fetchLocation();
          // return ;
        }else{
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        currentLocation = location;

                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (currentLocation != null){
            if ((currentLocation.getLongitude() == 0)|| (currentLocation.getLatitude() == 0)){
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            }else {
                final LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("I'm here")
                        .draggable(false);

                googleMap.setMyLocationEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                googleMap.addMarker(markerOptions);
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        return true;
                    }
                });

                getLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
            }
        }else{
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

    }

    private void getLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latitude,longitude,1);
            String landMark = addressList.get(0).getAddressLine(0);

            txtLocation.setText(landMark);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation();
                }
                break;
            case 0:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
