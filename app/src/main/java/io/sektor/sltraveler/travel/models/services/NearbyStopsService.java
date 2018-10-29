package io.sektor.sltraveler.travel.models.services;

import io.reactivex.Single;
import io.sektor.sltraveler.travel.models.results.NearbyStops;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbyStopsService {

    @GET("/api2/nearbystops.json")
    Single<NearbyStops> nearbyStops(@Query("key") String nbsApiKey, @Query("originCoordLat") String latitude, @Query("originCoordLong") String longitude);

}
