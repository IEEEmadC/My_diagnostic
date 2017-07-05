package org.dev4u.hv.my_diagnostic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.dev4u.hv.my_diagnostic.MyPlacesUI.NearbyPlaces;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.Place;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesException;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesListener;

import java.util.List;

import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class MapFragment extends Fragment implements OnMapReadyCallback, PlacesListener,LocationListener {

    MapView mMapView;
    private GoogleMap mMap;
    CameraPosition mCameraPosition;

    //TODO esta seria la ubicacion default
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    private static final long MIN_TIME = 500;//miliseconds
    private static final float MIN_DISTANCE = 5;//meters
    private static final float DEFAULT_ZOOM = 15;

    LocationManager locationManager;

    MarkerOptions markerOptions;
    Marker marker;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.activity_maps, container,
                false);


        //setting the map and the marker

        markerOptions = new MarkerOptions().position(new LatLng(mDefaultLocation.latitude, mDefaultLocation.longitude)).title("My Current position");

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));



        locationManager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);
        initAll();
        return v;
    }

    private void initAll(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mMapView.onResume();// needed to get the map to display immediately
                initMap();
                initLocation();
            }
        });
    }

    private void initMap(){
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapsInitializer.initialize(this.getContext());

        mMapView.getMapAsync(this);
    }

    private void initLocation(){

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        marker = this.mMap.addMarker(markerOptions);
        //TODO esta seria la ubicacion sin internet
        if(mLastKnownLocation==null){
            mCameraPosition = new CameraPosition.Builder().target(new LatLng(mDefaultLocation.latitude, mDefaultLocation.longitude)).zoom(DEFAULT_ZOOM).build();
            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
        /*

        new NearbyPlaces.Builder()
                .listener((PlacesListener) this)//<ERROR>
                .key("KEY")
                .latlng(latitude, -longitude)
                .radius(1500)
                .keyword("clinic")
                .build()
                .execute();

        mCameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        this.mMap.addMarker(markerOptions);
        */
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        Log.i("PlacesAPI", "onPlacesSuccess()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Place place : places) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(place.getName())
                            .snippet(place.getVicinity()));
                }

            }
        });

    }

    @Override
    public void onPlacesFinished() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
        if(mMap!=null && marker!=null) {
            LatLng islamabad = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            marker.setPosition(islamabad);
            new NearbyPlaces.Builder()
                    .listener((PlacesListener) this)//<ERROR>
                    .key("KEY")
                    .latlng(islamabad.latitude,islamabad.longitude)
                    .radius(1500)
                    .keyword("clinic")
                    .build()
                    .execute();
            //TODO esto mueve la camara
            float zoom = (mMap.getCameraPosition().zoom>DEFAULT_ZOOM)? DEFAULT_ZOOM : mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(islamabad, zoom));
            Toast.makeText(getContext(),"Cambio a ubicacion : " + location.getLatitude() + " "+ location.getLongitude(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}