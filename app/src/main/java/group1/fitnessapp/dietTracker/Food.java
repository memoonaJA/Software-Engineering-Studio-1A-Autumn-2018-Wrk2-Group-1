package group1.fitnessapp.dietTracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 1/04/2018.
 */

public class Food {
    private String name;
    private String subText;
    private int calories;
    private final JSONObject originalJSON;

    public Food(String name, String subText, int calories, JSONObject original) {
        this.name = name;
        this.subText = subText;
        this.calories = calories;
        this.originalJSON = original;
    }

    public Food(String name, String subText, int calories, String original) throws JSONException {
        this.name = name;
        this.subText = subText;
        this.calories = calories;
        if(original != null){
            this.originalJSON = new JSONObject(original);
        }else {
            this.originalJSON = null;
        }
    }

    public void editFood(String name, String subText, int calories){
        setName(name);
        setSubText(subText);
        setCalories(calories);
    }

    @Override
    public String toString() {
        return getName() + ": " + getSubText() + " - " + getCalories();
    }

    public String getName() {
        return name;
    }

    public void setName(String s){
        this.name = s;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String s){
        this.subText = s;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int i){
        this.calories = i;
    }

    public JSONObject getOriginalJSON() {
        return originalJSON;
    }

}
