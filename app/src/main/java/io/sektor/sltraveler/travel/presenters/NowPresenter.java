package io.sektor.sltraveler.travel.presenters;

import androidx.annotation.NonNull;
import io.sektor.sltraveler.travel.contracts.NowContract;
import io.sektor.sltraveler.travel.models.services.DeparturesService;

public class NowPresenter implements NowContract.Presenter {

    private final DeparturesService departuresService;

    private final NowContract.View nowView;

    public NowPresenter(@NonNull DeparturesService departuresService, @NonNull NowContract.View nowView) {
        this.departuresService = departuresService;
        this.nowView = nowView;

        nowView.setPresenter(this);
    }

    @Override
    public void loadDepartures(boolean forceUpdate) {

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
