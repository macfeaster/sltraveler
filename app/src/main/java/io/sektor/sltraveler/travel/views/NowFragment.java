package io.sektor.sltraveler.travel.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import io.sektor.sltraveler.R;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
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
        departureAdapter = new DepartureAdapter(getContext(), new ArrayList<String>(), new HashMap<String, List<Departure>>());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_now, container, false);

        List<String> headers = Arrays.asList("Buss", "Pendeltåg");
        Map<String, List<Departure>> departures = new HashMap<>();
        departures.put("Buss", Arrays.asList(
                new Departure(Departure.TransportMode.BUS, "611", "Täby centrum", "2 min"),
                new Departure(Departure.TransportMode.BUS, "614B", "Centralen", "12 min")
        ));
        departures.put("Pendeltåg", Arrays.asList(
                new Departure(Departure.TransportMode.TRAIN, "36", "Södertälje C", "12 min"),
                new Departure(Departure.TransportMode.TRAIN, "36", "Tumba", "12 min"),
                new Departure(Departure.TransportMode.TRAIN, "38", "Märsta", "12 min"),
                new Departure(Departure.TransportMode.TRAIN, "38", "Uppsala C", "12 min"),
                new Departure(Departure.TransportMode.TRAIN, "38", "Södertälje C", "12 min")
        ));

        // Set up Departures list
        departuresList = v.findViewById(R.id.now_departures_list);
        departuresList.setAdapter(departureAdapter);
        emptyList = v.findViewById(R.id.now_departures_list_empty);

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
    public void showDepartures(List<String> headers, Map<String, List<Departure>> departures) {
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
    public void setPresenter(@NonNull NowContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
