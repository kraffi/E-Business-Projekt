package e_business_projekt.e_business_projekt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import e_business_projekt.e_business_projekt.CamActivity;
import e_business_projekt.e_business_projekt.MainActivity;
import e_business_projekt.e_business_projekt.PoiListActivity;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.map_navigation.AbstractRouting;
import e_business_projekt.e_business_projekt.map_navigation.PlaceAutoCompleteAdapter;
import e_business_projekt.e_business_projekt.map_navigation.Route;
import e_business_projekt.e_business_projekt.map_navigation.Routing;
import e_business_projekt.e_business_projekt.map_navigation.RoutingListener;
import e_business_projekt.e_business_projekt.map_navigation.Util;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.POIRoute;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;

public class MapActivity extends AppCompatActivity implements RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    protected GoogleMap map;

    protected LatLng start;
    protected LatLng end;
    private ArrayList<LatLng> route1;
    private List<PointOfInterest> POIList;

    private String poi_id;
    private String poi_name;
    private double poi_lat;
    private double poi_long;

    LocationManager locationManager;
    /*@InjectView(R.id.start)
    AutoCompleteTextView starting;
    @InjectView(R.id.destination)
    AutoCompleteTextView destination;
    @InjectView(R.id.send)
    ImageView send;*/
    private String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private Location location;
    private PlaceAutoCompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Polyline> polylines;
    private int[] colors = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};

    public static final String EXTRAS_KEY_ACTIVITY_IR = "activityIr";
    public static final String EXTRAS_KEY_ACTIVITY_GEO = "activityGeo";
    boolean hasIr = true;
    boolean hasGeo = true;

    private static final LatLngBounds BOUNDS_JAMAICA= new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));

    private static Double latitude, longitude;

    public LatLng getStart(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * This activity loads a map and then displays the route and pushpins on it.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab_start_wikitude);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EXPLOCITY", "start_wikitude");

                //todo KR: 07.02.16: create JSON-string in .js file for wikitude
                int activated_route = POIRouteProvider.getInstance().getActivated();
                POIList = POIRouteProvider.getInstance().getPOIRouteList().get(activated_route).getPoiRoute();

                JSONObject json_poi = new JSONObject();
                JSONObject json_route = new JSONObject();

                for(PointOfInterest poi : POIList){

                    poi_id = poi.getId();
                    poi_name = poi.getName();
                    poi_lat = poi.getLatLng().latitude;
                    poi_long = poi.getLatLng().longitude;

                    try {
                        json_poi.put("id", poi_id);
                        json_poi.put("name", poi_name);
                        json_poi.put("latitude", poi_lat);
                        json_poi.put("longitude", poi_long);
                        json_poi.put("altitude", "100.0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        json_route.accumulate("route", json_poi);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }



                Log.d("EXPLOCITY", "json_route: " + json_route);

                Intent intent;
                intent = new Intent(MapActivity.this, CamActivity.class);
                intent.putExtra(EXTRAS_KEY_ACTIVITY_IR, hasIr);
                intent.putExtra(EXTRAS_KEY_ACTIVITY_GEO, hasGeo);
                startActivity(intent);
            }
        });

        polylines = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        map = mapFragment.getMap();

        // Passing harcoded values for latitude & longitude for Berlin. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 52.5243700;
        longitude = 13.4105300;

        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_JAMAICA, null);


        /*
        * Updates the bounds being used by the auto complete adapter based on the position of the
        * map.
        * */
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                mAdapter.setBounds(bounds);
            }
        });


        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);
        map.animateCamera(zoom);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("EXPLOCITY", "start fetching location via network");

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        map.moveCamera(center);
                        map.animateCamera(zoom);
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
                });

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("EXPLOCITY", "start fetching location via GPS");
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        map.moveCamera(center);
                        map.animateCamera(zoom);

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
                });
    }

    @Override
    public void onRoutingFailure() {
        // The Routing request failed
        progressDialog.dismiss();
        Toast.makeText(this,"Something went wrong, Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex)
    {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add POIRoute(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % colors.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(colors[colorIndex]));
            polyOptions.width(10 + i * 10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"POIRoute "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

        //set marker for all the POIs in the list
        for(LatLng poi : route1){
            MarkerOptions options = new MarkerOptions();
            options.position(poi);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_72));
            map.addMarker(options);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_TAG,connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("EXPLOCITY", "location onConnected: " + location);

        //starting point of every route
        start = new LatLng(location.getLatitude(), location.getLongitude());

        /*middle1 = new LatLng(52.51454340000001,13.3501189);
        middle2 = new LatLng(52.5096488,13.3759441);
        end= new LatLng(52.5162746,13.377704);*/

        //create an array-list with all POIs of the route
        route1 = new ArrayList();
        int activated_route = POIRouteProvider.getInstance().getActivated();
        POIList = POIRouteProvider.getInstance().getPOIRouteList().get(activated_route).getPoiRoute();

        //add the current location as the starting point of the route
        route1.add(start);

        for(PointOfInterest poi : POIList){
            route1.add(poi.getLatLng());
        }
        Log.d("EXPLOCITY", "route1: " + route1);

        Log.d("EXPLOCITY", "POIList: " + POIList);

        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Fetching POIRoute information.", true);
        Log.d("EXPLOCITY", "start: " + start);
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(route1)
                .build();
        routing.execute();

    }

    @Override
    public void onConnectionSuspended(int i) {

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
            case R.id.action_poi_list:
                intent = new Intent(this, PoiListActivity.class);
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
