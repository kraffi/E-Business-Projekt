package e_business_projekt.e_business_projekt;

import android.location.Location;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 15.01.2016.
 */
public class SearchPlacesService {

    private static final String TAG = "EBP.SearchPlacesService";
    private List<String> idList = new ArrayList<>();

    double latidude = 52.5243700;
    double longitude = 13.4105300;
    int radius = 500;
    String type = "";
    String name = "";
    String key = "AIzaSyAwNzfpWJnTxtOVlqkKT3M1P9f9-pTqGuw";

    // URL Parts
    final String URL_BASE = "https://maps.googleapis.com/maps/api/place/";
    final String SEARCH_MODE = "nearbysearch/json?";
    final String LOCATION = "location=" + latidude + "," + longitude;
    final String RADIUS = "&radius=" + radius;
    final String TYPE = "&type=" + type;
    final String NAME = "&name=" + name;
    final String KEY = "&key=" + key;

    public List<String>  getIDs() {
        Log.i(TAG, "getIDs() called");
        idList.clear();

        try {
            // Create URL and opens connection to URL
            //URL url = new URL("http://www.tixik.com/api/nearby?lat=52.519171163930377&lng=13.40609125996093&limit=20&key=demo");
            //URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.5243700,13.4105300&radius=500&types=food&name=a&key=AIzaSyAwNzfpWJnTxtOVlqkKT3M1P9f9-pTqGuw");

            String urlString = URL_BASE + SEARCH_MODE + LOCATION + RADIUS + TYPE + NAME + KEY;
            URL url = new URL(urlString);
            Log.i(TAG, "Query URL: " + urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // only if in main thread TODO: AsyncTask!
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

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
                String name = results.getJSONObject(i).getString("name");
                String id = results.getJSONObject(i).getString("place_id");
                idList.add(id);
                Log.d(TAG, "Result " + i + ": " + name + " | " + id);
            }

            return idList;

        } catch (IOException e) {
            Log.e(TAG, "Malformed URL Exception!");
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
