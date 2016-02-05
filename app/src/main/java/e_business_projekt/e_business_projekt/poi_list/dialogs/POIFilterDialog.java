package e_business_projekt.e_business_projekt.poi_list.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import com.google.android.gms.location.places.Place;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.adapter.POIFilterListViewItemAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by RaulVinhKhoa on 01.02.2016.
 */
public class POIFilterDialog extends DialogFragment {

    private static final String TAG = "EBP.POIFilterDialog";

    private static final List<String> filterTypeNames = new ArrayList<>();
    private static final List<int[]> filterTypes = new ArrayList<>();

    private POIFilterDialogCallback fdc;

    // Filter Types
    private static final int[] TYPE_ATTRACTION = {Place.TYPE_EMBASSY, Place.TYPE_UNIVERSITY, Place.TYPE_LOCALITY, Place.TYPE_NEIGHBORHOOD, Place.TYPE_POINT_OF_INTEREST};
    private static final int[] TYPE_CULTURE_RELIGION = {Place.TYPE_ART_GALLERY, Place.TYPE_MUSEUM, Place.TYPE_LIBRARY, Place.TYPE_CHURCH, Place.TYPE_HINDU_TEMPLE, Place.TYPE_MOSQUE, Place.TYPE_PLACE_OF_WORSHIP, Place.TYPE_SYNAGOGUE, Place.TYPE_CITY_HALL};
    private static final int[] TYPE_ENTERTAINMENT = {Place.TYPE_AMUSEMENT_PARK, Place.TYPE_MOVIE_THEATER, Place.TYPE_STADIUM, Place.TYPE_SPA, Place.TYPE_BOWLING_ALLEY, Place.TYPE_CASINO, Place.TYPE_NIGHT_CLUB};
    private static final int[] TYPE_VIVARIUM_NATURE = {Place.TYPE_PARK, Place.TYPE_NATURAL_FEATURE, Place.TYPE_AQUARIUM, Place.TYPE_ZOO};
    private static final int[] TYPE_FOOD_DRINK = {Place.TYPE_RESTAURANT, Place.TYPE_FOOD, Place.TYPE_BAR, Place.TYPE_CAFE, Place.TYPE_LIQUOR_STORE, Place.TYPE_MEAL_TAKEAWAY, Place.TYPE_BAKERY};
    private static final int[] TYPE_OVERNIGHT = {Place.TYPE_CAMPGROUND, Place.TYPE_LODGING, Place.TYPE_RV_PARK};
    private static final int[] TYPE_SHOPPING = {Place.TYPE_SHOPPING_MALL};
    private static final int[] TYPE_TRAVELING = {Place.TYPE_AIRPORT, Place.TYPE_BUS_STATION, Place.TYPE_TRAIN_STATION, Place.TYPE_TAXI_STAND, Place.TYPE_SUBWAY_STATION, Place.TYPE_TRAVEL_AGENCY};

    // Filter limits
    int fMax = 10000;
    int fMin = 100;

    // filter values on create
    int fRadius = 5000;
    String fKeyword = "";

    public POIFilterDialog() {
        filterTypeNames.clear();
        filterTypeNames.add("Attraction");         //0
        filterTypeNames.add("Culture & Religion"); //1
        filterTypeNames.add("Entertainment");      //2
        filterTypeNames.add("Vivarium & Nature");  //3
        filterTypeNames.add("Food & Drink");       //4
        filterTypeNames.add("Shopping");           //5
        filterTypeNames.add("Traveling");          //6

        filterTypes.clear();
        filterTypes.add(TYPE_ATTRACTION);           //0
        filterTypes.add(TYPE_CULTURE_RELIGION);     //1
        filterTypes.add(TYPE_ENTERTAINMENT);        //2
        filterTypes.add(TYPE_VIVARIUM_NATURE);      //3
        filterTypes.add(TYPE_FOOD_DRINK);           //4
        filterTypes.add(TYPE_SHOPPING);             //5
        filterTypes.add(TYPE_TRAVELING);            //6
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.fdc = (POIFilterDialogCallback) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "Creating POI Filter Dialog ");

        // get filter status passed by parent activity
        Bundle args = getArguments();

        fKeyword = args.getString("keyword");
        fRadius = args.getInt("radius");

        Set<String> types = args.keySet();
        types.remove("keyword");
        types.remove("radius");
        List<Integer> checks = new ArrayList<>();
        for(String type: types){
            int[] typeArray = args.getIntArray(type);
            for(int i = 0; i < filterTypes.size(); i++){
                if (typeArray[0] == filterTypes.get(i)[0]){
                    if(!checks.contains(i)){
                        checks.add(i);
                    }
                }
            }
        }
        //Log.i("TEST:", checks.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poi_filter, null);

        builder.setView(dialogView);
        builder.setTitle("POI Filter");

        final ListView listView = (ListView) dialogView.findViewById(R.id.listViewFilterTypes);
        final POIFilterListViewItemAdapter adapter = new POIFilterListViewItemAdapter(getActivity(), filterTypeNames, filterTypes, checks);
        listView.setAdapter(adapter);

        final EditText radiusText = (EditText) dialogView.findViewById(R.id.editTextRadius);
        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.seekBarRadius);

        // init values
        radiusText.setText(fRadius + "");
        radiusText.setFocusable(false);
        radiusText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                radiusText.setFocusableInTouchMode(true);
                return false;
            }
        });

        seekBar.setMax(fMax);
        seekBar.setProgress(fRadius);

        radiusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    int value = Integer.parseInt(s.toString());

                    if (value > fMax){
                        value = fMax;
                    } else if (value < fMin){
                        value = fMin;
                    }

                    seekBar.setProgress(value);
                    radiusText.setSelection(radiusText.getText().length());
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusText.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radiusText.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < fMin){
                    seekBar.setProgress(fMin);
                }
                radiusText.setText(seekBar.getProgress() + "");
            }
        });

        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fdc.poiFilterDialogCallback(fKeyword, adapter.getSelectedFilter(), seekBar.getProgress());
                Log.i(TAG, "Filter OK clicked");
            }
        });

        builder.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Cancel Filter clicked");
            }
        });
        return builder.create();
    }
}
