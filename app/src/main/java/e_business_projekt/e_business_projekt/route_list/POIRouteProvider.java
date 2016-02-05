package e_business_projekt.e_business_projekt.route_list;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.ArrayList;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class POIRouteProvider {

    private static final String TAG = "EBP.POIRouteProvider";
    private ArrayList<POIRoute> POIRouteList;

    public POIRouteProvider() {
        this.POIRouteList = new ArrayList<>();
        createTestData();
    }

    public POIRouteProvider(ArrayList<POIRoute> POIRouteList) {
        this.POIRouteList = POIRouteList;
    }

    public ArrayList<POIRoute> getPOIRouteList() {
        Log.i(TAG, "getPOIRouteList() called");
        return POIRouteList;
    }

    public void setPOIRouteList(ArrayList<POIRoute> POIRouteList) {
        Log.i(TAG, "setPOIRouteList() called");
        this.POIRouteList = POIRouteList;
    }

    public void addRoute(){
        Log.i(TAG, "addRoute() called");
        this.POIRouteList.add(new POIRoute());
    }


    public void createTestData(){
        //----------------------- Creating Test Data -----------------------
        PointOfInterest poi1 = new PointOfInterest();
        poi1.setName("Siegessäule");
        poi1.setId("TEST ID Siegessäule");
        poi1.setDistance(200);
        poi1.setLatLng(new LatLng(52.51454340000001, 13.3501189));
        poi1.setAddress("An der Siegessäule!");
        poi1.setPhonenumber("03012345");
        poi1.addPlaceType(1234);
        poi1.addPlaceType(12);

        PointOfInterest poi2 = new PointOfInterest();
        poi2.setName("Potsdamer Platz");
        poi2.setId("TEST ID Potsdamer Platz");
        poi2.setDistance(100);
        poi2.setLatLng(new LatLng(52.5096488, 13.3759441));
        poi2.setAddress("Am Potsdamer Platz!");
        poi2.setPhonenumber("030111111");
        poi2.addPlaceType(4321);
        poi2.addPlaceType(43);


        PointOfInterest poi3 = new PointOfInterest();
        poi3.setName("Brandenburger Tor");
        poi3.setId("TEST ID Brandenburger Tor");
        poi3.setDistance(300);
        poi3.setLatLng(new LatLng(52.5162746, 13.377704));
        poi3.setAddress("Am Brandenburger Tor!");
        poi3.setPhonenumber("0309999999999999999");
        poi3.addPlaceType(9876);
        poi3.addPlaceType(98);

        PointOfInterest poi4 = new PointOfInterest();
        poi4.setName("Fernsehturm");
        poi4.setId("TEST ID Fernsehturm");
        poi4.setDistance(3);
        poi4.setLatLng(new LatLng(52.5162746, 13.377704));
        poi4.setAddress("Am Fernsehturm!");
        poi4.setPhonenumber("030 6x die 6");
        poi4.addPlaceType(4567);
        poi4.addPlaceType(45);

        PointOfInterest poi5 = new PointOfInterest();
        poi5.setName("Andels");
        poi5.setId("TEST ID Andels");
        poi5.setDistance(3);
        poi5.setLatLng(new LatLng(52.5283906, 1313.4570176));
        poi5.setAddress("Am Andels irgendwo Landsberger Allee!");
        poi5.setPhonenumber("030 6x die 6");
        poi5.addPlaceType(1111);
        poi5.addPlaceType(11);

        POIRoute POIRoute1 = new POIRoute();
        POIRoute1.addPOI(poi1);
        POIRoute1.addPOI(poi2);
        POIRoute1.addPOI(poi3);

        POIRoute POIRoute2 = new POIRoute();
        POIRoute2.addPOI(poi3);
        POIRoute2.addPOI(poi4);
        POIRoute2.addPOI(poi5);

        POIRoute POIRoute3 = new POIRoute();
        POIRoute3.addPOI(poi1);
        POIRoute3.addPOI(poi2);
        POIRoute3.addPOI(poi3);
        POIRoute3.addPOI(poi4);
        POIRoute3.addPOI(poi5);

        POIRoute1.setRouteName("Party Route");
        POIRouteList.add(POIRoute1);
        POIRoute2.setRouteName("Langweilige Route");
        POIRouteList.add(POIRoute2);
        POIRoute3.setRouteName("Die Geile Route");
        POIRouteList.add(POIRoute3);

        //----------------------- Test Data End -----------------------
    }

}