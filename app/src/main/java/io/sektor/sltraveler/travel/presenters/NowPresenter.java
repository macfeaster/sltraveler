package io.sektor.sltraveler.travel.presenters;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.results.NearbyStops;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;
import io.sektor.sltraveler.travel.models.services.DeparturesService;
import io.sektor.sltraveler.travel.models.services.NearbyStopsService;
import io.sektor.sltraveler.travel.models.util.DeparturesUtil;

public class NowPresenter implements NowContract.Presenter {

    private final DeparturesService departuresService;
    private final NearbyStopsService nearbyStopsService;
    private final String rtApiKey;
    private final String nbsApiKey;
    private final NowContract.View nowView;

    private double latitude;
    private double longitude;

    private StopLocation stop = null;
    private boolean userSelectedSite = false;

    public NowPresenter(DeparturesService departuresService, NearbyStopsService nearbyStopsService, String rtApiKey, String nbsApiKey, NowContract.View nowView) {
        this.departuresService = departuresService;
        this.nearbyStopsService = nearbyStopsService;
        this.rtApiKey = rtApiKey;
        this.nbsApiKey = nbsApiKey;
        this.nowView = nowView;

        nowView.setPresenter(this);
    }

    @Override
    public void loadNearbyStops() {
        nearbyStopsService.nearbyStops(nbsApiKey, "" + latitude, "" + longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NearbyStops>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        nowView.setLoadingIndicator(true);
                    }

                    @Override
                    public void onSuccess(NearbyStops nearbyStops) {
                        List<StopLocation> list = nearbyStops.getLocationList().getStopLocation();
                        if (list == null || list.size() == 0) {
                            nowView.showNoDepartures();
                            return;
                        }

                        stop = list.get(0);
                        nowView.setLoadingIndicator(false);
                        nowView.showNearbyStop(stop.getName(), stop.getDist());
                        loadDepartures(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        nowView.setLoadingIndicator(false);
                        nowView.showLoadingDeparturesError();
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void loadDepartures(boolean forceUpdate) {
        departuresService.departures(rtApiKey, stop.getSiteId())
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
        if (stop == null)
            nowView.showNoDepartures();
        else
            nowView.showNearbyStop(stop.getName(), stop.getDist());
    }
}
