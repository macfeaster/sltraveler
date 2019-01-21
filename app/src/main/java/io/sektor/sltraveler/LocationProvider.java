package io.sektor.sltraveler;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.subjects.PublishSubject;

public class LocationProvider extends LocationCallback implements OnSuccessListener<Location> {

    private static final String LOG_TAG = "LocationProvider";
    private PublishSubject<Location> locationSubject;

    public LocationProvider(PublishSubject<Location> locationSubject) {
        super();
        this.locationSubject = locationSubject;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        Log.d(LOG_TAG, "LocationProvider got onLocationResult");

        if (locationResult.getLastLocation() != null)
            locationSubject.onNext(locationResult.getLastLocation());
    }

    @Override
    public void onSuccess(Location location) {
        Log.d(LOG_TAG, "LocationProvider got onSuccess");

        if (location != null)
            locationSubject.onNext(location);
    }
}
