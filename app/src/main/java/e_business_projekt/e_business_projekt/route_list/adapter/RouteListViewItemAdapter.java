package e_business_projekt.e_business_projekt.route_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.route_list.POIRoute;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class RouteListViewItemAdapter extends BaseAdapter {

    private List<POIRoute> listOfPOIRoutes;
    private LayoutInflater mInflater;

    public RouteListViewItemAdapter(Context ListViewSection, List<POIRoute> listOfPOIRoutes) {
        this.listOfPOIRoutes = listOfPOIRoutes;
        this.mInflater = LayoutInflater.from(ListViewSection);
    }

    @Override
    public int getCount() {
        return listOfPOIRoutes.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfPOIRoutes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listview_item_route, null);
            holder = new ViewHolder();
            holder.routeName = (TextView) convertView.findViewById(R.id.textViewRoute);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.routeName.setText(listOfPOIRoutes.get(position).getRouteName());
        return convertView;
    }

    static class ViewHolder{
        TextView routeName;
    }
}
