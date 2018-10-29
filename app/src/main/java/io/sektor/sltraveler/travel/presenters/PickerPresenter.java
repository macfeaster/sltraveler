package io.sektor.sltraveler.travel.presenters;

import io.sektor.sltraveler.travel.contracts.PickerContract;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;
import io.sektor.sltraveler.travel.models.services.NearbyStopsService;

public class PickerPresenter implements PickerContract.Presenter {

    private NearbyStopsService nearbyStopsService;
    private String nbsApiKey;
    private PickerContract.View pickerView;

    public PickerPresenter(NearbyStopsService nearbyStopsService, String nbsApiKey, PickerContract.View pickerView) {
        this.nearbyStopsService = nearbyStopsService;
        this.nbsApiKey = nbsApiKey;
        this.pickerView = pickerView;
    }

    @Override
    public void loadStopsAndFavorites() {

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

    }
}
