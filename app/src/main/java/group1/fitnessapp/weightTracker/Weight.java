package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weight {
    private Date logDate;
    private double weight;
    private String units;

    public  Weight(Date logDate, double weight, String units){
        this.logDate = logDate;
        this.weight = weight;
        this.units = units;
    }

    public Date getLogDate() {
        return logDate;
    }

    public String getLogDateFormatted() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");
        return df.format(logDate);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
