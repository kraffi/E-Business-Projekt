package e_business_projekt.e_business_projekt.poi_list;

import android.app.*;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import e_business_projekt.e_business_projekt.R;

public class POIDialog extends DialogFragment {

    private static final String TAG = "EBP.POIDialog";

    public POIDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String name = args.getString("name", "");
        String info = args.getString("info", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(name);
        builder.setMessage(info);
        builder.setPositiveButton("Drück mich!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "OK");
            }
        });

        return builder.create();
    }

}
