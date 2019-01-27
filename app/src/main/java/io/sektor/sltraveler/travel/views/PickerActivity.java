package io.sektor.sltraveler.travel.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.appcompat.app.AppCompatActivity;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.R;
import io.sektor.sltraveler.travel.contracts.PickerContract;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;
import io.sektor.sltraveler.travel.presenters.PickerPresenter;
import io.sektor.sltraveler.travel.views.adapters.StationAdapter;

public class PickerActivity extends AppCompatActivity implements PickerContract.View, ExpandableListView.OnChildClickListener {

    private static final String LOG_TAG = "PickerActivity";

    private PickerContract.Presenter presenter;
    private ApplicationState appState;
    private StationAdapter stationAdapter;
    private ExpandableListView stationList;
    private List<String> headers;
    private Map<String, List<StopLocation>> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        appState = ApplicationState.getInstance();

        headers = new ArrayList<>();
        headers.add(getString(R.string.favorites));
        headers.add(getString(R.string.nearby_stops));

        children = new TreeMap<>();
        stationAdapter = new StationAdapter(this, headers, children, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set up Departures list
        stationList = findViewById(R.id.picker_list);
        stationList.setAdapter(stationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final PickerPresenter presenter = new PickerPresenter(appState, this);

        this.setPresenter(presenter);
        presenter.start();
    }

    @Override
    public void showFavorites(List<StopLocation> favorites) {

    }

    @Override
    public void showNearbyStops(List<StopLocation> stops) {
        children.put(getString(R.string.nearby_stops), stops);
        stationAdapter.replaceData(headers, children);

        for (int i = 0; i < stationAdapter.getGroupCount(); i++)
            stationList.expandGroup(i);

        stationList.setVisibility(View.VISIBLE);
        // emptyList.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(PickerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Log.e(LOG_TAG, "Child clicked: " + childPosition);
        String lookupKey = groupPosition == 0 ? getString(R.string.favorites) : getString(R.string.nearby_stops);
        List<StopLocation> stops = children.get(lookupKey);

        if (stops == null || stops.get(childPosition) == null)
            throw new IllegalArgumentException("Trying to lookup non-existent station.");

        StopLocation stop = stops.get(childPosition);
        Log.e(LOG_TAG, "Sending back stop: " + stop);

        Intent data = new Intent();
        data.putExtra("stop", stop);
        setResult(RESULT_OK, data);
        finish();

        return true;
    }
}
