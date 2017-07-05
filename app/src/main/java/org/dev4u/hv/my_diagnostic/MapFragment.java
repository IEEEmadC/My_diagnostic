package  org.dev4u.hv.my_diagnostic;
import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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


public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        PlacesListener {
    private GoogleMap mMap;
    private static final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        Log.d(TAG, "onCreate");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapsInitializer.initialize(this.getContext());
        mMapView.getMapAsync(this);
        Log.d(TAG, "onCreateView");
        return rootView;
    }
    @Override
    public void onPlacesFailure(PlacesException e) {
    }
    @Override
    public void onPlacesStart() {

    }
    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mMapView.onResume();
        setUpMap();

        Log.d(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapView.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        else
            handleNewLocation(location);

        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);

        Log.d(TAG, "onLocationChanged");
    }

    private void setUpMap() {
        if (mGoogleMap == null)
            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                }
            });
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
    private void handleNewLocation(Location location) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng Miubicacion = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(Miubicacion).title("I am Here")
                .icon((BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Miubicacion, 15));

        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location
// Set listeners for marker events.  See the bottom of this class for their behavior.
        // mMap.setOnMarkerClickListener(this);
        //mMap.setOnInfoWindowClickListener(this);
        // Turn on the My Location layer and the related control on the map.

        new NearbyPlaces.Builder()
                .listener(this)
                .key("KEY")
                .latlng(location.getLatitude(),location.getLongitude())
                .radius(1500)
                .keyword("clinic")
                .build()
                .execute();
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    @Override
    public void onPlacesFinished() {

    }
}