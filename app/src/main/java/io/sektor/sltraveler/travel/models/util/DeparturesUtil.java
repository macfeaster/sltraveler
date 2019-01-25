package io.sektor.sltraveler.travel.models.util;

import java.util.List;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.models.results.departures.ResponseData;

public class DeparturesUtil {

    @NonNull
    public static TreeMap<TransportMode, List<Departure>> getRealtimeMap(Departures departures) {
        TreeMap<TransportMode, List<Departure>> data = new TreeMap<>();
        ResponseData rd = departures.getResponseData();
        if (rd == null) return data;

        if (hasItems(rd.getTrains())) data.put(TransportMode.TRAIN, rd.getTrains());
        if (hasItems(rd.getMetros())) data.put(TransportMode.METRO, rd.getMetros());
        if (hasItems(rd.getTrams()))  data.put(TransportMode.TRAM,  rd.getTrams());
        if (hasItems(rd.getBuses()))  data.put(TransportMode.BUS,   rd.getBuses());
        if (hasItems(rd.getShips()))  data.put(TransportMode.SHIP,  rd.getShips());

        return data;
    }

    private static boolean hasItems(List list) {
        return list != null && !list.isEmpty();
    }

}
