package e_business_projekt.e_business_projekt.wikitude;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.WebViewActivity;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;


public class PoiDetailActivity extends AppCompatActivity {

	public static final String EXTRAS_KEY_POI_ID = "id";
	public static final String EXTRAS_KEY_POI_TITILE = "name";
	public static final String EXTRAS_KEY_POI_DESCR = "description";
    public String poi_id;
    public String website;
	//KR:
	protected List<PointOfInterest> POIList;
    private PointOfInterest active_poi;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sample_poidetail);

		poi_id = getIntent().getExtras().getString(EXTRAS_KEY_POI_ID);
        Log.d("EXPLOCITY", "poi_id: " + poi_id);


		POIList = POIRouteProvider.getInstance().getActivatedRoute().getPoiRoute();



        for(PointOfInterest poi : POIList){
            Log.d("EXPLOCITY", "Vergleichs_poi: " + poi.getId());
            if (poi.getId().equals(poi_id)){
                active_poi = poi;
                Log.d("EXPLOCITY", "POIDet-active_poi: " + active_poi);
                break;
            }
        }

		((TextView)findViewById(R.id.addressTextView_pd)).setText(active_poi.getAddress());
        ((TextView)findViewById(R.id.phoneTextView_pd)).setText(active_poi.getPhonenumber());

        website = active_poi.getWebsiteUri().toString();

        TextView websiteText = (TextView)findViewById(R.id.websiteTextView_pd);
        if ((website.length() == 0) || website.equals("www.google.com")){
            websiteText.setVisibility(View.GONE);
            findViewById(R.id.websiteImageView_pd).setVisibility(View.GONE);
        } else {
            //websiteText.setText(website);
            websiteText.setText(Html.fromHtml("<a href=\"" + website + "\">" + website + "</a>"));
            //websiteText.setMovementMethod(LinkMovementMethod.getInstance());

            //((TextView)findViewById(R.id.websiteTextView_pd)).setText(active_poi.getWebsiteUri().toString());
        }

        websiteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( PoiDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", website);
                startActivity(intent);
            }
        });
    }

}
