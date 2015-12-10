package e_business_projekt.e_business_projekt;

import android.util.Log;
import android.widget.ImageView;

/**
 * Created by RaulVinhKhoa on 10.12.2015.
 */
public class PointOfInterest {

    String title;
    String info;
    int img;

    // constructor for PointOfInterest
    public PointOfInterest(String title, String info) {
        this.title = title;
        this.info = info;
        this.img = R.mipmap.ic_launcher;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public int getImg() {
        return img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
