package edu.uoregon.richie.tidev3;

import java.util.Calendar;

public class TideData {
    public enum TideType { LOW, HIGH };
    // stores data relevant to each tide entry
    private Calendar date;
    private double valueFt, valueCm;
    private TideType tideType;

    public TideData() {
        date = Calendar.getInstance();
        valueFt = 0;
        valueCm = 0;
        tideType = TideType.LOW;
    }

    // getters and setters
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getValueFt() {
        return valueFt;
    }

    public void setValueFt(double valueFt) {
        this.valueFt = valueFt;
    }

    public double getValueCm() {
        return valueCm;
    }

    public void setValueCm(double valueCm) {
        this.valueCm = valueCm;
    }

    public TideType getTideType() {
        return tideType;
    }

    public void setTideType(TideType tideType) {
        this.tideType = tideType;
    }
}