package io.sektor.sltraveler.travel.models.results.departures;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Departure {

    public enum TransportMode {
        BUS, TRAIN, TRAM, METRO, SHIP
    }

    private String groupOfLine;
    private TransportMode transportMode;
    private String lineNumber;
    private String destination;
    private String stopAreaName;
    private String stopAreaNumber;
    private String stopPointNumber;
    private String stopPointDesignation;

    @JsonFormat(timezone = "Europe/Stockholm")
    private Date timeTabledDateTime;

    @JsonFormat(timezone = "Europe/Stockholm")
    private Date expectedDateTime;
    private String displayTime;
    private List<Deviation> deviations;

    public Departure() {}

    public Departure(TransportMode transportMode, String lineNumber, String destination, String displayTime) {
        this.transportMode = transportMode;
        this.lineNumber = lineNumber;
        this.destination = destination;
        this.displayTime = displayTime;
    }

    public String getGroupOfLine() {
        return groupOfLine;
    }

    public void setGroupOfLine(String groupOfLine) {
        this.groupOfLine = groupOfLine;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStopAreaName() {
        return stopAreaName;
    }

    public void setStopAreaName(String stopAreaName) {
        this.stopAreaName = stopAreaName;
    }

    public String getStopAreaNumber() {
        return stopAreaNumber;
    }

    public void setStopAreaNumber(String stopAreaNumber) {
        this.stopAreaNumber = stopAreaNumber;
    }

    public String getStopPointNumber() {
        return stopPointNumber;
    }

    public void setStopPointNumber(String stopPointNumber) {
        this.stopPointNumber = stopPointNumber;
    }

    public String getStopPointDesignation() {
        return stopPointDesignation;
    }

    public void setStopPointDesignation(String stopPointDesignation) {
        this.stopPointDesignation = stopPointDesignation;
    }

    public Date getTimeTabledDateTime() {
        return timeTabledDateTime;
    }

    public void setTimeTabledDateTime(Date timeTabledDateTime) {
        this.timeTabledDateTime = timeTabledDateTime;
    }

    public Date getExpectedDateTime() {
        return expectedDateTime;
    }

    public void setExpectedDateTime(Date expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public List<Deviation> getDeviations() {
        return deviations;
    }

    public void setDeviations(List<Deviation> deviations) {
        this.deviations = deviations;
    }
}