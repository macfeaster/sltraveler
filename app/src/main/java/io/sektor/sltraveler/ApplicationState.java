package io.sektor.sltraveler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.room.Room;
import io.reactivex.subjects.PublishSubject;
import io.sektor.sltraveler.travel.models.AppDatabase;
import io.sektor.sltraveler.travel.models.repositories.NearbyStopsRepository;
import io.sektor.sltraveler.travel.models.services.DeparturesService;
import io.sektor.sltraveler.travel.models.services.NearbyStopsService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApplicationState {

    private static ApplicationState instance;

    private static DeparturesService departuresService;
    private static NearbyStopsService nearbyStopsService;
    private static Retrofit retrofit;

    private FusedLocationProviderClient locationClient;
    private LocationProvider provider;
    private PublishSubject<Location> locationSubject = PublishSubject.create();
    private final AppDatabase db;
    private final NearbyStopsRepository stopsRepository;

    private ApplicationState(Context context) {
        locationClient = LocationServices.getFusedLocationProviderClient(context);
        provider = new LocationProvider(locationSubject);
        db = Room.databaseBuilder(context, AppDatabase.class, "sl-traveler").build();
        stopsRepository = new NearbyStopsRepository(this);
    }

    public static void createInstance(Context context) {
        instance = new ApplicationState(context);
    }

    public static ApplicationState getInstance() {
        return instance;
    }

    public PublishSubject<Location> getLocationSubject() {
        return locationSubject;
    }

    public AppDatabase getDb() {
        return db;
    }

    public NearbyStopsRepository getStopsRepository() {
        return stopsRepository;
    }

    @SuppressLint("MissingPermission")
    public void updateLocation() {
        System.err.println("Location update requested");
        locationClient.getLastLocation().addOnSuccessListener(provider);
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.sl.se")
                    .client(client)
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public DeparturesService getDeparturesService() {
        if (departuresService == null)
            departuresService = getRetrofit().create(DeparturesService.class);

        return departuresService;
    }

    public NearbyStopsService getNearbyStopsService() {
        if (nearbyStopsService == null)
            nearbyStopsService = getRetrofit().create(NearbyStopsService.class);

        return nearbyStopsService;
    }

    public String getRTKey() {
        return "";
    }

    public String getNBSKey() {
        return "";
    }


}
