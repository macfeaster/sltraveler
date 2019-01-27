package io.sektor.sltraveler.travel.models.repositories;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.TreeMap;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.travel.models.disk.DiskCache;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.models.util.DeparturesUtil;

public class DeparturesRepository {

    private final static String LOG_TAG = "DeparturesRepository";
    private final static String CACHE_NAME = "departures_cache";

    private final ApplicationState appState;
    private final DiskCache<TreeMap<TransportMode, List<Departure>>> diskCache;

    public DeparturesRepository(ApplicationState appState, Context context) {
        this.appState = appState;
        this.diskCache = new DiskCache<>(context, CACHE_NAME);
    }

    public void cleanCache() {
        TreeMap<TransportMode, List<Departure>> result = diskCache.forceCleanCacheBlocking();
        Log.d(LOG_TAG, "Force cleaned departures with result " + result);
    }

    public Maybe<TreeMap<TransportMode, List<Departure>>> loadDepartures(int siteId) {
        // Invalidate anything older than a minute
        return Maybe
                .concat(diskCache.cleanCache(60), diskCache.readCache(), loadStopsRemote(siteId))
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .firstElement();
    }

    private Maybe<TreeMap<TransportMode, List<Departure>>> loadStopsRemote(int id) {
        return appState
                .getDeparturesService()
                .departures(appState.getRTKey(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(DeparturesUtil::getRealtimeMap)
                .filter(map -> !map.keySet().isEmpty())
                .doAfterSuccess(departures -> diskCache
                        .persist(departures)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(e -> Log.e(LOG_TAG, e.getMessage()))
                        .subscribe(new MaybeObserver<TreeMap<TransportMode, List<Departure>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(LOG_TAG, "Post persist received event onSubscribe");
                            }

                            @Override
                            public void onSuccess(TreeMap<TransportMode, List<Departure>> ls) {
                                Log.d(LOG_TAG, "Post persist received event onSuccess: " + ls.size());
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

}
