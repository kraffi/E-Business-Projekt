package e_business_projekt.e_business_projekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import e_business_projekt.e_business_projekt.R;

/**
 * Created by Sebastian on 18.11.2015.
 */
public class CamActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cam, menu);
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
            case R.id.action_main:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.action_map:
                intent = new Intent(this, MapActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }
}
