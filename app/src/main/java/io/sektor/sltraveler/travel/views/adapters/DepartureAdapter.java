package io.sektor.sltraveler.travel.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import io.sektor.sltraveler.R;
import io.sektor.sltraveler.travel.models.results.departures.Departure;

public class DepartureAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> headers;
    private Map<String, List<Departure>> children;

    public DepartureAdapter(Context context, List<String> headers, Map<String, List<Departure>> children) {
        this.context = context;
        this.headers = headers;
        this.children = children;
    }

    public void replaceData(List<String> headers, Map<String, List<Departure>> children) {
        this.headers = headers;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Departure> departures = children.get(headers.get(groupPosition));
        return departures == null ? 0 : departures.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Departure> departures = children.get(headers.get(groupPosition));
        if (departures == null) return null;

        return departures.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }

        TextView listHeader = view.findViewById(R.id.departure_list_header);
        listHeader.setText(title);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        Departure child = (Departure) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.departure_list_item, null);
        }

        // TextView type = (TextView) convertView.findViewById(R.id.departure_item_type);
        TextView number = view.findViewById(R.id.departure_item_number);
        TextView destination = view.findViewById(R.id.departure_item_destination);
        TextView datetime = view.findViewById(R.id.departure_item_datetime);
        ImageView type = view.findViewById(R.id.departure_item_type);

        // type.setText(child.getType().toString());
        number.setText(child.getLineNumber());
        destination.setText(child.getDestination());
        datetime.setText("2 min");

        switch (child.getTransportMode()) {
            case TRAIN: type.setImageResource(R.drawable.ic_directions_railway); break;
            case BUS: type.setImageResource(R.drawable.ic_directions_bus); break;
            default: type.setImageResource(R.drawable.ic_directions); break;
        }

        ImageViewCompat.setImageTintList(type, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.traffic_type_tint)));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
