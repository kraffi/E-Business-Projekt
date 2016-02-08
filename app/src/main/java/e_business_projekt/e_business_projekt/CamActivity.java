package e_business_projekt.e_business_projekt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.CaptureScreenCallback;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.wikitude.architect.StartupConfiguration;
import com.wikitude.architect.StartupConfiguration.CameraPosition;

import org.json.JSONArray;
import org.json.JSONObject;

import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIDialog;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIFilterDialog;
import e_business_projekt.e_business_projekt.poi_list.provider.POIFilter;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;
import e_business_projekt.e_business_projekt.wikitude.AbstractArchitectCamActivity;
import e_business_projekt.e_business_projekt.wikitude.ArchitectViewHolderInterface;
import e_business_projekt.e_business_projekt.wikitude.LocationProvider;
import e_business_projekt.e_business_projekt.wikitude.PoiDetailActivity;
import e_business_projekt.e_business_projekt.wikitude.SampleCamFragment;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIDialog;


/**
 * Created by kr on 18.11.2015.
 */
public class CamActivity extends AbstractArchitectCamActivity {

    protected static final String WIKITUDE_SDK_KEY = "D/Xxxdhm2dwAW0QeZqCFe2hgxnGoxonrYVpE58UNUpusslSfRUGuLta2CJ2ZpZyESHU/npCvCj6KeSJMQHoGoZdVcGb9mPnxyCe1AsX2uktIvJmQfhl+Umy2ZvcqiNCJfU+ndoPKjFoR0Eoc4eLFbntZ5JVNItstuoz7kPcHHRVTYWx0ZWRfX4bImhnIspj3MCTSby9mVeQ2bDed8gk9flKGU5KrNcXz1UxPIsLPufBbJC8xH51+WZw1pJb9R7KCHnQPL32zvbyz+1NB9Kps6mtxzWYMDy6J73z6K0Koi3/l1UVtrecHSoXhVaY1TIjeg9SSxU3upWhin9yAWaGUrebcC9KqPiwQf1h1kWzQOhuoJrT5WeS07MmPlvbouNbgLPb+a/EcRKPZt/nZT3nhChmXzENdqup+KfHtWic8ae+zrYGfYUmm4OkNpq8KFvhNA1Y1F2whaCtZomDNbB+RscihhXPP6AeGybXHu9m14Y8iHjN18Tp0fVsjlGwvSVyYyYQHHoZBWEIX2JnSyQ9Bgq7z+9BrdG2rpI6EUK77dS53Ia6+vS8Ug4JJOwkOIXn3iZdmruOcljTSWAdylNfFmGuWxqLwDQ8xMLnjMucBc2XBeeQAxESrWHsXLdxAgxgPfJ7ktwM8oGLSwClawsBhAuSmk1E10LdesHVBH2YhpWqHUwZ1SsnBxl9JMwkDw89CYj1aTqqoWXmteWfgWQ+AHmSqxphHdQRd/WPug/7aSVU=";

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    //KR:
    private List<PointOfInterest> POIList;


    /**
     * holds the Wikitude SDK AR-View, this is where camera, markers, compass, 3D models etc. are rendered
     */
    protected ArchitectView architectView;

    /**
     * sensor accuracy listener in case you want to display calibration hints
     */
    protected SensorAccuracyChangeListener sensorAccuracyListener;

    /**
     * last known location of the user, used internally for content-loading after user location was fetched
     */
    protected Location lastKnownLocaton;

    /**
     * sample location strategy, you may implement a more sophisticated approach too
     */
    protected ILocationProvider				locationProvider;

    /**
     * location listener receives location updates and must forward them to the architectView
     */
    protected LocationListener locationListener;

    /**
     * urlListener handling "document.location= 'architectsdk://...' " calls in JavaScript"
     */
    protected ArchitectUrlListener urlListener;

    protected JSONArray poiData;

    protected boolean isLoading = false;

    /** Called when the activity is first created. */
    @SuppressLint("NewApi")
    @Override
    public void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

		/*Log.d("EXPLOCITY", "AbstractCam...: injectData() wird gestartet");
		this.injectData();*/

		/* pressing volume up/down should cause music volume changes */
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		/* set samples content view */
        this.setContentView( this.getContentViewId() );

        this.setTitle( this.getActivityTitle() );

		/*
		 *	this enables remote debugging of a WebView on Android 4.4+ when debugging = true in AndroidManifest.xml
		 *	If you get a compile time error here, ensure to have SDK 19+ used in your ADT/Eclipse.
		 *	You may even delete this block in case you don't need remote debugging or don't have an Android 4.4+ device in place.
		 *	Details: https://developers.google.com/chrome-developer-tools/docs/remote-debugging
		 */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) ) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

		/* set AR-view for life-cycle notifications etc. */
        this.architectView = (ArchitectView)this.findViewById( this.getArchitectViewId()  );

		/* pass SDK key if you have one, this one is only valid for this package identifier and must not be used somewhere else */
        final StartupConfiguration config = new StartupConfiguration( this.getWikitudeSDKLicenseKey(), this.getFeatures(), this.getCameraPosition() );

        try {
			/* first mandatory life-cycle notification */
            this.architectView.onCreate( config );
        } catch (RuntimeException rex) {
            this.architectView = null;
            Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", rex);
        }

        // set accuracy listener if implemented, you may e.g. show calibration prompt for compass using this listener
        this.sensorAccuracyListener = this.getSensorAccuracyListener();

        // set urlListener, any calls made in JS like "document.location = 'architectsdk://foo?bar=123'" is forwarded to this listener, use this to interact between JS and native Android activity/fragment
        this.urlListener = this.getUrlListener();

        // register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
        if (this.urlListener != null && this.architectView != null) {
            this.architectView.registerUrlListener( this.getUrlListener() );
        }

        if (hasGeo()) {
            // listener passed over to locationProvider, any location update is handled here
            this.locationListener = new LocationListener() {

                @Override
                public void onStatusChanged( String provider, int status, Bundle extras ) {
                }

                @Override
                public void onProviderEnabled( String provider ) {
                }

                @Override
                public void onProviderDisabled( String provider ) {
                }

                @Override
                public void onLocationChanged( final Location location ) {
                    // forward location updates fired by LocationProvider to architectView, you can set lat/lon from any location-strategy
                    if (location!=null) {
                        // sore last location as member, in case it is needed somewhere (in e.g. your adjusted project)
                        CamActivity.this.lastKnownLocaton = location;
                        if ( CamActivity.this.architectView != null ) {
                            // check if location has altitude at certain accuracy level & call right architect method (the one with altitude information)
                            if ( location.hasAltitude() && location.hasAccuracy() && location.getAccuracy()<7) {
                                CamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
                            } else {
                                CamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
                            }
                        }
                    }
                }
            };

            // locationProvider used to fetch user position
            this.locationProvider = getLocationProvider( this.locationListener );
        } else {
            this.locationProvider = null;
            this.locationListener = null;
        }
    }

    private int getFeatures() {
        int features = (hasGeo() ? StartupConfiguration.Features.Geo : 0) | (hasIR() ? StartupConfiguration.Features.Tracking2D : 0);
        return features;
    }

    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );
        Log.d("EXPLOCITY", "CamActivity-onPostCreate: injectData() wird gestartet");
        this.injectData();
    }

    @Override
    public String getARchitectWorldPath() {
        return getIntent().getExtras().getString(MainActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL);
    }

    @Override
    public String getActivityTitle() {
        return (getIntent().getExtras() != null && getIntent().getExtras().get(
                MainActivity.EXTRAS_KEY_ACTIVITY_TITLE_STRING) != null) ? getIntent()
                .getExtras().getString(MainActivity.EXTRAS_KEY_ACTIVITY_TITLE_STRING)
                : "Test-World";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cam;
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return WIKITUDE_SDK_KEY;
    }

    @Override
    public SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && CamActivity.this != null && !CamActivity.this.isFinishing() && System.currentTimeMillis() - CamActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText(CamActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
                    CamActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectUrlListener getUrlListener() {
        return new ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);

                // pressed "Detail" button on POI-detail panel
                if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
                    final Intent poiDetailIntent = new Intent(CamActivity.this, PoiDetailActivity.class);
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
                    Log.d("EXPLOCITY", "trying to start dialog");
                    CamActivity.this.startActivity(poiDetailIntent);
                    Log.d("EXPLOCITY", "tried to start intent");

                    /*Log.d("EXPLOCITIY", "creating bundle");
                    Bundle args = new Bundle();
                    args.putString("keyword", filter.getKeyword());
                    args.putInt("radius", filter.getRadius());
                    Log.d("EXPLOCITIY", "creating dialog");
                    POIFilterDialog dialog = new POIFilterDialog();
                    dialog.setArguments(args);
                    dialog.onAttach(CamActivity.this);
                    dialog.show(getFragmentManager(), "POI Filter Dialog");
                    Log.d("EXPLOCITY", "Dialog is shown");*/
                    return true;
                }

                // pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene
                else if ("button".equalsIgnoreCase(invokedUri.getHost())) {
                    CamActivity.this.architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new CaptureScreenCallback() {

                        @Override
                        public void onScreenCaptured(final Bitmap screenCapture) {
                            // store screenCapture into external cache directory
                            final File screenCaptureFile = new File(Environment.getExternalStorageDirectory().toString(), "screenCapture_" + System.currentTimeMillis() + ".jpg");

                            // 1. Save bitmap to file & compress to jpeg. You may use PNG too
                            try {
                                final FileOutputStream out = new FileOutputStream(screenCaptureFile);
                                screenCapture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                out.flush();
                                out.close();

                                // 2. create send intent
                                final Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpg");
                                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(screenCaptureFile));

                                // 3. launch intent-chooser
                                final String chooserTitle = "Share Snaphot";
                                CamActivity.this.startActivity(Intent.createChooser(share, chooserTitle));

                            } catch (final Exception e) {
                                // should not occur when all permissions are set
                                CamActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // show toast message in case something went wrong
                                        Toast.makeText(CamActivity.this, "Unexpected error, " + e, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                }
                return true;
            }
        };
    }

    protected void injectData() {
        if (!isLoading) {
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int activated_route = POIRouteProvider.getInstance().getActivated();
                    POIList = POIRouteProvider.getInstance().getPOIRouteList().get(activated_route).getPoiRoute();
                    isLoading = true;

                    final int WAIT_FOR_LOCATION_STEP_MS = 2000;
                    while (lastKnownLocaton==null && !isFinishing()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CamActivity.this, R.string.location_fetching, Toast.LENGTH_SHORT).show();
                            }
                        });
                        try {
                            Thread.sleep(WAIT_FOR_LOCATION_STEP_MS);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    if (lastKnownLocaton!=null && !isFinishing()) {
                        //KR: take pois and pass them to javascript
                        poiData = getPoiInformation(lastKnownLocaton, POIList);
                        Log.d("EXPLOCITY", "CamActivity-lastKnownLocation: " + lastKnownLocaton);
                        Log.d("EXPLOCITY", "CamActivity-injectData(): poiData: " + poiData);
                        callJavaScript("World.loadPoisFromJsonData", new String[] { poiData.toString() });
                    }
                    isLoading = false;
                }
            });
            t.start();
        }
        Log.d("EXPLOCITY", "CamActivity-injectData");
    }
    /**
     * call JacaScript in architectView
     * @param methodName
     * @param arguments
    */
    private void callJavaScript(final String methodName, final String[] arguments) {
        final StringBuilder argumentsString = new StringBuilder("");
        for (int i= 0; i<arguments.length; i++) {
            argumentsString.append(arguments[i]);
            if (i<arguments.length-1) {
                argumentsString.append(", ");
            }
        }
        if (this.architectView!=null) {
            final String js = ( methodName + "( " + argumentsString.toString() + " );" );
            this.architectView.callJavascript(js);
        }
    }
    /**
     * loads poiInformation and returns them as JSONArray. Ensure attributeNames of JSON POIs are well known in JavaScript, so you can parse them easily
     * @param userLocation the location of the user
     * @return POI information in JSONArray
    */
    public static JSONArray getPoiInformation(final Location userLocation, List<PointOfInterest> POIList){
        if (userLocation==null) {
            return null;
        }
        final JSONArray pois = new JSONArray();
        // ensure these attributes are also used in JavaScript when extracting POI data
        final String ATTR_ID = "id";
        final String ATTR_NAME = "name";
        final String ATTR_LATITUDE = "latitude";
        final String ATTR_LONGITUDE = "longitude";
        final String ATTR_ALTITUDE = "altitude";
        for (PointOfInterest poi : POIList) {
            final HashMap<String, String> poiInformation = new HashMap<>();
            poiInformation.put(ATTR_ID, poi.getId());
            poiInformation.put(ATTR_LATITUDE, String.valueOf(poi.getLatLng().latitude));
            poiInformation.put(ATTR_LONGITUDE, String.valueOf(poi.getLatLng().longitude));
            final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
            // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
            poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
            poiInformation.put(ATTR_NAME, poi.getName());
            pois.put(new JSONObject(poiInformation));
        }
        Log.d("EXPLOCITY", "CamActivity-getPoiInformatin-pois: " + pois);
        return pois;
    }


    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        return new LocationProvider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    public boolean hasGeo() {
        return getIntent().getExtras().getBoolean(MainActivity.EXTRAS_KEY_ACTIVITY_GEO);
    }

    @Override
    protected boolean hasIR() {
        return getIntent().getExtras().getBoolean(
                MainActivity.EXTRAS_KEY_ACTIVITY_IR);
    }

    @Override
    protected CameraPosition getCameraPosition() {
        return CameraPosition.DEFAULT;
    }

}