package io.sektor.sltraveler.travel.views;

import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.sektor.sltraveler.R;
import io.sektor.sltraveler.travel.contracts.PickerContract;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

public class PickerActivity extends AppCompatActivity implements PickerContract.View {

    private PickerContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
    }

    @Override
    public void showFavorites(List<StopLocation> favorites) {

    }

    @Override
    public void showNearbyStops(List<StopLocation> stops) {

    }

    @Override
    public void setPresenter(PickerContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
