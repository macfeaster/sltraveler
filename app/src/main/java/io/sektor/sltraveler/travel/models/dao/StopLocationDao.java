package io.sektor.sltraveler.travel.models.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;
import io.sektor.sltraveler.travel.models.results.nearbystops.StopLocation;

@Dao
public interface StopLocationDao {

    @Query("SELECT * FROM stop")
    Maybe<List<StopLocation>> getAll();

    @Query("SELECT * FROM stop ORDER BY dist LIMIT 1")
    Maybe<StopLocation> findClosest();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insertAll(List<StopLocation> stops);

    @Query("DELETE FROM stop WHERE time < :maxAge")
    void deleteAll(long maxAge);

}
