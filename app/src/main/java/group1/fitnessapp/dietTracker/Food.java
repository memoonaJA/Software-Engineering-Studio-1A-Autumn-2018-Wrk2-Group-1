package group1.fitnessapp.dietTracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 1/04/2018.
 */

public class Food {
    private String name;
    private String subText;
    private double servings;
    private double servingQuantity;
    private String servingUnit;
    private double calories;
    private final JSONObject originalJSON;

    public Food(String name, String subText, double servings, double servingQuantity, String servingUnit, double calories, JSONObject original) {
        this.name = name;
        this.subText = subText;
        this.servings = servings;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
        this.originalJSON = original;
    }

    public Food(String name, String subText, double servings, double servingQuantity, String servingUnit, double calories, String original) throws JSONException {
        this.name = name;
        this.subText = subText;
        this.servings = servings;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
        if(original != null){
            this.originalJSON = new JSONObject(original);
        }else {
            this.originalJSON = null;
        }
    }

    public String getName() {
        return name;
    }

    public String getSubText() {
        return subText;
    }

    public double getCalories() {
        return calories;
    }

    public double  getTotalCalories(){
        return (calories * servings);
    }

    public JSONObject getOriginalJSON() {
        return originalJSON;
    }

    public double getServings(){
        return servings;
    }

    public double getServingQuantity() {
        return servingQuantity;
    }

    public String getServingUnit() {
        return servingUnit;
    }

}
