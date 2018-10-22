package io.sektor.sltraveler.travel.contracts;

import java.util.List;
import java.util.Map;

import io.sektor.sltraveler.BasePresenter;
import io.sektor.sltraveler.BaseView;
import io.sektor.sltraveler.travel.models.results.departures.Departure;

public interface NowContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showDepartures(List<String> headers, Map<String, List<Departure>> departures);

        void showNoDepartures();

        void showLoadingDeparturesError();

    }

    interface Presenter extends BasePresenter {

        void loadDepartures(boolean forceUpdate);

        void showPicker();

        void result(int requestCode, int resultCode);

    }

}
