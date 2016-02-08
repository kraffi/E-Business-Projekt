package e_business_projekt.e_business_projekt.wikitude;

import android.os.Bundle;
import android.util.Log;

import e_business_projekt.e_business_projekt.CamActivity;

public class CamContentFromNativeActivity extends CamActivity
{
	@Override
	protected void onPostCreate( final Bundle savedInstanceState ) {
		super.onPostCreate( savedInstanceState );
        Log.d("EXPLOCITY", "ContentFromNative...: injectData() wird gestartet");
		this.injectData();
	}
}