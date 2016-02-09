package e_business_projekt.e_business_projekt.poi_list.dialogs;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;
import e_business_projekt.e_business_projekt.PoiListActivity;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.WebViewActivity;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;

import java.util.List;

public class POIDialog extends DialogFragment {

    private static final String TAG = "EBP.POIDialog";

    public POIDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "Creating POI Dialog ");

        Bundle args = getArguments();

        final PointOfInterest poi = args.getParcelable("poi");

        final String name ;
        final String address;
        final String phone;
        final String website;
        final String wikiLink;

        if (poi != null){
            name = poi.getName();
            address = poi.getAddress();
            phone = poi.getPhonenumber();
            website = poi.getWebsiteUri().toString();
            wikiLink = poi.getWikiLink();
        } else {
            name = "";
            address = "";
            phone = "";
            website = "";
            wikiLink = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poi, null);

        TextView addressText = (TextView) dialogView.findViewById(R.id.addressTextView);
        if (address.length() == 0){
            addressText.setVisibility(View.GONE);
            dialogView.findViewById(R.id.addressImageView).setVisibility(View.GONE);
        } else {
            addressText.setText(address);
        }

        TextView phoneText = (TextView) dialogView.findViewById(R.id.phoneTextView);
        if (phone.length() == 0){
            phoneText.setVisibility(View.GONE);
            dialogView.findViewById(R.id.phoneImageView).setVisibility(View.GONE);
        } else {
            phoneText.setText(phone);
        }

        TextView websiteText = (TextView) dialogView.findViewById(R.id.websiteTextView);
        if ((website.length() == 0) || website.equals("www.google.com")){
            websiteText.setVisibility(View.GONE);
            dialogView.findViewById(R.id.websiteImageView).setVisibility(View.GONE);
        } else {
            //websiteText.setText(website);
            websiteText.setText(Html.fromHtml("<a href=\"" + website + "\">" + website + "</a>"));
            //websiteText.setMovementMethod(LinkMovementMethod.getInstance());

        }

        websiteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Text geklickt!");

                Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", website);
                startActivity(intent);
            }
        });

        builder.setView(dialogView);
        builder.setTitle(name);
        if (wikiLink.length() != 0){
            builder.setNeutralButton("Wikipedia", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
                    intent.putExtra("url", wikiLink);
                    startActivity(intent);

                    Log.i(TAG, "Wikipediabutton clicked");
                }
            });
        }

        if(getActivity() instanceof PoiListActivity){
            builder.setPositiveButton("Add POI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    POIRouteProvider routeManager = POIRouteProvider.getInstance();
                    String routeName = routeManager.getActivatedRoute().getRouteName();
                    List<PointOfInterest> poiList = routeManager.getActivatedRoute().getPoiRoute();
                    boolean contains = false;
                    for(PointOfInterest comparePoi: poiList){
                        if (comparePoi.getId().equals(poi.getId())){
                            contains = true;
                            break;
                        }
                    }
                    if (!contains){
                        routeManager.addPoiToActiveRoute(poi);
                        Toast.makeText(getActivity(), "Add " + poi.getName() + " to " + routeName , Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), poi.getName() + " already exists in " + routeName, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Backbutton clicked");
            }
        });

        return builder.create();
    }

}
