package e_business_projekt.e_business_projekt.route_list.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import e_business_projekt.e_business_projekt.R;
import e_business_projekt.e_business_projekt.poi_list.PointOfInterest;

import java.util.List;

/**
 * Created by RaulVinhKhoa on 05.02.2016.
 */
public class EditRouteListViewItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PointOfInterest> poiList;
    private EditRouteListViewItemAdapterCallback callback;

    public EditRouteListViewItemAdapter(EditRouteListViewItemAdapterCallback callback, Context ListViewSection, List<PointOfInterest> poiList) {
        this.callback = callback;
        this.mInflater = LayoutInflater.from(ListViewSection);
        this.poiList = poiList;
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

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_edit_route, null);
            holder = new ViewHolder(convertView);
            holder.poiName = (TextView) convertView.findViewById(R.id.textViewRoutePOI);

            holder.poiName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.clickPoi(position);
                }
            });
            holder.getRemoveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.removePoiFromRouteButton(position);
                }
            });
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.poiName.setText(poiList.get(position).getName());

        return convertView;
    }



    static class ViewHolder{
        View base;
        TextView poiName;
        ImageButton removeButton;

        public ViewHolder(View base) {
            this.base = base;
        }

        public ImageButton getRemoveButton() {
            if (removeButton == null){
                removeButton = (ImageButton) base.findViewById(R.id.removePOIButton);
            }
            return removeButton;
        }
    }

}
