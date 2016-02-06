package e_business_projekt.e_business_projekt.route_list.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.route_list.POIRoute;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class RouteListViewItemAdapter extends BaseAdapter{

    private List<POIRoute> listOfPOIRoutes;
    private LayoutInflater mInflater;
    private RouteListViewItemCallback callback;

    public RouteListViewItemAdapter(RouteListViewItemCallback callback, Context ListViewSection, List<POIRoute> listOfPOIRoutes) {
        this.listOfPOIRoutes = listOfPOIRoutes;
        this.mInflater = LayoutInflater.from(ListViewSection);
        this.callback = callback;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listview_item_route, null);
            holder = new ViewHolder(convertView);
            holder.routeName = (TextView) convertView.findViewById(R.id.textViewRoute);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.routeName.setText(listOfPOIRoutes.get(position).getRouteName());
        if (listOfPOIRoutes.get(position).isActivated()){
            convertView.setBackgroundResource(R.color.routeSelected);
        }

        holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("ADAPTER:", "Delete Button " + position + " clicked");



                callback.deleteRouteButtonCallback(position);
            }
        });

        holder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("ADAPTER:", "Edit Button " + position + " clicked");



                callback.editRouteButtonCallback(position);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        View base;
        TextView routeName;
        ImageButton deleteButton;
        ImageButton editButton;

        public ViewHolder(View base) {
            this.base = base;
        }

        public ImageButton getDeleteButton() {
            if (deleteButton == null) {
                deleteButton = (ImageButton) base.findViewById(R.id.deleteRouteButton);
            }
            return (deleteButton);
        }

        public ImageButton getEditButton() {
            if (editButton == null) {
                editButton = (ImageButton) base.findViewById(R.id.editRouteButton);
            }
            return (editButton);
        }
    }
}
