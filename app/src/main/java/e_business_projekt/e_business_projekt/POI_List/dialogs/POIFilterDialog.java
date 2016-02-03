package e_business_projekt.e_business_projekt.poi_list.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
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

    private POIFilterDialogCallback fdc;

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

        final ListView listView = (ListView) dialogView.findViewById(R.id.listViewFilterTypes);
        final POIFilterListViewItemAdapter adapter = new POIFilterListViewItemAdapter(getActivity(), types );
        listView.setAdapter(adapter);

        final EditText radiusText = (EditText) dialogView.findViewById(R.id.editTextRadius);
        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.seekBarRadius);

        // init values
        radiusText.setText("5000");
        radiusText.setFocusable(false);
        radiusText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                radiusText.setFocusableInTouchMode(true);
                return false;
            }
        });

        seekBar.setMax(25000);
        seekBar.setProgress(seekBar.getMax()/5);

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

                    if (value > 25000){
                        value = 25000;
                    } else if (value < 1){
                        value = 1;
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
                if (seekBar.getProgress() == 0){
                    seekBar.setProgress(1);
                }
                radiusText.setText("" + seekBar.getProgress());
            }
        });

        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fdc.poiFilterDialogCallback(adapter.getSelected(), seekBar.getProgress());

//                Log.i(TAG, "setFilterRadius: " + seekBar.getProgress());
//                Log.i(TAG, "setFilterTypes: " + adapter.getSelected().toString());
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
