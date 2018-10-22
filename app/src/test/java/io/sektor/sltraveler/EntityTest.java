package io.sektor.sltraveler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import io.sektor.sltraveler.travel.models.results.Departures;
import io.sektor.sltraveler.travel.models.results.departures.Departure;
import io.sektor.sltraveler.travel.models.results.departures.Departure.TransportMode;
import io.sektor.sltraveler.travel.models.results.departures.ResponseData;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EntityTest {
    @Test
    public void departuresEntityTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        InputStream in = getClass().getClassLoader().getResourceAsStream("departures.json");
        InputStreamReader d = new InputStreamReader(in);

        Departures departures = mapper.readValue(d, Departures.class);

        assertEquals(0, (int) departures.getStatusCode());
        ResponseData data = departures.getResponseData();

        assertNotNull(data);
        assertNotNull(data.getLatestUpdate());
        assertNotNull(data.getTrains());
        assertNotNull(data.getBuses());

        Departure firstBus = data.getBuses().get(0);
        assertEquals(TransportMode.BUS, firstBus.getTransportMode());
        assertEquals("176", firstBus.getLineNumber());
        assertEquals(new Date(118, 9, 22, 22, 7, 0), firstBus.getExpectedDateTime());

        Departure lastTrain = data.getTrains().get(1);
        assertEquals("Pendeltåg", lastTrain.getGroupOfLine());
        assertNotNull(lastTrain.getDeviations());
        assertEquals("Resa förbi Arlanda C kräver både UL- och SL- biljett.", lastTrain.getDeviations().get(0).getText());
    }
}