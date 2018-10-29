package io.sektor.sltraveler.travel.views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import io.sektor.sltraveler.R;
import io.sektor.sltraveler.SLApplication;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.presenters.NowPresenter;
import io.sektor.sltraveler.travel.views.adapters.DepartureAdapter;

public class NowFragment extends Fragment implements NowContract.View {

    private NowContract.Presenter presenter;
    private ExpandableListView departuresList;
    private DepartureAdapter departureAdapter;
    private ConstraintLayout emptyList;

    public NowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        departureAdapter = new DepartureAdapter(getContext(), new ArrayList<TransportMode>(), new HashMap<TransportMode, List<Departure>>());
        SLApplication app = (SLApplication) getActivity().getApplication();

        NowPresenter presenter = new NowPresenter(app.getDeparturesService(),
                app.getNearbyStopsService(),
                app.getRTKey(),
                app.getNBSKey(),
                this);

        this.setPresenter(presenter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_now, container, false);

        // Set up Departures list
        departuresList = v.findViewById(R.id.now_departures_list);
        departuresList.setAdapter(departureAdapter);
        emptyList = v.findViewById(R.id.now_departures_list_empty);

        LinearLayout closestStop = v.findViewById(R.id.closest_stop);
        closestStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showPicker();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        // TODO
    }

    @Override
    public void showDepartures(List<TransportMode> headers, Map<TransportMode, List<Departure>> departures) {
        departureAdapter.replaceData(headers, departures);

        for (int i = 0; i < departureAdapter.getGroupCount(); i++)
            departuresList.expandGroup(i);

        departuresList.setVisibility(View.VISIBLE);
        emptyList.setVisibility(View.GONE);
    }

    @Override
    public void showNoDepartures() {
        emptyList.setVisibility(View.VISIBLE);
        departuresList.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingDeparturesError() {
        // TODO
    }

    @Override
    public void showNearbyStop(String stopName, String stopDistance) {
        TextView stopNameView = getView().findViewById(R.id.stop_name);
        TextView stopDistanceView = getView().findViewById(R.id.stop_distance);

        stopNameView.setText(stopName);
        stopDistanceView.setText(getString(R.string.stop_distance, stopDistance));
    }

    @Override
    public void launchPicker() {
        Intent intent = new Intent(getActivity(), PickerActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPresenter(@NonNull NowContract.Presenter presenter) {
        this.presenter = presenter;
    }

    void setLocation(Location location) {
        presenter.updateLocation(location.getLatitude(), location.getLongitude());
    }
}
