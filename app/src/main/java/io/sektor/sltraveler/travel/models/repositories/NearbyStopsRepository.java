package io.sektor.sltraveler.travel.models.repositories;

import android.util.Log;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
        return Maybe
                .concat(cleanCache(false), loadStopsLocal(), loadStopsRemote(latitude, longitude))
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .firstElement();
    }

    public Maybe<List<StopLocation>> cleanCache(boolean forceClean) {
        // Invalidate anything older than an hour
        long maxAgeSeconds = forceClean ? 0 : 3600;
        long maxAge = System.currentTimeMillis() / 1000L - maxAgeSeconds;

        MaybeOnSubscribe<List<StopLocation>> del = emitter -> {
            stopLocationDao.deleteAll(maxAge);
            emitter.onComplete();
        };

        return Maybe.create(del)
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(LOG_TAG, "Cleaned cache"));
    }

    private Maybe<List<StopLocation>> loadStopsRemote(String latitude, String longitude) {
        return appState
                .getNearbyStopsService()
                .nearbyStops(appState.getNBSKey(), latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(nbs -> nbs.getLocationList().getStopLocation())
                .filter(this::notEmpty)
                .doAfterSuccess(stopLocations -> stopLocationDao
                        .insertAll(stopLocations)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(e -> Log.e(LOG_TAG, e.getMessage()))
                        .subscribe(new MaybeObserver<List<Long>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(LOG_TAG, "Post persist received event onSubscribe");
                            }

                            @Override
                            public void onSuccess(List<Long> longs) {
                                Log.d(LOG_TAG, "Post persist received event onSuccess: " + longs);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(LOG_TAG, "Post persist received event onComplete");
                            }
                        }));
    }

    public Maybe<List<StopLocation>> loadStopsLocal() {
        return stopLocationDao
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(this::notEmpty)
                .doOnSuccess(l -> Log.d(LOG_TAG, "Hit cache: found " + l.size()));
    }

    private boolean notEmpty(List l) {
        return l != null && !l.isEmpty();
    }

}
