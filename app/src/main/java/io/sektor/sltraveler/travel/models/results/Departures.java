package io.sektor.sltraveler.travel.models.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.sektor.sltraveler.travel.models.results.departures.ResponseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Departures {

    private Integer statusCode;
    private Object message;
    private Integer executionTime;
    private ResponseData responseData;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

}