package e_business_projekt.e_business_projekt.route_list.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class EditRouteDialog extends DialogFragment {

    private static final String TAG = "EBP.EditRouteDialog";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_route, null);

        builder.setView(dialogView);
        builder.setTitle("Edit Route Dialog");

        final ListView listView = (ListView) dialogView.findViewById(R.id.editRouteListView);

        //TODO Pass POI LIST
        List<PointOfInterest> poiList = new ArrayList<>();

        final EditRouteListViewAdapter adapter = new EditRouteListViewAdapter(getActivity(), poiList);
        listView.setAdapter(adapter);

        return builder.create();
    }
}
