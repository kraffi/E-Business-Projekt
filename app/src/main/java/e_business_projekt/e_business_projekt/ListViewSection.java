package e_business_projekt.e_business_projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by kr on 18.11.2015.
 */
public class ListViewSection extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_listview_section, container, false);

        ArrayList<PointOfInterest> poiList = getPoiList();

        ListView lv = (ListView) rootView.findViewById(R.id.poiListView);
        lv.setAdapter(new ListViewItemAdapter(getActivity(), poiList));

        return rootView;
    }

    private ArrayList<PointOfInterest> getPoiList(){
        ArrayList<PointOfInterest> list = new ArrayList<PointOfInterest>();

        list.add(new PointOfInterest("Fernsehturm", "Der Fernsehturm ist schon ziemlich cool!"));
        list.add(new PointOfInterest("Beuth Hochschule für Technik", "Beuth ftw!"));
        list.add(new PointOfInterest("Funkturm", "Geiles Teil!"));
        list.add(new PointOfInterest("Mercedes Benz Arena", "O2 Arena RIP"));

        return list;
    }
}
