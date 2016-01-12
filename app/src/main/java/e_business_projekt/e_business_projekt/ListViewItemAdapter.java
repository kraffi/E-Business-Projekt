package e_business_projekt.e_business_projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by RaulVinhKhoa on 10.12.2015.
 */
public class ListViewItemAdapter extends BaseAdapter {

    private static ArrayList<PointOfInterest> poiList;

    private LayoutInflater mInflater;

    public ListViewItemAdapter(Context ListViewSection, ArrayList<PointOfInterest> results){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.info = (TextView) convertView.findViewById(R.id.itemInfo);
            holder.img = (ImageView) convertView.findViewById(R.id.itemImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(poiList.get(position).getName());
        holder.info.setText(poiList.get(position).getId());
        //rn: TODO replace with Placeholder
        holder.img.setImageResource(poiList.get(position).getImg());

        return convertView;
    }

    static class ViewHolder{
        TextView title, info;
        ImageView img;
    }
}
