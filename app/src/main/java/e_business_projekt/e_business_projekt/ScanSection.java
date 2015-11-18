package e_business_projekt.e_business_projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import e_business_projekt.e_business_projekt.R;

/**
 * Created by Sebastian on 18.11.2015.
 */
public class ScanSection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_scan_section, container, false);
        return rootView;
    }
}
