package e_business_projekt.e_business_projekt;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;
import e_business_projekt.e_business_projekt.route_list.POIRoute;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;
import e_business_projekt.e_business_projekt.route_list.adapter.RouteListViewItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EBP.MainActivity";
    private ArrayList<POIRoute> POIRouteList = new ArrayList<>();
    private POIRouteProvider routeManager = new POIRouteProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        POIRouteList = routeManager.getPOIRouteList();

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addRouteButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "addRouteButton clicked");
                routeManager.addRoute();
                buildRouteList(routeManager.getPOIRouteList());
            }
        });
        buildRouteList(POIRouteList);
    }

    public void buildRouteList(final List<POIRoute> poiRouteList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView lv = (ListView) findViewById(R.id.routeListView);
                if (!poiRouteList.isEmpty()){
                    lv.setAdapter(new RouteListViewItemAdapter(MainActivity.this, poiRouteList));
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
