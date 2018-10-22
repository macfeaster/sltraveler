package io.sektor.sltraveler.travel.models.services;

import io.reactivex.Single;
import io.sektor.sltraveler.travel.models.results.NearbyStops;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeparturesService {

    @GET("api2/realtimedeparturesV4.json?key={keyRT}&siteid={siteID}&timewindow=30")
    Single<NearbyStops> departures(@Path("keyNBS") String nbsApiKey, @Path("lat") String latitude, @Path("long") String longitude);

}
