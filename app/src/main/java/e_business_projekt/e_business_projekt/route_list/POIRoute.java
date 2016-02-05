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
    private boolean activated;

    public POIRoute(String routeName, List<PointOfInterest> poiRoute) {
        this.routeName = routeName;
        this.poiRoute = poiRoute;
        activated = false;
    }

    public POIRoute(){
        this.routeName = "New Route";
        this.poiRoute = new ArrayList<>();
        activated = false;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public PointOfInterest getPOI(int position){
        return this.poiRoute.get(position);
    }
}
