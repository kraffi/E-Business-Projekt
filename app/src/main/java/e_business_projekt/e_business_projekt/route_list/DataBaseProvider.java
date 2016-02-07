package e_business_projekt.e_business_projekt.route_list;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.*;
import e_business_projekt.e_business_projekt.adapter.UriAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoDatabase;

/**
 * Created by RaulVinhKhoa on 06.02.2016.
 */
public class DataBaseProvider {

    private static final String TAG = "EBP.DataBaseProvider";
    private String jsonString;
    private String userID;
    private Gson gson;

    private String query;

    public DataBaseProvider() {

        gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriAdapter()).create();
        userID = POIRouteProvider.getInstance().getUserID();
        query = "&q={\"userID\":\"" + userID + "\"}";

        readData();
        //createData();
    }

    public void createData(){
        POIRouteProvider routeManager = POIRouteProvider.getInstance();

        jsonString = gson.toJson(routeManager);
        Log.i(TAG, "INPUT: " + jsonString);

        new createQuery().execute();

    }

    public void readData(){
        new readQuery().execute();
    }

    private class readQuery extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "BACKGROUND!");

            try {

                URL url = new URL("https://api.mongolab.com/api/1/databases/explocity_database/collections/data?apiKey=Hg6Dr44IiwAGXwJirCdGbe5kJ4j9HaQr" + query);
                Log.i(TAG, "Query URL: " + url.toString());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");

                BufferedReader br = new BufferedReader(in);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }

                Log.i(TAG, "GET: " + sb.toString());

                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(sb.toString()).getAsJsonArray();

                if (jsonArray.size() == 0){
                    createData();
                    Log.i(TAG, "No User! Create new User/Data");
                } else {
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    POIRouteProvider.getInstance().setInstance(gson.fromJson(jsonObject.toString(), POIRouteProvider.class));
                    Log.i(TAG, jsonObject.toString());
                }
            } catch (IOException e) {
                Log.e(TAG, "Malformed URL Exception!");
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "POST EXECUTE!");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "PRE EXECUTE!");
        }
    }

    private class createQuery extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://api.mongolab.com/api/1/databases/explocity_database/collections/data?apiKey=Hg6Dr44IiwAGXwJirCdGbe5kJ4j9HaQr");
                Log.i("TAG", "Create Query URL: " + url.toString());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(jsonString);
                wr.flush();

                int HttpResult = connection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    Log.i(TAG, "Success: " + connection.getResponseMessage());
                }else{
                    Log.i(TAG, "Fail: " + connection.getResponseMessage());
                }

            } catch (IOException e) {
                Log.e(TAG, "Malformed URL Exception!");
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "POST EXECUTE!");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "PRE EXECUTE!");
        }
    }

    // TODO If user exist = normal getrequest, if not post request;


}
