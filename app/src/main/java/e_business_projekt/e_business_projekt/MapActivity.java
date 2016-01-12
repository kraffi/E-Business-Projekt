package e_business_projekt.e_business_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static Double latitude, longitude;
    public static final String LOGTAG = "XploCity-MapSection: ";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Passing harcoded values for latitude & longitude for Berlin. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 52.5243700;
        longitude = 13.4105300;

        /*if(container == null){
            return null;
        }
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        // Passing harcoded values for latitude & longitude for Berlin. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 52.5243700;
        longitude = 13.4105300;

        setUpMapIfNeeded();
        Log.d(LOGTAG, "Map loaded and supplied with hardcoded values.");

        return view;*/
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker Berlin"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }

    /*public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        setUpMapIfNeeded();
    }*/

    /*public void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) ().findFragmentById(R.id.map))
                    .getMapAsync(this);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        //kr: handle presses on the action menu items
        switch (item.getItemId()) {
            case R.id.action_main:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.action_cam:
                intent = new Intent(this, CamActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }
}
