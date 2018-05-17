package group1.fitnessapp.dietTracker;

import java.io.Serializable;

/**
 * Created by aaron on 1/04/2018.
 */

public class Food implements Serializable{
    private int keyID;
    private String name;
    private String subText;
    private double servings;
    private double servingQuantity;
    private String servingUnit;
    private double calories;

    // Constructors
    public Food(String name, String subText, double servings, double servingQuantity, String servingUnit, double calories) {
        this.keyID = 0;
        this.name = name;
        this.subText = subText;
        this.servings = servings;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
    }

    // Necessary when constructing foods with reference to position in DB 
    public Food(int key_id, String name, String subText, double servings, double servingQuantity, String servingUnit, double calories) {
        this.keyID = key_id;
        this.name = name;
        this.subText = subText;
        this.servings = servings;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.calories = calories;
    }

    public Food(Food food){
        this.keyID = food.keyID;
        this.name = food.getName();
        this.subText = food.getSubText();
        this.servings = food.servings;
        this.servingQuantity = food.getServingQuantity();
        this.servingUnit = food.servingUnit;
        this.calories = food.calories;
    }

    public int getKeyId() {
        return keyID;
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

    public double getServings(){
        return servings;
    }

    public double getServingQuantity() {
        return servingQuantity;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public Food editServings(double servings){
        this.servings = servings;
        return this;
    }

    @Override
    public String toString(){
        return name +" " +subText +" " +servings +" " +getTotalCalories();
    }
}
