package io.sektor.sltraveler.travel.models.repositories;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.travel.models.dao.StopLocationDao;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public class NearbyStopsRepository {

    private final static String LOG_TAG = "NearbyStopsRepository";

    private final ApplicationState appState;
    private final StopLocationDao stopLocationDao;

    public NearbyStopsRepository(ApplicationState appState) {
        this.appState = appState;
        stopLocationDao = appState.getDb().stopLocationDao();
    }

    public Maybe<List<StopLocation>> loadNearbyStops(String latitude, String longitude) {
        return Completable.fromAction(stopLocationDao::deleteAll)
                .subscribeOn(Schedulers.io())
                .toMaybe()
                .flatMap(next -> Maybe
                        .concat(loadStopsLocal(), loadStopsRemote(latitude, longitude))
                        .firstElement()
                        .doAfterSuccess(stopLocations -> stopLocationDao
                                .insertAll(stopLocations)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(e -> Log.e(LOG_TAG, e.getMessage()))
                                .subscribe()));
    }

    private Maybe<List<StopLocation>> loadStopsRemote(String latitude, String longitude) {
        return appState
                .getNearbyStopsService()
                .nearbyStops(appState.getNBSKey(), latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(nbs -> nbs.getLocationList().getStopLocation())
                .filter(this::notEmpty)
                .doAfterSuccess(ls -> Log.e(LOG_TAG, "Cached NBS response: " + ls.size()));
    }

    private Maybe<List<StopLocation>> loadStopsLocal() {
        return stopLocationDao
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(this::notEmpty)
                .doOnSuccess(l -> Log.e(LOG_TAG, "Hit cache: found " + l.size()));
    }

    private boolean notEmpty(List l) {
        return l != null && !l.isEmpty();
    }

}
