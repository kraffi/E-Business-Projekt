package e_business_projekt.e_business_projekt;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 10.12.2015.
 */
public class PointOfInterest {

    private String name;
    private String id;
    private LatLng latLng;
    private Uri websiteUri;
    private List<Integer> placeTypes;
    private int img;

    // constructor for PointOfInterest
    public PointOfInterest(Place place) {
        this.id = place.getId();
        this.latLng = place.getLatLng();
        this.name = place.getName().toString();
        this.placeTypes = place.getPlaceTypes();
        this.websiteUri = place.getWebsiteUri();

        //TODO: Replace Placeholder img
        this.img = R.mipmap.ic_launcher;
    }

    // TODO: Placeholder constructor: Delete if it gets unnecessary
    public PointOfInterest(String name, String info){
        this.id = info;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getImg() {
        return img;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaceTypes(List<Integer> placeTypes) {
        this.placeTypes = placeTypes;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public String toString() {
        return "name: " + name;
    }
}
