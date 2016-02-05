package e_business_projekt.e_business_projekt.route_list.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class EditRouteListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PointOfInterest> poiList;

    public EditRouteListViewAdapter(Context ListViewSection, List<PointOfInterest> poiList) {
        this.mInflater = LayoutInflater.from(ListViewSection);
        this.poiList = poiList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_edit_route, null);
            holder = new ViewHolder();
            holder.poiName = (TextView) convertView.findViewById(R.id.textViewRoutePOI);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.poiName.setText(poiList.get(position).getName());

        return convertView;
    }

    static class ViewHolder{
        TextView poiName;

    }

}
