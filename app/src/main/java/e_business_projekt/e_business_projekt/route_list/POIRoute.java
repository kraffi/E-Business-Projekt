package e_business_projekt.e_business_projekt.route_list;

import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Vinh Khoa Nguyen on 05.02.2016.
 */
public class POIRoute {
    private String routeName;
    private List<PointOfInterest> poiRoute;

    public POIRoute(String routeName, List<PointOfInterest> poiRoute) {
        this.routeName = routeName;
        this.poiRoute = poiRoute;
    }

    public POIRoute(){
        this.routeName = "New Route";
        this.poiRoute = new ArrayList<>();
    }

    public List<PointOfInterest> getPoiRoute() {
        return poiRoute;
    }

    public void setPoiRoute(List<PointOfInterest> poiRoute) {
        this.poiRoute = poiRoute;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void addPOI(PointOfInterest poi){
        if(!poiRoute.contains(poi)){
            this.poiRoute.add(poi);
        }
    }

    public PointOfInterest getPOI(int position){
        return this.poiRoute.get(position);
    }
}
