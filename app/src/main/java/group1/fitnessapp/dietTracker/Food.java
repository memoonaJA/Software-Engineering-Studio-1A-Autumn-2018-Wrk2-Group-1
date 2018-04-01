package group1.fitnessapp.dietTracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 1/04/2018.
 */

public class Food {
    private final String name;
    private final String subText;
    private final int calories;
    private final JSONObject originalJSON;

    public Food(String name, String subText, int calories, JSONObject original) {
        this.name = name;
        this.subText = subText;
        this.calories = calories;
        this.originalJSON = original;
    }

    public String getName() {
        return name;
    }

    public String getSubText() {
        return subText;
    }

    public int getCalories() {
        return calories;
    }

    public JSONObject getOriginalJSON() {
        return originalJSON;
    }
}
