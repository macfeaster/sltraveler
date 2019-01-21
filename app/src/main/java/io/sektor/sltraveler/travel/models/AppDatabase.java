package io.sektor.sltraveler.travel.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import io.sektor.sltraveler.travel.models.dao.StopLocationDao;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

@Database(entities = {StopLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StopLocationDao stopLocationDao();
}