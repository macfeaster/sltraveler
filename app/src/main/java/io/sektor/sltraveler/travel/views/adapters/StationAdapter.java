package io.sektor.sltraveler.travel.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import io.sektor.sltraveler.R;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public class StationAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> headers;
    private Map<String, List<StopLocation>> children;

    public StationAdapter(Context context, List<String> headers, Map<String, List<StopLocation>> children) {
        this.context = context;
        this.headers = headers;
        this.children = children;
    }

    public void replaceData(List<String> headers, Map<String, List<StopLocation>> children) {
        this.headers = headers;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<StopLocation> stops = children.get(headers.get(groupPosition));
        return stops == null ? 0 : stops.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<StopLocation> stops = children.get(headers.get(groupPosition));
        if (stops == null) return null;

        return stops.get(childPosition);
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
        StopLocation child = (StopLocation) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.departure_list_item, null);
        }

        TextView name = view.findViewById(R.id.station_name);
        TextView distance = view.findViewById(R.id.station_distance);
        ImageView star = view.findViewById(R.id.station_star_icon);

        name.setText(child.getName());
        distance.setText(view.getResources().getString(R.string.stop_distance_short, child.getDist()));
        star.setImageResource(R.drawable.ic_star_border);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
