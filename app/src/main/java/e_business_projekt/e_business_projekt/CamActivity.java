package e_business_projekt.e_business_projekt;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.CaptureScreenCallback;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.wikitude.architect.StartupConfiguration.CameraPosition;

import e_business_projekt.e_business_projekt.Wikitude.AbstractArchitectCamActivity;
import e_business_projekt.e_business_projekt.Wikitude.ArchitectViewHolderInterface;
import e_business_projekt.e_business_projekt.Wikitude.PoiDetailActivity;


/**
 * Created by kr on 18.11.2015.
 */
public class CamActivity extends AbstractArchitectCamActivity {

    protected static final String WIKITUDE_SDK_KEY = "D/Xxxdhm2dwAW0QeZqCFe2hgxnGoxonrYVpE58UNUpusslSfRUGuLta2CJ2ZpZyESHU/npCvCj6KeSJMQHoGoZdVcGb9mPnxyCe1AsX2uktIvJmQfhl+Umy2ZvcqiNCJfU+ndoPKjFoR0Eoc4eLFbntZ5JVNItstuoz7kPcHHRVTYWx0ZWRfX4bImhnIspj3MCTSby9mVeQ2bDed8gk9flKGU5KrNcXz1UxPIsLPufBbJC8xH51+WZw1pJb9R7KCHnQPL32zvbyz+1NB9Kps6mtxzWYMDy6J73z6K0Koi3/l1UVtrecHSoXhVaY1TIjeg9SSxU3upWhin9yAWaGUrebcC9KqPiwQf1h1kWzQOhuoJrT5WeS07MmPlvbouNbgLPb+a/EcRKPZt/nZT3nhChmXzENdqup+KfHtWic8ae+zrYGfYUmm4OkNpq8KFvhNA1Y1F2whaCtZomDNbB+RscihhXPP6AeGybXHu9m14Y8iHjN18Tp0fVsjlGwvSVyYyYQHHoZBWEIX2JnSyQ9Bgq7z+9BrdG2rpI6EUK77dS53Ia6+vS8Ug4JJOwkOIXn3iZdmruOcljTSWAdylNfFmGuWxqLwDQ8xMLnjMucBc2XBeeQAxESrWHsXLdxAgxgPfJ7ktwM8oGLSwClawsBhAuSmk1E10LdesHVBH2YhpWqHUwZ1SsnBxl9JMwkDw89CYj1aTqqoWXmteWfgWQ+AHmSqxphHdQRd/WPug/7aSVU=";

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    @Override
    public String getARchitectWorldPath() {
        return getIntent().getExtras().getString(
                MainActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL);
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

                // pressed "More" button on POI-detail panel
                if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
                    final Intent poiDetailIntent = new Intent(CamActivity.this, PoiDetailActivity.class);
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
                    poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
                    CamActivity.this.startActivity(poiDetailIntent);
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

    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        //todo kr 01.02.16: connect with location provider Raul implemented
        return new LocationPRovider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    protected boolean hasGeo() {
        return getIntent().getExtras().getBoolean(
                MainActivity.EXTRAS_KEY_ACTIVITY_GEO);
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