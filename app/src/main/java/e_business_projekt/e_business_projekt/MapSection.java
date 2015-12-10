package e_business_projekt.e_business_projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sebastian on 18.11.2015.
 */
public class MapSection extends Fragment {

    private static View view;

    private static GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        if(container == null){
            return null;
        }
        view = (RelativeLayout) inflater.inflate(R.layout.activity_maps, container, false);

        // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 25.00;
        longitude = 75.00;

        setUpMapIfNeeded();

        return view;
    }

    public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if(mMap == null){
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment)MainActivity.fragmentManager.findFragmentById(R.id.map)).getMap();
            //Check if we were successfull in obtaining  the map
            if(mMap != null){
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    private static void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Home").snippet("HomeAddress"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        if(mMap != null){
            setUpMap();
        }

        if(mMap == null){
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) MainActivity.fragmentManager.findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if(mMap != null){
                setUpMap();
            }
        }
    }

    public void onDestroy(){
        super.onDestroyView();
        if(mMap != null){
            MainActivity.fragmentManager.beginTransaction().remove(MainActivity.fragmentManager.findFragmentById(R.id.map)).commit();
            mMap = null;
        }
    }
}
