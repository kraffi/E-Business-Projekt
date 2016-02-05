package e_business_projekt.e_business_projekt.poi_list.dialogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 02.02.2016.
 */
public interface POIFilterDialogCallback {
    void poiFilterDialogCallback(String query, List<int[]> filterTypes, int radius);
}
