package e_business_projekt.e_business_projekt.poi_list.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 02.02.2016.
 */
public class POIFilter {

    private String keyword;
    private List<String> filterTypes;
    private int radius;

    public POIFilter(String keyword, List<String> filterTypes,  int radius) {
        this.filterTypes = filterTypes;
        this.keyword = keyword;
        this.radius = radius;
    }

    public POIFilter() {
        this.keyword = "";
        this.filterTypes = new ArrayList<>();
        this.radius = 5000;
    }

    public List<String> getFilterTypes() {
        return filterTypes;
    }

    public void setFilterTypes(List<String> filterTypes) {
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

    @Override
    public String toString() {
        return "POIFilter{" +
                "filterTypes=" + filterTypes +
                ", keyword='" + keyword + '\'' +
                ", radius=" + radius +
                '}';
    }
}
