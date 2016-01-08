package e_business_projekt.e_business_projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sebastian on 18.11.2015.
 */
public class MapSection extends Fragment implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private static Double latitude, longitude;
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String LOGTAG = "XploCity-MapSection: ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        if(container == null){
            return null;
        }
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        // Passing harcoded values for latitude & longitude for Berlin. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 52.5243700;
        longitude = 13.4105300;

        setUpMapIfNeeded();
        Log.d(LOGTAG, "Map loaded and supplied with hardcoded values.");

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        setUpMapIfNeeded();
    }

    public void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap mMap) {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }
}
