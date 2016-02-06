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
import e_business_projekt.e_business_projekt.poi_list.dialogs.POIFilterDialogCallback;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class EditRouteDialog extends DialogFragment {

    private static final String TAG = "EBP.EditRouteDialog";

    private EditRouteDialogCallback callback;

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

        Bundle args = getArguments();
        final List<PointOfInterest> poiList = args.getParcelableArrayList("poiList");
        final String routeName = args.getString("routeName");
        final int position = args.getInt("position");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_route, null);
        final ListView listView = (ListView) dialogView.findViewById(R.id.editRouteListView);
        final EditText editRouteName = (EditText) dialogView.findViewById(R.id.editRouteName);
        editRouteName.setText(routeName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Edit Route");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callback.editRouteDialogOK(editRouteName.getText().toString(), poiList, position);
                Log.i(TAG, "Edit OK clicked");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Edit Cancel clicked");
            }
        });

        final EditRouteListViewItemAdapter adapter = new EditRouteListViewItemAdapter(getActivity(), poiList);
        listView.setAdapter(adapter);

        return builder.create();
    }
}
