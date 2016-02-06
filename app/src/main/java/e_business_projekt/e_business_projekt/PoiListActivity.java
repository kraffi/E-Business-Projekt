package e_business_projekt.e_business_projekt;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import e_business_projekt.e_business_projekt.poi_list.adapter.POIListViewItemAdapter;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIDialog;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIFilterDialog;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIFilterDialogCallback;
import e_business_projekt.e_business_projekt.poi_list.provider.POIFilter;
import e_business_projekt.e_business_projekt.poi_list.provider.PlacesProvider;
import e_business_projekt.e_business_projekt.poi_list.provider.PlacesProviderCallback;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;

import java.util.ArrayList;
import java.util.List;

public class PoiListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, PlacesProviderCallback, POIFilterDialogCallback {

    private POIRouteProvider routeManager = POIRouteProvider.getInstance();
    private static final String TAG = "EBP.PoiListActivity";

    //
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<PointOfInterest> poiList = new ArrayList<>();
    POIFilter filter;
    Location lastLocation;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_list);


        int a = routeManager.getActivated();
        Log.i("TEST:", routeManager.getPOIRouteList().get(a).getPoiRoute().toString());

        //initialize GoogleApiClient
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //initialize Filter
        filter = new POIFilter();

        searchView = (SearchView) findViewById(R.id.poiSearchView);
        //remove focus from SearchView
        searchView.clearFocus();
        searchView.setFocusable(false);
        searchView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.setFocusableInTouchMode(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.replace(" ", "%20");
                Log.i(TAG, "Searchbar Query: " + query);
                //remove focus from SearchView
                searchView.clearFocus();
                searchView.setFocusable(false);

                filter.setKeyword(query);
                startProvider(filter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ImageButton filterButton = (ImageButton) findViewById(R.id.poiFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "POI Filter Button clicked");
                //remove focus from SearchView
                searchView.clearFocus();
                searchView.setFocusable(false);

                Bundle args = new Bundle();
                args.putString("keyword", filter.getKeyword());
                args.putInt("radius", filter.getRadius());

                List<int[]> filterTypesList = filter.getFilterTypes();
                for (int i = 0; i < filterTypesList.size(); i++){
                    args.putIntArray("types_" + i, filterTypesList.get(i));
                }

                POIFilterDialog dialog = new POIFilterDialog();
                dialog.setArguments(args);
                dialog.onAttach(PoiListActivity.this);
                dialog.show(getFragmentManager(), "POI Filter Dialog");
            }
        });
    }


    //TODO: DEPRECATED
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
                        Log.i(TAG, "Place: " + placeLikelihood.getPlace().getName() + " as number " + i + " added!");
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

    public void buildPOIList(final List<PointOfInterest> placesPOIList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView lv = (ListView) findViewById(R.id.poiListView);
                if(!placesPOIList.isEmpty()){
                    lv.setAdapter(new POIListViewItemAdapter(PoiListActivity.this, placesPOIList));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            //remove focus from SearchView
                            searchView.clearFocus();
                            searchView.setFocusable(false);

                            PointOfInterest poi = placesPOIList.get(position);

                            Bundle args = new Bundle();
                            args.putParcelable("poi", poi);

                            POIDialog dialog = new POIDialog();
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "POI Dialog");

                            Log.i(TAG, "Called: onItemClick(): Item number " + position);
                        }
                    });
                } else {
                    Log.i(TAG, "Show No Results");
                }
            }
        });



    }

    public void showCallbackToast(List<PointOfInterest> poiList) {
        String text = poiList.size() + " POIs added to List";
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi_list, menu);
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
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startProvider(filter);
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

    @Override
    public void placesProviderCallback(List<PointOfInterest> pois) {
        Log.i(TAG,"Callback: " + pois.size());
        buildPOIList(pois);
    }

    @Override
    public void poiFilterDialogCallback(String query, List<int[]> filterTypes, int radius) {
        Log.i(TAG,"Callback Filter => Query: " + query + " | Types: "  + filterTypes.toString() + " | Radius: " + radius);
        filter = new POIFilter(query, filterTypes, radius);
        startProvider(filter);
    }

    public void startProvider(POIFilter filter){
        Location newLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (newLocation != null){
            lastLocation = newLocation;
        }
        PlacesProvider placesProvider = new PlacesProvider(this, mGoogleApiClient, filter, lastLocation);
        placesProvider.start();
    }
}