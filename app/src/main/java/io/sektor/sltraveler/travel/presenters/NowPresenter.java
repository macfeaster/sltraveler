package io.sektor.sltraveler.travel.presenters;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.services.DeparturesService;
import io.sektor.sltraveler.travel.models.util.DeparturesUtil;

public class NowPresenter implements NowContract.Presenter {

    private final DeparturesService departuresService;
    private final String rtApiKey;
    private final NowContract.View nowView;
    private int siteId = 9202;

    public NowPresenter(DeparturesService departuresService, String rtApiKey, NowContract.View nowView) {
        this.departuresService = departuresService;
        this.rtApiKey = rtApiKey;
        this.nowView = nowView;

        nowView.setPresenter(this);
    }

    @Override
    public void loadDepartures(boolean forceUpdate) {
        departuresService.departures(rtApiKey, siteId)
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

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {
        loadDepartures(false);
    }
}
