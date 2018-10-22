
package io.sektor.sltraveler.travel.models.results.nearbystops;

import java.util.List;

public class LocationList {

    private String noNamespaceSchemaLocation;
    private List<StopLocation> stopLocation = null;

    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }

    public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }

    public List<StopLocation> getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(List<StopLocation> stopLocation) {
        this.stopLocation = stopLocation;
    }

}
