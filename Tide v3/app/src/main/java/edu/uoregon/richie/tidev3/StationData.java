package edu.uoregon.richie.tidev3;

import java.util.ArrayList;

public class StationData {
    // stores data pertinent to all tide data entries
    private String stationName,
            stationState,
            stationId;
    // list of tide data entries
    private ArrayList<TideData> tideData;

    public StationData() {
        stationName = "";
        stationState = "";
        stationId = "";
        tideData = new ArrayList<>();
    }

    // getters and setters
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationState() {
        return stationState;
    }

    public void setStationState(String stationState) {
        this.stationState = stationState;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public ArrayList<TideData> getTideData() {
        return tideData;
    }

    public void setTideData(ArrayList<TideData> tideData) {
        this.tideData = tideData;
    }
}