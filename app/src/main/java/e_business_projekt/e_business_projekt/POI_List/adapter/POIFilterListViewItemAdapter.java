package e_business_projekt.e_business_projekt.poi_list.adapter;

import android.content.Context;
import android.nfc.Tag;
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

    private static List<String> filterList;
    private List<String> selected = new ArrayList<>();

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
        final ViewHolder holder;

        if (convertView == null){

            convertView = mInflater.inflate(R.layout.listview_item_filter_type, null);

            holder = new ViewHolder();
            holder.filterName = (TextView) convertView.findViewById(R.id.textViewFilterType);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxFilterType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.filterName.setText(filterList.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected.add(holder.filterName.getText().toString());
                }else{
                    selected.remove(holder.filterName.getText().toString());
                }
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView filterName;
        CheckBox checkBox;
    }

    public List<String> getSelected(){
        return selected;
    }


}
