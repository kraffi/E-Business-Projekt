package e_business_projekt.e_business_projekt.poi_list.provider;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 02.02.2016.
 */
public class POIFilter {

    private String keyword;
    private List<int[]> filterTypes;
    private int radius;

    // TODO: Think if it makes sense to use an initFilter
    private String initFilter = "airport|amusement_park|aquarium|art_gallery|atm|bank|bar|bowling_alley|" +
            "bus_station|cafe|campground|casino|church|city_hall|embassy|establishment|food|hindu_temple|" +
            "liquor_store|lodging|meal_takeaway|mosque|movie_theater|museum|night_club|park|parking|" +
            "place_of_worship|police|post_office|restaurant|rv_park|shopping_mall|spa|stadium|" +
            "subway_station|synagogue|taxi_stand|train_station|travel_agency|university|zoo";

    public POIFilter(String keyword, List<int[]> filterTypes,  int radius) {
        this.filterTypes = filterTypes;
        this.keyword = keyword;
        this.radius = radius;
    }

    public POIFilter() {
        this.keyword = "";
        this.filterTypes = new ArrayList<>();
        this.radius = 5000;
    }

    public List<int[]> getFilterTypes() {
        return filterTypes;
    }

    public void setFilterTypes(List<int[]> filterTypes) {
        this.filterTypes = filterTypes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getInitFilter() {
        return initFilter;
    }

    public void setInitFilter(String initFilter) {
        this.initFilter = initFilter;
    }

    @Override
    public String toString() {
        return "POIFilter{" +
                "filterTypes=" + filterTypes +
                ", keyword='" + keyword + '\'' +
                ", radius=" + radius +
                '}';
    }
}
