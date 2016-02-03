package e_business_projekt.e_business_projekt.poi_list.provider;

import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 15.01.2016.
 */
public class PlacesProvider {

    private static final String TAG = "EBP.PlacesProvider";
    private ArrayList<PointOfInterest> poiList = new ArrayList<>();

    PlacesProviderCallback ppc;
    GoogleApiClient googleApiClient;
    POIFilter filter;
    Location location;

    int radius;
    String type;

    //Query String
    String urlString;

    // constructor
    public PlacesProvider(PlacesProviderCallback callback, GoogleApiClient googleApiClient, POIFilter filter, Location location) {
        this.ppc = callback;
        this.googleApiClient = googleApiClient;
        this.location = location;
        this.filter = filter;

        this.radius = filter.getRadius();

        int numberOfTypes = filter.getFilterTypes().size();
        if (numberOfTypes == 0){
            type = "null";
        } else {
            type = TextUtils.join("|", filter.getFilterTypes());
        }

        double latitude = this.location.getLatitude();
        double longitude = this.location.getLongitude();

        String name = "";
        String key = "AIzaSyAwNzfpWJnTxtOVlqkKT3M1P9f9-pTqGuw";

        //composing query parts
        String URL_BASE = "https://maps.googleapis.com/maps/api/place/";
        String LOCATION = "location=" + latitude + "," + longitude;
        String RADIUS = "&radius=" + radius;
        //String SEARCH_MODE = "nearbysearch/json?";
        String SEARCH_MODE = "radarsearch/json?";
        String TYPE = "&type=" + type;
        String NAME = "&name=" + name;
        String KEY = "&key=" + key;

        // composing query url
        urlString = URL_BASE + SEARCH_MODE + LOCATION + RADIUS + TYPE + NAME + KEY;
    }

    public void start(){
        new StartAsyncQuery().execute();
    }

    private void getNearbyPOIs(List<String> idList){
        Log.i(TAG, "getNearbyPOIs(): started");
        String[] idArray = idList.toArray(new String[idList.size()]);

        if(idList.size() != 0){
            if(googleApiClient != null){
                PendingResult<PlaceBuffer> result = Places.GeoDataApi.getPlaceById(googleApiClient, idArray);
                result.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        poiList.clear();
                        for (Place place :places){
                            PointOfInterest poi = new PointOfInterest(place);

                            //determine distance to last location
                            Location poiLocation = new Location("");
                            poiLocation.setLatitude(poi.getLatLng().latitude);
                            poiLocation.setLongitude(poi.getLatLng().longitude);

                            Location currentLocation = location;

                            poi.setDistance(currentLocation.distanceTo(poiLocation));

                            poiList.add(poi);
                        }
                        Log.i(TAG, "getNearbyPOIs(): " + poiList.size() + " POIs added");

                        //sort pois by distance
                        Collections.sort(poiList, new Comparator<PointOfInterest>() {
                            @Override
                            public int compare(PointOfInterest poi1, PointOfInterest poi2) {
                                return (int) (poi1.getDistance() - poi2.getDistance());
                            }
                        });

                        ppc.placesProviderCallback(poiList);
                    }
                });
            }
        } else {
            poiList.clear();
            ppc.placesProviderCallback(poiList);
        }
    }

    private List<String>  getIDs() {
        Log.i(TAG, "getIDs(): getting IDs from Places... " );
        List<String> idList = new ArrayList<>();
        try {
            // Create URL and opens connection to URL
            // URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.5243700,13.4105300&radius=500&types=food&name=a&key=AIzaSyAwNzfpWJnTxtOVlqkKT3M1P9f9-pTqGuw");
            URL url = new URL(urlString);
            Log.i(TAG, "Query URL: " + urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");

            BufferedReader br = new BufferedReader(in);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            JSONObject jsonResults = new JSONObject(sb.toString());
            JSONArray results = jsonResults.getJSONArray("results");

            for (int i = 0; i < results.length(); i++){
                String id = results.getJSONObject(i).getString("place_id");
                idList.add(id);
                //Log.d(TAG, "Result " + i + ": " + id);
            }
            Log.i(TAG, "getIDs(): getting IDs from Places finished");
            return idList;

        } catch (IOException e) {
            Log.e(TAG, "Malformed URL Exception!");
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private class StartAsyncQuery extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "StartAsyncQuery()");
            getNearbyPOIs(getIDs());
            return "StartAsyncQuery finished!";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "Get IDs from Places finished!");
        }

        @Override
        protected void onPreExecute() {

        }
    }
}
