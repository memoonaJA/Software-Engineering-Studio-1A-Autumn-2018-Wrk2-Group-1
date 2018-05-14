package group1.fitnessapp.dietTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aaron on 1/04/2018.
 */

public class Food implements Serializable{
    //private int keyID;
    private String name;
    private String subText;
    private double servings;
    private double servingQuantity;
    private String servingUnit;
    private double calories;

    // Constructors
    public Food(String name, String subText, double servings, double servingQuantity, String servingUnit, double calories) {
        //this.keyID = -1;
        this.name = name;
        this.subText = subText;
        this.servings = servings;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
    }

    public Food(Food food){
        //this.keyID = -1;
        this.name = food.getName();
        this.subText = food.getSubText();
        this.servings = food.servings;
        this.servingQuantity = food.getServingQuantity();
        this.servingUnit = food.servingUnit;
        this.calories = food.calories;
    }


    // Getters
//    public int getKeyID(){
//        return keyID;
//    }

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

    public double getServings(){
        return servings;
    }

    public double getServingQuantity() {
        return servingQuantity;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    // Setters
//    public void setKeyID(int id){
//        this.keyID = id;
//    }

    public Food editServings(double servings){
        this.servings = servings;
        return this;
    }

    @Override
    public String toString(){
        return name +" " +subText +" " +servings +" " +getTotalCalories();
    }
}
