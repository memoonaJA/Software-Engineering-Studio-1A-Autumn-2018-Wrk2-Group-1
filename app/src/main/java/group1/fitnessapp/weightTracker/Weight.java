package group1.fitnessapp.weightTracker;

public class Weight {
    private int key_id;
    private long logDate;
    private double weight;
    private String units;

    public  Weight(long logDate, double weight, String units){
        this.key_id = -1;
        this.logDate = logDate;
        this.weight = weight;
        this.units = units;
    }
    public  Weight(int key_id, long logDate, double weight, String units){
        this.key_id = key_id;
        this.logDate = logDate;
        this.weight = weight;
        this.units = units;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public long getLogDate() {
        return logDate;
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
