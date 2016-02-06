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
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.WebViewActivity;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

public class POIDialog extends DialogFragment {

    private static final String TAG = "EBP.POIDialog";

    public POIDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "Creating POI Dialog ");

        Bundle args = getArguments();

        PointOfInterest poi = args.getParcelable("poi");

        final String name ;
        final String address;
        final String phone;
        final String website;

        if (poi != null){
            name = poi.getName();
            address = poi.getAddress();
            phone = poi.getPhonenumber();
            website = poi.getWebsiteUri().toString();
        } else {
            name = "";
            address = "";
            phone = "";
            website = "";
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
        if (website.length() == 0){
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

        builder.setPositiveButton("Wikipedia?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Wikipediabutton clicked");
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Backbutton clicked");
            }
        });

        return builder.create();
    }

}
