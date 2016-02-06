package e_business_projekt.e_business_projekt.route_list.dialogs;

import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 06.02.2016.
 */
public interface EditRouteDialogCallback {
    void editRouteDialogOK(String name, List<PointOfInterest> poiList, int position);
}
