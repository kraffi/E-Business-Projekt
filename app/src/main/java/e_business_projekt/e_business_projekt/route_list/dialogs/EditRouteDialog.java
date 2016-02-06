package e_business_projekt.e_business_projekt.route_list.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIDialog;
import e_business_projekt.e_business_projekt.route_list.adapter.EditRouteListViewItemAdapter;
import e_business_projekt.e_business_projekt.route_list.adapter.EditRouteListViewItemAdapterCallback;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class EditRouteDialog extends DialogFragment implements EditRouteListViewItemAdapterCallback{

    private static final String TAG = "EBP.EditRouteDialog";
    private EditRouteDialogCallback callback;
    private List<PointOfInterest> poiList;
    private EditRouteListViewItemAdapter adapter;

    // set callback
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.callback = (EditRouteDialogCallback) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get arguments
        Bundle args = getArguments();
        poiList = args.getParcelableArrayList("poiList");
        final String routeName = args.getString("routeName");
        final int position = args.getInt("position");

        // Get Dialog View
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_route, null);

        // ListView editRouteListView
        final ListView listView = (ListView) dialogView.findViewById(R.id.editRouteListView);

        // EditText Routename
        final EditText editRouteName = (EditText) dialogView.findViewById(R.id.editRouteName);
        editRouteName.setText(routeName);
        editRouteName.setSelection(editRouteName.getText().length());

        // Build Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Edit Route");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callback.editRouteDialogOkButtonCallback(editRouteName.getText().toString(), poiList, position);
                Log.i(TAG, "Edit OK clicked");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Edit Cancel clicked");
            }
        });

        //Create and set Adapter
        adapter = new EditRouteListViewItemAdapter(this, getActivity(), poiList);
        listView.setAdapter(adapter);

        return builder.create();
    }

    @Override
    public void clickPoi(int position) {
        Log.i("KLICK", poiList.get(position).getName());

        PointOfInterest poi = poiList.get(position);

        Bundle args = new Bundle();
        args.putParcelable("poi", poi);

        POIDialog dialog = new POIDialog();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "POI Dialog");
    }

    @Override
    public void removePoiFromRouteButton(int position) {
        Log.i("REMOVE", poiList.get(position).getName());
        poiList.remove(position);
        adapter.notifyDataSetChanged();
    }
}
