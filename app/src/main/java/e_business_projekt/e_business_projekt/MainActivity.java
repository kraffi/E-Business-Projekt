package e_business_projekt.e_business_projekt;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import e_business_projekt.e_business_projekt.maps_navigation.MapActivity;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.POIRoute;
import e_business_projekt.e_business_projekt.route_list.adapter.RouteListViewItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EBP.MainActivity";
    private ArrayList<POIRoute> POIRouteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------------- Creating Test Data -----------------------
        PointOfInterest poi1 = new PointOfInterest();
        poi1.setName("Siegessäule");
        poi1.setId("TEST ID Siegessäule");
        poi1.setDistance(200);
        poi1.setLatLng(new LatLng(52.51454340000001, 13.3501189));
        poi1.setAddress("An der Siegessäule!");
        poi1.setPhonenumber("03012345");
        poi1.addPlaceType(1234);
        poi1.addPlaceType(12);

        PointOfInterest poi2 = new PointOfInterest();
        poi2.setName("Potsdamer Platz");
        poi2.setId("TEST ID Potsdamer Platz");
        poi2.setDistance(100);
        poi2.setLatLng(new LatLng(52.5096488, 13.3759441));
        poi2.setAddress("Am Potsdamer Platz!");
        poi2.setPhonenumber("030111111");
        poi2.addPlaceType(4321);
        poi2.addPlaceType(43);


        PointOfInterest poi3 = new PointOfInterest();
        poi3.setName("Brandenburger Tor");
        poi3.setId("TEST ID Brandenburger Tor");
        poi3.setDistance(300);
        poi3.setLatLng(new LatLng(52.5162746, 13.377704));
        poi3.setAddress("Am Brandenburger Tor!");
        poi3.setPhonenumber("0309999999999999999");
        poi3.addPlaceType(9876);
        poi3.addPlaceType(98);

        PointOfInterest poi4 = new PointOfInterest();
        poi4.setName("Fernsehturm");
        poi4.setId("TEST ID Fernsehturm");
        poi4.setDistance(3);
        poi4.setLatLng(new LatLng(52.5162746, 13.377704));
        poi4.setAddress("Am Fernsehturm!");
        poi4.setPhonenumber("030 6x die 6");
        poi4.addPlaceType(4567);
        poi4.addPlaceType(45);

        PointOfInterest poi5 = new PointOfInterest();
        poi5.setName("Andels");
        poi5.setId("TEST ID Andels");
        poi5.setDistance(3);
        poi5.setLatLng(new LatLng(52.5283906, 1313.4570176));
        poi5.setAddress("Am Andels irgendwo Landsberger Allee!");
        poi5.setPhonenumber("030 6x die 6");
        poi5.addPlaceType(1111);
        poi5.addPlaceType(11);

        POIRoute POIRoute1 = new POIRoute();
        POIRoute1.addPOI(poi1);
        POIRoute1.addPOI(poi2);
        POIRoute1.addPOI(poi3);

        POIRoute POIRoute2 = new POIRoute();
        POIRoute2.addPOI(poi3);
        POIRoute2.addPOI(poi4);
        POIRoute2.addPOI(poi5);

        POIRoute POIRoute3 = new POIRoute();
        POIRoute3.addPOI(poi1);
        POIRoute3.addPOI(poi2);
        POIRoute3.addPOI(poi3);
        POIRoute3.addPOI(poi4);
        POIRoute3.addPOI(poi5);

        POIRoute1.setRouteName("Party POIRoute");
        POIRouteList.add(POIRoute1);
        POIRoute2.setRouteName("Langweilige POIRoute");
        POIRouteList.add(POIRoute2);
        POIRoute3.setRouteName("Die Geile POIRoute");
        POIRouteList.add(POIRoute3);

        //----------------------- Test Data End -----------------------
        buildRouteList(POIRouteList);
    }

    public void buildRouteList(final List<POIRoute> POIRouteList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView lv = (ListView) findViewById(R.id.routeListView);
                if (!POIRouteList.isEmpty()){
                    lv.setAdapter(new RouteListViewItemAdapter(MainActivity.this, POIRouteList));
                }  else {
                    Log.i(TAG, "Show No Results");
                }
            }
        });
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
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }
}
