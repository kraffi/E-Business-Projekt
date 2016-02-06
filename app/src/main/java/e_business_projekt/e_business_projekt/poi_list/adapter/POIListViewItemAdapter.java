package e_business_projekt.e_business_projekt.poi_list.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.location.places.Place;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 10.12.2015.
 */
public class POIListViewItemAdapter extends BaseAdapter {

    private List<PointOfInterest> poiList;
    private LayoutInflater mInflater;

    public POIListViewItemAdapter(Context ListViewSection, List<PointOfInterest> results){
        this.poiList = results;
        this.mInflater = LayoutInflater.from(ListViewSection);
    }

    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listview_item_poi, null);
            holder = new ViewHolder(convertView);
            holder.title = (TextView) convertView.findViewById(R.id.textViewItemTitle);
            holder.info = (TextView) convertView.findViewById(R.id.textViewItemInfo);
            holder.img = (ImageView) convertView.findViewById(R.id.itemImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //convert distance to km
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        double dist = poiList.get(position).getDistance()/1000;
        String types = poiList.get(position).getPlaceTypes().toString();

        holder.title.setText(poiList.get(position).getName());
        holder.info.setText("Distanz: " + df.format(dist) + " km \n" + types);
        //rn: TODO replace with Placeholder
        holder.img.setImageResource(poiList.get(position).getImg());

        holder.getAddPoiButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POIRouteProvider routeManager = POIRouteProvider.getInstance();
                PointOfInterest poi = poiList.get(position);
                routeManager.addPoiToActiveRoute(poi);
                Toast.makeText(mInflater.getContext(), poi.getName() + "added to Route", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    static class ViewHolder{
        View base;
        TextView title, info;
        ImageView img;
        ImageButton addPoiButton;

        public ViewHolder(View base) {
            this.base = base;
        }

        public ImageButton getAddPoiButton(){
            if (addPoiButton == null) {
                addPoiButton = (ImageButton) base.findViewById(R.id.addPoiButton);
            }
            return (addPoiButton);
        }
    }
}
