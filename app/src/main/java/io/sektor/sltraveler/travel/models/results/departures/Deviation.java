package io.sektor.sltraveler.travel.models.results.departures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Deviation {

    private String text;
    private String consequence;
    private int importanceLevel;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(int importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

}
