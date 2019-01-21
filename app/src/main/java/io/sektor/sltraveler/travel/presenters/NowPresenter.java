package io.sektor.sltraveler.travel.presenters;

import android.util.Log;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;
import io.sektor.sltraveler.travel.models.util.DeparturesUtil;

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
        appState.getStopsRepository()
                .loadNearbyStops("" + latitude, "" + longitude)
                .subscribe(new MaybeObserver<List<StopLocation>>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   nowView.setLoadingIndicator(true);
                               }

                               @Override
                               public void onSuccess(List<StopLocation> stops) {
                                   stop = stops.get(0);
                                   nowView.setLoadingIndicator(false);
                                   nowView.showNearbyStop(stop.getName(), stop.getDist());
                                   // loadDepartures(true);
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
         appState.getDeparturesService().departures(appState.getRTKey(), stop.getSiteId())
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new SingleObserver<Departures>() {
                     @Override
                     public void onSubscribe(Disposable d) {
                         nowView.setLoadingIndicator(true);
                     }

                     @Override
                     public void onSuccess(Departures departures) {
                         nowView.setLoadingIndicator(false);
                         nowView.showDepartures(DeparturesUtil.getRealtimeHeaders(departures), DeparturesUtil.getRealtimeMap(departures));
                     }

                     @Override
                     public void onError(Throwable e) {
                         nowView.setLoadingIndicator(false);
                         nowView.showLoadingDeparturesError();
                         Log.e(getClass().getSimpleName(), e.getMessage());
                         e.printStackTrace();
                     }
                 });
    }

    @Override
    public void showPicker() {
        nowView.launchPicker();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        loadNearbyStops();
    }

    @Override
    public void start() {
        // If we already have a location, hit the cache
        // Otherwise, wait for location data
        if (latitude != 0 && longitude != 0)
            loadNearbyStops();
    }
}
