
package io.sektor.sltraveler.travel.models.results.nearbystops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationList {

    private List<StopLocation> stopLocation = null;

    public List<StopLocation> getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(List<StopLocation> stopLocation) {
        this.stopLocation = stopLocation;
    }

}
