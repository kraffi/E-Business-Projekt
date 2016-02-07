package e_business_projekt.e_business_projekt.poi_list;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import e_business_projekt.e_business_projekt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Point of Interest Object
 * Created by Raul Vinh Khoa Nguyen on 10.12.2015.
 */
public class PointOfInterest implements Parcelable {

    private String name;
    private String id;

    private LatLng latLng;
    private float distance;

    private String address;
    private String phonenumber;
    private Uri websiteUri;
    private String wikiLink;

    private List<Integer> placeTypes;
    private int img;

    /**
     * constructor for a PointOfInterest
     * @param place takes a place object from Google Places
     */
    public PointOfInterest(Place place) {
        this.id = place.getId();
        this.latLng = place.getLatLng();
        this.distance = -1;
        this.name = place.getName().toString();
        this.placeTypes = place.getPlaceTypes();

        this.address = place.getAddress().toString();
        this.phonenumber = place.getPhoneNumber().toString();
        this.websiteUri = place.getWebsiteUri();

        //TODO: Replace Placeholder img
        this.img = R.mipmap.ic_launcher;
    }

    public PointOfInterest(Parcel in){
        this.name = in.readString();
        this.id = in.readString();
        //TODO: DoubleCheck:
        this.latLng = new LatLng(in.readDouble(), in.readDouble());
        this.distance = in.readFloat();
        this.address = in.readString();
        this.phonenumber = in.readString();
        this.websiteUri = Uri.parse(in.readString());
        this.placeTypes = in.readArrayList(Integer.class.getClassLoader());
        this.img = in.readInt();
    }

    // TODO: Testing purpose
    public  PointOfInterest(){
        this.id = "";
        this.latLng = new LatLng(0.0,0.0);
        this.distance = -1;
        this.name = "";
        this.placeTypes = new ArrayList<>();

        this.address = "";
        this.phonenumber = "";
        this.websiteUri = Uri.parse("http://www.google.com");

        //TODO: Replace Placeholder img
        this.img = R.mipmap.ic_launcher;
    }

    /**
     * returns the id of the POI-Object
     * @return id as String
     */
    public String getId() {
        if (id != null){
            return id;
        } else {
            return "";
        }
    }

    /**
     * returns the image of the POI-Object
     * @return img as integer
     */
    public int getImg() {
        return img;
    }

    /**
     * returns the latitude and longitude of the POI-Object
     * @return LatLng Object
     */
    public LatLng getLatLng() {

        if (latLng != null){
            return latLng;
        } else {
            return new LatLng(0.0, 0.0);
        }
    }

    /**
     * returns the name of the POI-Object
     * @return name as a string
     */
    public String getName() {
        if (name != null){
            return name;
        } else {
            return "";
        }
    }

    /**
     * returns a list of integers, which represents the type of the POI-Object
     * @return list of integers
     */
    public List<Integer> getPlaceTypes() {
        if (placeTypes != null){
            return placeTypes;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * returns the website uri of the POI-Object
     * @return website uri
     */
    public Uri getWebsiteUri() {
        if (websiteUri != null){
            return websiteUri;
        } else {
            return Uri.parse("www.google.com");
        }
    }

    /**
     * Returns the distance between the last known location and the POI location
     * @return the distance in meters
     */
    public float getDistance() {
        return distance;
    }

    /**
     * sets the id of the Point of Interest
     * @param id string
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * sets the img of the Point of Interest
     * @param img as integer
     */
    public void setImg(int img) {
        this.img = img;
    }

    /**
     * sets the latitude and longitude of the Point of Interest
     * @param latLng object
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * sets the name of the Point of Interest
     * @param name as string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the place types of the Point of Interest
     * @param placeTypes list of integers
     */
    public void setPlaceTypes(List<Integer> placeTypes) {
        this.placeTypes = placeTypes;
    }

    /**
     * sets the website uri of the Point of Interest
     * @param websiteUri as uri
     */
    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    /**
     * sets the distance of the Point of Interest
     * @param distance as float in meters
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void addPlaceType(int id){
        if (!this.placeTypes.contains(id)){
            placeTypes.add(id);
        }
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    /**
     * to string method of POI-Object
     * @return string
     */
    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                ", distance=" + distance +
                ", phone=" + phonenumber +
                ", websiteUri=" + websiteUri +
                ", placeTypes=" + placeTypes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeDouble(latLng.latitude);
        dest.writeDouble(latLng.longitude);
        dest.writeFloat(distance);
        dest.writeString(address);
        dest.writeString(phonenumber);
        dest.writeString(websiteUri.toString());
        dest.writeList(placeTypes);
        dest.writeInt(img);
    }

    public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>()
    {
        public PointOfInterest createFromParcel(Parcel in)
        {
            return new PointOfInterest(in);
        }
        public PointOfInterest[] newArray(int size)
        {
            return new PointOfInterest[size];
        }
    };

}
