
package io.sektor.sltraveler.travel.models.results.nearbystops;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stop")
public class StopLocation implements Parcelable {

    @ColumnInfo
    private String idx;

    @ColumnInfo
    private String name;

    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo
    private String lat;

    @ColumnInfo
    private String lon;

    @ColumnInfo
    private String dist;

    @ColumnInfo
    private long time;

    public StopLocation() {
        this.time = System.currentTimeMillis() / 1000L;
    }

    protected StopLocation(Parcel in) {
        idx = in.readString();
        name = in.readString();
        id = in.readString();
        lat = in.readString();
        lon = in.readString();
        dist = in.readString();
        time = in.readLong();
    }

    public static final Creator<StopLocation> CREATOR = new Creator<StopLocation>() {
        @Override
        public StopLocation createFromParcel(Parcel in) {
            return new StopLocation(in);
        }

        @Override
        public StopLocation[] newArray(int size) {
            return new StopLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idx);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(dist);
        dest.writeLong(time);
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public int getSiteId() {
        return Integer.parseInt(id.replace("30010", ""));
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
