package org.dev4u.hv.my_diagnostic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.dev4u.hv.my_diagnostic.MyPlacesUI.NearbyPlaces;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.Place;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesException;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesListener;

import java.util.List;

import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class MapFragment extends Fragment implements  OnMapReadyCallback,PlacesListener {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.activity_maps, container,
                false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapsInitializer.initialize(this.getContext());

        mMapView.getMapAsync(this);

        // Perform any camera updates here
        return v;
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

        this.googleMap = googleMap;

        MapsInitializer.initialize(getContext());

        // latitude and longitude
        double latitude = 13.730556;
        double longitude = -89.718956;
        new NearbyPlaces.Builder()
         .listener((PlacesListener) this)//<ERROR>
         .key("KEY")
         .latlng(13.730556, -89.718956)
         .radius(1500)
         .keyword("clinic")
         .build()
         .execute();

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();

        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        this.googleMap.addMarker(marker);
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

                    googleMap.addMarker(new MarkerOptions().position(latLng)

                            .title(place.getName())

                            .snippet(place.getVicinity()));



                }

            }
        });

    }

    @Override
    public void onPlacesFinished() {

    }
}