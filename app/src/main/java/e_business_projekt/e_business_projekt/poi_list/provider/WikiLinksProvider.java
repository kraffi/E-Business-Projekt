package e_business_projekt.e_business_projekt.poi_list.provider;


import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RaulVinhKhoa on 08.02.2016.
 */
public class WikiLinksProvider {

    private static final String TAG = "EBP.WikiLinksProvider";
    WikiLinksProviderCallback callback;

    PointOfInterest poi;
    String wikiQuery;
    String wikiLink;


    public WikiLinksProvider(WikiLinksProviderCallback callback, PointOfInterest poi) {
        this.callback = callback;
        this.wikiQuery = poi.getName().replace(" ", "%20");
        this.poi = poi;
    }

    public void start(){
        new StartAsyncWikiQuery().execute();
    }

    private String getWikiLink(String query){

        try {

            URL url = new URL("https://de.wikipedia.org/w/api.php?action=opensearch&limit=1&namespace=0&format=json&search=" + query);
            //Log.i(TAG, "Wikipedia Query " + query + " URL: " + url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");

            BufferedReader br = new BufferedReader(in);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(sb.toString()).getAsJsonArray();

            String link = jsonArray.get(3).toString();

            if (link.length() == 2 ){
                link = "";
            } else {
                link = link.substring(2, link.length()-2);
            }

            Log.i(TAG, "Wikipedialink :" + link);
            return link;

        } catch (IOException e) {
            Log.e(TAG, "Malformed URL Exception!");
            throw new RuntimeException(e);
        }
    }

    private class StartAsyncWikiQuery extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "StartAsyncQuery()");
            wikiLink = getWikiLink(wikiQuery.replace(" ", "%20"));
            poi.setWikiLink(wikiLink);
            return "StartAsyncQuery finished!";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "Get WikiLinks finished!");
            callback.wikiLinksProviderCallback(poi);
        }

        @Override
        protected void onPreExecute() {

        }
    }
}
