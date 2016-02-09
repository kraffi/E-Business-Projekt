package e_business_projekt.e_business_projekt.poi_list.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaulVinhKhoa on 01.02.2016.
 */
public class POIFilterListViewItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<String> filterListNames;
    private List<int[]> filterList;
    private List<int[]> selectedFilter = new ArrayList<>();
    private List<Integer> checks = new ArrayList<>();

    public POIFilterListViewItemAdapter(Context ListViewSection, List<String> filterListNames, List<int[]> filterList, List<Integer> checks) {
        this.filterListNames = filterListNames;
        this.filterList = filterList;
        this.mInflater = LayoutInflater.from(ListViewSection);
        this.checks = checks;
    }

    @Override
    public int getCount() {
        return filterListNames.size();
    }

    @Override
    public Object getItem(int position) {
        return filterListNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){

            convertView = mInflater.inflate(R.layout.listview_item_filter_type, null);

            holder = new ViewHolder();
            holder.filterName = (TextView) convertView.findViewById(R.id.textViewFilterType);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxFilterType);

            if (checks.contains(position)){
                holder.checkBox.setChecked(true);
                if (!selectedFilter.contains(filterList.get(position))){
                    selectedFilter.add(filterList.get(position));
                }
            } else {
                holder.checkBox.setChecked(false);
                if (selectedFilter.contains(filterList.get(position))){
                    selectedFilter.remove(filterList.get(position));
                }
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.filterName.setText(filterListNames.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectedFilter.contains(filterList.get(position))){
                        selectedFilter.add(filterList.get(position));
                    }
                }else{
                    if (selectedFilter.contains(filterList.get(position))){
                        selectedFilter.remove(filterList.get(position));
                    }
                }
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView filterName;
        CheckBox checkBox;
    }

    public List<int[]> getSelectedFilter(){
        //Log.i("TEST:", "selectedFilter: " + selectedFilter.toString());
        return selectedFilter;
    }



}
