package io.sektor.sltraveler.travel.models.services;

import io.reactivex.Single;
import io.sektor.sltraveler.travel.models.results.Departures;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DeparturesService {

    @GET("api2/realtimedeparturesV4.json")
    Single<Departures> departures(@Query("key") String rtApiKey, @Query("siteid") int id);

}
