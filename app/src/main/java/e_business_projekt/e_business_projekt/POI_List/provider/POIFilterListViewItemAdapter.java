package e_business_projekt.e_business_projekt.poi_list.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 01.02.2016.
 */
public class POIFilterListViewItemAdapter extends BaseAdapter {

    private static List<String> filterList;

    private LayoutInflater mInflater;

    public POIFilterListViewItemAdapter(Context ListViewSection, List<String> filterList) {
        this.filterList = filterList;
        this.mInflater = LayoutInflater.from(ListViewSection);
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){

            convertView = mInflater.inflate(R.layout.listview_types, null);

            holder = new ViewHolder();
            holder.filterName = (TextView) convertView.findViewById(R.id.textViewFilterType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.filterName.setText(filterList.get(position));

        return convertView;
    }

    static class ViewHolder{
        TextView filterName;
    }
}
