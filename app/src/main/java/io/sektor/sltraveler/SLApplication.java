package io.sektor.sltraveler;

import android.app.Application;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.sektor.sltraveler.travel.models.services.DeparturesService;
import io.sektor.sltraveler.travel.models.services.NearbyStopsService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SLApplication extends Application {

    private static DeparturesService departuresService;
    private static NearbyStopsService nearbyStopsService;
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
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
