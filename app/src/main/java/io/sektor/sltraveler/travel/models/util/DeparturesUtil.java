package io.sektor.sltraveler.travel.models.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.models.results.departures.ResponseData;

public class DeparturesUtil {

    public static Map<TransportMode, List<Departure>> getRealtimeMap(Departures departures) {
        Map<TransportMode, List<Departure>> data = new HashMap<>();
        ResponseData rd = departures.getResponseData();
        if (rd == null) return data;

        if (hasItems(rd.getTrains())) data.put(TransportMode.TRAIN, rd.getTrains());
        if (hasItems(rd.getMetros())) data.put(TransportMode.METRO, rd.getMetros());
        if (hasItems(rd.getTrams()))  data.put(TransportMode.TRAM,  rd.getTrams());
        if (hasItems(rd.getBuses()))  data.put(TransportMode.BUS,   rd.getBuses());
        if (hasItems(rd.getShips()))  data.put(TransportMode.SHIP,  rd.getShips());

        return data;
    }

    public static List<TransportMode> getRealtimeHeaders(Departures departures) {
        List<TransportMode> headers = new ArrayList<>();
        ResponseData rd = departures.getResponseData();
        if (rd == null) return headers;

        if (hasItems(rd.getTrains())) headers.add(TransportMode.TRAIN);
        if (hasItems(rd.getMetros())) headers.add(TransportMode.METRO);
        if (hasItems(rd.getTrams()))  headers.add(TransportMode.TRAM);
        if (hasItems(rd.getBuses()))  headers.add(TransportMode.BUS);
        if (hasItems(rd.getShips()))  headers.add(TransportMode.SHIP);

        return headers;
    }

    private static boolean hasItems(List list) {
        return list != null && !list.isEmpty();
    }

}
