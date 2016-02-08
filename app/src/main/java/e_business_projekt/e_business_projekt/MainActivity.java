package e_business_projekt.e_business_projekt;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import e_business_projekt.e_business_projekt.adapter.UriAdapter;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.DataBaseCallback;
import e_business_projekt.e_business_projekt.route_list.DataBaseProvider;
import e_business_projekt.e_business_projekt.route_list.POIRoute;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;
import e_business_projekt.e_business_projekt.route_list.adapter.RouteListViewItemAdapter;
import e_business_projekt.e_business_projekt.route_list.adapter.RouteListViewItemCallback;
import e_business_projekt.e_business_projekt.route_list.dialogs.EditRouteDialog;
import e_business_projekt.e_business_projekt.route_list.dialogs.EditRouteDialogCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RouteListViewItemCallback, EditRouteDialogCallback,
        DataBaseCallback {

    private static final String TAG = "EBP.MainActivity";

    //Wikitude variables
    public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
    public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
    public static final String EXTRAS_KEY_ACTIVITIES_TILES_ARRAY = "activitiesTitles";
    public static final String EXTRAS_KEY_ACTIVITIES_CLASSNAMES_ARRAY = "activitiesClassnames";
    public static final String EXTRAS_KEY_ACTIVITY_IR = "activityIr";
    public static final String EXTRAS_KEY_ACTIVITY_GEO = "activityGeo";
    public static final String EXTRAS_KEY_ACTIVITIES_ARCHITECT_WORLD_URLS_ARRAY = "activitiesArchitectWorldUrls";

    boolean hasIr = true;
    boolean hasGeo = true;
    private POIRouteProvider routeManager = POIRouteProvider.getInstance();
    private DataBaseProvider dataBaseManager = DataBaseProvider.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<POIRoute> POIRouteList = routeManager.getPOIRouteList();
        dataBaseManager.setCallback(this);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addRouteButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "addRouteButton clicked");
                routeManager.addRoute();
                buildRouteList(routeManager.getPOIRouteList());
            }
        });
        login();
        buildRouteList(POIRouteList);

    }

    //TODO: Replace with real Login
    public void login(){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("userurl", "www.google.com");
        startActivity(intent);

        boolean loggedIn = true;
        if (loggedIn){
            dataBaseManager.readData();
        }
    }

    public void buildRouteList(final List<POIRoute> poiRouteList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView lv = (ListView) findViewById(R.id.routeListView);
                final TextView empty = (TextView) findViewById(R.id.emptyRouteListView);

                if (!poiRouteList.isEmpty()){
                    empty.setVisibility(View.INVISIBLE);
                    lv.setAdapter(new RouteListViewItemAdapter(MainActivity.this, MainActivity.this, poiRouteList));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i(TAG, "Item " + position + " clicked and activated");
                            view.setBackgroundResource(R.color.routeSelected);
                            routeManager.setActivated(position);

                            int listLength = lv.getCount();
                            for (int i = 0; i < listLength; i++){
                                if (!(i == position)){
                                    View v = getViewByPosition(i, lv);
                                    v.setBackgroundResource(R.color.routeUnselected);
                                }
                            }
                        }
                    });
                }  else {
                    Log.i(TAG, "Show No Results");
                    empty.setVisibility(View.VISIBLE);
                    lv.setAdapter(new RouteListViewItemAdapter(MainActivity.this, MainActivity.this, poiRouteList));
                }
            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void editRouteButtonCallback(int position) {
        Log.i(TAG, "editRouteButtonCallback() with Item " + position);

        // Set route and activated Route
        routeManager.setActivated(position);
        POIRoute route = routeManager.getPOIRouteList().get(position);

        // Set arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList("poiList", route.getPoiRoute());
        args.putString("routeName", route.getRouteName());
        args.putInt("position", position);

        // Create dialog and pass arguments
        EditRouteDialog dialog = new EditRouteDialog();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "Edit Route Dialog");

        buildRouteList(routeManager.getPOIRouteList());
    }

    @Override
    public void deleteRouteButtonCallback(int position) {
        Log.i(TAG, "editRouteButtonCallback() with Item " + position);
        routeManager.deleteRoute(position);
        buildRouteList(routeManager.getPOIRouteList());
    }

    @Override
    public void editRouteDialogOkButtonCallback(String name, List<PointOfInterest> poiList, int position) {
        Log.i(TAG, "editRouteDialogOKCallback(): edit " + name + " at position " + position);
        routeManager.editRouteName(name, position);
        buildRouteList(routeManager.getPOIRouteList());
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
            case R.id.action_poi_list:
                intent = new Intent(this, PoiListActivity.class);
                break;
            case R.id.action_map:
                intent = new Intent(this, MapActivity.class);
                break;
            case R.id.action_cam:
                intent = new Intent(this, CamActivity.class);
                //intent.putExtra(EXTRAS_KEY_ACTIVITIES_ARCHITECT_WORLD_URLS_ARRAY, activityUrl);
                //intent.putExtra(EXTRAS_KEY_ACTIVITIES_CLASSNAMES_ARRAY, activityClasses);
                //intent.putExtra(EXTRAS_KEY_ACTIVITIES_TILES_ARRAY, activityTitles);
                //intent.putExtra(EXTRAS_KEY_ACTIVITY_TITLE_STRING, activityTitle);
                intent.putExtra(EXTRAS_KEY_ACTIVITY_IR, hasIr);
                intent.putExtra(EXTRAS_KEY_ACTIVITY_GEO, hasGeo);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void readDataBaseCallback() {
        buildRouteList(routeManager.getPOIRouteList());
    }
}
