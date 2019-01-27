package io.sektor.sltraveler.travel.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public class NowPresenter implements NowContract.Presenter {

    private final NowContract.View nowView;
    private final ApplicationState appState;

    private double latitude;
    private double longitude;

    private StopLocation stop = null;
    private boolean userSelectedSite = false;

    public NowPresenter(ApplicationState appState, NowContract.View nowView) {
        this.appState = appState;
        this.nowView = nowView;

        nowView.setPresenter(this);
    }

    @Override
    public void loadNearbyStops() {
        if (userSelectedSite) {
            loadDepartures(false);
            return;
        }

        appState.getStopsRepository()
                .loadNearbyStops("" + latitude, "" + longitude)
                .subscribe(new MaybeObserver<List<StopLocation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        nowView.setLoadingIndicator(true);
                    }

                    @Override
                    public void onSuccess(List<StopLocation> stops) {
                        boolean changed = Objects.equals(stop, stops.get(0));
                        selectSite(stops.get(0), false);
                        loadDepartures(changed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        nowView.setLoadingIndicator(false);
                        nowView.showLoadingDeparturesError();
                        Log.e(getClass().getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        nowView.setLoadingIndicator(false);
                        nowView.showNoDepartures();
                        Log.i(getClass().getSimpleName(), "Pretty sure it was empty eh?");
                    }
                });
    }

    @Override
    public void loadDepartures(boolean forceUpdate) {
        appState.getDeparturesRepository()
                .loadDepartures(stop.getSiteId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<TreeMap<TransportMode, List<Departure>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        nowView.setLoadingIndicator(true);
                    }

                    @Override
                    public void onSuccess(TreeMap<TransportMode, List<Departure>> departures) {
                        nowView.setLoadingIndicator(false);
                        nowView.showDepartures(new ArrayList<>(departures.keySet()), departures);
                    }

                    @Override
                    public void onError(Throwable e) {
                        nowView.setLoadingIndicator(false);
                        nowView.showLoadingDeparturesError();
                        Log.e(getClass().getSimpleName(), e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        nowView.setLoadingIndicator(false);
                        nowView.showNoDepartures();
                        Log.i(getClass().getSimpleName(), "Pretty sure it was empty eh?");
                    }
                });
    }

    @Override
    public void showPicker() {
        nowView.launchPicker();
    }

    @Override
    public void selectSite(StopLocation stop, boolean userSelectedSite) {
        if (stop == null)
            throw new IllegalArgumentException("Selected stop cannot be null");

        if (userSelectedSite && !Objects.equals(this.stop, stop)) {
            appState.getDeparturesRepository().cleanCache();
        }

        this.userSelectedSite = userSelectedSite;
        this.stop = stop;
        nowView.setLoadingIndicator(false);
        nowView.showNearbyStop(stop.getName(), stop.getDist());
        // loadDepartures(userSelectedSite);
    }

    @Override
    public StopLocation getStop() {
        return userSelectedSite ? stop : null;
    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        // If we've moved, invalidate cache
        boolean changed = latitude != this.latitude && longitude != this.longitude;

        System.err.println("------------- updateLocation called!");

        this.latitude = latitude;
        this.longitude = longitude;

        if (changed) {
            appState.getStopsRepository().cleanCache(true);
            loadNearbyStops();
        }
    }

    @Override
    public void start() {
        // If we already have a location, hit the cache
        // Otherwise, wait for location data
        if (latitude != 0 && longitude != 0)
            loadNearbyStops();
    }
}
