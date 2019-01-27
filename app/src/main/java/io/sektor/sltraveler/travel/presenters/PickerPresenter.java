package io.sektor.sltraveler.travel.presenters;

import android.util.Log;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.ApplicationState;
import io.sektor.sltraveler.travel.contracts.PickerContract;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public class PickerPresenter implements PickerContract.Presenter {

    private static final String LOG_TAG = "PickerPresenter";
    private final ApplicationState appState;
    private PickerContract.View pickerView;

    public PickerPresenter(ApplicationState appState, PickerContract.View pickerView) {
        this.appState = appState;
        this.pickerView = pickerView;
    }

    @Override
    public void loadStopsAndFavorites() {
        appState.getStopsRepository()
                .loadStopsLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<StopLocation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<StopLocation> stops) {
                        Log.d(LOG_TAG, "Presenter got " + stops.size() + " stations from repo");
                        pickerView.showNearbyStops(stops);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "loadStopsLocal errored:", e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "Received no stops from local cache.");
                    }
                });
    }

    @Override
    public boolean isFavorite(StopLocation stop) {
        return false;
    }

    @Override
    public void addFavorite(StopLocation stop) {

    }

    @Override
    public void removeFavorite(StopLocation stop) {

    }

    @Override
    public void start() {
        loadStopsAndFavorites();
    }
}
