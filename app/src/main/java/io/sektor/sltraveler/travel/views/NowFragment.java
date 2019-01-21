package io.sektor.sltraveler.travel.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.R;
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
    private View v;

    private Disposable disposable;
    private ApplicationState appState;
    private View loadingList;

    public NowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        departureAdapter = new DepartureAdapter(getContext(), new ArrayList<>(), new HashMap<>());
        appState = ApplicationState.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_now, container, false);

        // Set up Departures list
        departuresList = v.findViewById(R.id.now_departures_list);
        departuresList.setAdapter(departureAdapter);
        emptyList = v.findViewById(R.id.now_departures_list_empty);
        loadingList = v.findViewById(R.id.now_departures_list_loading);

        LinearLayout closestStop = v.findViewById(R.id.closest_stop);
        closestStop.setOnClickListener(v -> presenter.showPicker());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        final NowPresenter presenter = new NowPresenter(appState, this);

        disposable = appState.getLocationSubject().subscribe(
                location -> presenter.updateLocation(location.getLatitude(), location.getLongitude()),
                throwable -> {
                    showLoadingDeparturesError();
                    Log.e(getClass().getSimpleName(), throwable.getMessage());
                });

        this.setPresenter(presenter);
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        int showDepartures = active ? View.GONE : View.VISIBLE;
        int showSpinner = active ? View.VISIBLE : View.GONE;

        departuresList.setVisibility(showDepartures);
        loadingList.setVisibility(showSpinner);
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
        if (v == null)
            Toast.makeText(null, "view is null!", Toast.LENGTH_SHORT).show();
        else
            Snackbar.make(v, "Error fetching departures", Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNearbyStop(String stopName, String stopDistance) {
        if (v == null) {
            Log.e(this.getClass().getSimpleName(), "view is null, cannot show nearby stop, not inflated yet?");
            return;
        }

        TextView stopNameView = v.findViewById(R.id.stop_name);
        TextView stopDistanceView = v.findViewById(R.id.stop_distance);

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

}
