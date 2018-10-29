package io.sektor.sltraveler.travel.contracts;

import java.util.List;

import io.sektor.sltraveler.BasePresenter;
import io.sektor.sltraveler.BaseView;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public interface PickerContract {

    interface View extends BaseView<Presenter> {

        void showFavorites(List<StopLocation> favorites);

        void showNearbyStops(List<StopLocation> stops);

    }

    interface Presenter extends BasePresenter {

        void loadStopsAndFavorites();

        boolean isFavorite(StopLocation stop);

        void addFavorite(StopLocation stop);

        void removeFavorite(StopLocation stop);

    }
}
