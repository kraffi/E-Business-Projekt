package e_business_projekt.e_business_projekt;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.*;

import java.util.ArrayList;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "EBP.MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<PointOfInterest> poiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize GoogleApiClient
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        SearchPlacesService test = new SearchPlacesService();
        test.getIDs();

        getPoiList();
    }

    private void getPoiList() {
        if (mGoogleApiClient != null) {

//            create filter for results
//            List<Integer> filterTypes = new ArrayList<>();
//            filterTypes.add(Place.TYPE_ART_GALLERY);
//            filterTypes.add(Place.TYPE_MUSEUM);
//
//            List<String> test = new ArrayList<>();
//            test.add("ChIJre7qb9YjZUERKUDdeKxvZDI");
//            PlaceFilter placeFilter = new PlaceFilter(false, test);
//
//            Test
//            AutocompleteFilter autocompleteFilter = AutocompleteFilter.create(filterTypes);
//            LatLng a = new LatLng(55.196354, 14.451385);
//            LatLng b = new LatLng(46.866110, 4.409882);
//            PendingResult<AutocompletePredictionBuffer> pendingResult = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, "Brandenburger Tor", new LatLngBounds(b,a),autocompleteFilter );
//            test END
//
//############AUTO COMPLETE PREDICTION##################################################################################
//            pendingResult.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
//                @Override
//                public void onResult(AutocompletePredictionBuffer autocompletePredictions) {
//
//                    ArrayList<String> x = new ArrayList<>();
//                    for (AutocompletePrediction predictions : autocompletePredictions){
//                        x.add(predictions.getPlaceId());
//                    }
//                    String[] y = new String[x.size()];
//                    y = x.toArray(y);
//                    PendingResult<PlaceBuffer> z = Places.GeoDataApi.getPlaceById(mGoogleApiClient, y);
//                    z.setResultCallback(new ResultCallback<PlaceBuffer>() {
//                        @Override
//                        public void onResult(PlaceBuffer places) {
//                            for (Place p : places){
//                                Log.d(TAG, "TESTI: " + p.getName());
//                                Log.d(TAG, "TESTI: " + p.getId());
//                            }
//                        }
//                    });
//                }
//            });
//######################################################################################################################


            // HTTP Request Test


            // HTTP Request Test end
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                    poiList.clear();
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    Log.i(TAG, "Getting results from GooglePlacesBuffer: " + likelyPlaces.toString());
                    int i = 0;
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        i++;
                        //create new poi to add to POI list
                        PointOfInterest poi = new PointOfInterest(placeLikelihood.getPlace());

                        //determine distance to last location
                        Location poiLocation = new Location("");
                        poiLocation.setLatitude(poi.getLatLng().latitude);
                        poiLocation.setLongitude(poi.getLatLng().longitude);
                        poi.setDistance(lastLocation.distanceTo(poiLocation));

                        // add POI to POI-List
                        poiList.add(poi);
                        //Log.i(TAG, "Place: " + placeLikelihood.getPlace().getName() + " as number " + i + " added!");
                    }
                    likelyPlaces.release();

                    if (poiList.isEmpty()) {
                        Toast.makeText(getBaseContext(), "POI-List is empty!", Toast.LENGTH_LONG).show();
                    }

                    showCallbackToast(poiList);
                    buildPOIList(poiList);
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "No GoogleApiClient", Toast.LENGTH_SHORT).show();
        }
    }

    public void buildPOIList(ArrayList<PointOfInterest> placesPOIList) {
        ListView lv = (ListView) findViewById(R.id.poiListView);
        lv.setAdapter(new ListViewItemAdapter(this, placesPOIList));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String info = poiList.get(position).toString();
                Log.i(TAG, "Called: onItemClick(): Item number " + position);
                Toast.makeText(getBaseContext(), info, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showCallbackToast(ArrayList<PointOfInterest> poiList) {
        String text = poiList.size() + " POIs added to List";
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_map:
                intent = new Intent(this, MapActivity.class);
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



    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleApiClient: Connection suspended!");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "GoogleApiClient: Connected!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient: Connection failed!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}