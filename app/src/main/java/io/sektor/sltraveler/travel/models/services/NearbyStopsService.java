package io.sektor.sltraveler.travel.models.services;

import io.reactivex.Single;
import io.sektor.sltraveler.travel.models.results.NearbyStops;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NearbyStopsService {

    @GET("/api2/nearbystops.json?key={keyNBS}&originCoordLat=${lat}&originCoordLong=${long}&maxResults=2")
    Single<NearbyStops> nearbyStops(@Path("keyNBS") String nbsApiKey, @Path("lat") String latitude, @Path("long") String longitude);

}
