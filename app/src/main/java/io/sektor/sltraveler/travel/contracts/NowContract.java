package io.sektor.sltraveler.travel.contracts;

import java.util.List;
import java.util.Map;

import io.sektor.sltraveler.BasePresenter;
import io.sektor.sltraveler.BaseView;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;

public interface NowContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showDepartures(List<TransportMode> headers, Map<TransportMode, List<Departure>> departures);

        void showNoDepartures();

        void showLoadingDeparturesError();

        void showNearbyStop(String stopName, String stopDistance);

        void launchPicker();
    }

    interface Presenter extends BasePresenter {

        void loadNearbyStops();

        void loadDepartures(boolean forceUpdate);

        void showPicker();

        void result(int requestCode, int resultCode);

        void updateLocation(double latitude, double longitude);

    }

}
