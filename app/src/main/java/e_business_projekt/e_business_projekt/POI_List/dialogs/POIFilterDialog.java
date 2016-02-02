package e_business_projekt.e_business_projekt.poi_list.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.adapter.POIFilterListViewItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 01.02.2016.
 */
public class POIFilterDialog extends DialogFragment {

    private static final String TAG = "EBP.POIFilterDialog";

    //TODO: Replace
    private static final String FOOD ="food";
    private static final String MUSEUM = "museum";
    private static final String NIGHT_CLUB = "night_club";
    private static final String ESTABLISHMENT = "establishment";

    private List<String> types = new ArrayList<>();

    public POIFilterDialog() {
        types.add(FOOD);
        types.add(MUSEUM);
        types.add(NIGHT_CLUB);
        types.add(ESTABLISHMENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "Creating POI Filter Dialog ");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poi_filter, null);

        builder.setView(dialogView);
        builder.setTitle("POI Filter");
        builder.setMessage("Hier Filtermöglichkeiten!");

        ListView listView = (ListView) dialogView.findViewById(R.id.listViewFilterTypes);
        listView.setAdapter(new POIFilterListViewItemAdapter(getActivity(), types ));

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Item Selected!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "Nothing selected!");
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "OK Filter clicked");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Cancel Filter clicked");
            }
        });
        return builder.create();
    }
}
