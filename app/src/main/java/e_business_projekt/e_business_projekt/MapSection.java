package e_business_projekt.e_business_projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sebastian on 18.11.2015.
 */
public class MapSection extends Fragment {

    public static final String ARG_SECTION_NUMBER = "map number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_map_section, container, false);
        return rootView;
    }
}
