
package io.sektor.sltraveler.travel.models.results;


import io.sektor.sltraveler.travel.models.results.nearbystops.LocationList;

public class NearbyStops {

    private LocationList locationList;

    public LocationList getLocationList() {
        return locationList;
    }

    public void setLocationList(LocationList locationList) {
        this.locationList = locationList;
    }

}
