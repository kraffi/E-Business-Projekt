package e_business_projekt.e_business_projekt;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    /*public static AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    public static FragmentManager fragmentManager;*/
    private GoogleApiClient mGoogleApiClient;

    // Viewpager that will display the several sections of the app, one at a time
    //ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rn: google places stuff --------------------------------------------------------------------------------------

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                Log.d("EBP", "onResult called");
                Log.d("EBP", "Buffer: " + likelyPlaces.toString());
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.d("EBP", "Places: " + placeLikelihood.getPlace().getName());
                    Log.i("EBP", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }

        });

        Log.d("EBP", mGoogleApiClient.toString());
        //rn: end google places stuff ----------------------------------------------------------------------------------
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

        /*mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        fragmentManager = getSupportFragmentManager();

        final ActionBar actionbar = getActionBar();

        //deactivate home button since there is no parent
        actionbar.setHomeButtonEnabled(false);

        //display tabs in the actionbar
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //viewpager that displays the different views
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionbar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionbar.addTab(
                    actionbar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }*/

   /* @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }*/

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("EBP", "GoogleApiClient: Connected!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("EBP", "GoogleApiClient: Connection failed!");
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

    /*
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    /*public static class AppSectionsPagerAdapter extends FragmentPagerAdapter{

        public AppSectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new ListViewSection();

                case 1:
                    return new ScanSection();

                default:
                    return new MapSection();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            String title = "";
            if (position + 1 == 1) {
                title = "Sights in reach";
            }
            else if (position + 1 == 2) {
                title = "Scanner";
                }
            else if (position + 1 == 3){
                title = "Map";
            }
            return title;
        }
    }*/
}
