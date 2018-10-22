package io.sektor.sltraveler.travel.models.results.departures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData {

    private Date latestUpdate;
    private Integer dataAge;
    private List<Departure> metros = null;
    private List<Departure> buses = null;
    private List<Departure> trains = null;
    private List<Departure> trams = null;
    private List<Departure> ships = null;
    private List<StopPointDeviation> stopPointDeviations = null;

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public Integer getDataAge() {
        return dataAge;
    }

    public void setDataAge(Integer dataAge) {
        this.dataAge = dataAge;
    }

    public List<Departure> getMetros() {
        return metros;
    }

    public void setMetros(List<Departure> metros) {
        this.metros = metros;
    }

    public List<Departure> getBuses() {
        return buses;
    }

    public void setBuses(List<Departure> buses) {
        this.buses = buses;
    }

    public List<Departure> getTrains() {
        return trains;
    }

    public void setTrains(List<Departure> trains) {
        this.trains = trains;
    }

    public List<Departure> getTrams() {
        return trams;
    }

    public void setTrams(List<Departure> trams) {
        this.trams = trams;
    }

    public List<Departure> getShips() {
        return ships;
    }

    public void setShips(List<Departure> ships) {
        this.ships = ships;
    }

    public List<StopPointDeviation> getStopPointDeviations() {
        return stopPointDeviations;
    }

    public void setStopPointDeviations(List<StopPointDeviation> stopPointDeviations) {
        this.stopPointDeviations = stopPointDeviations;
    }



}