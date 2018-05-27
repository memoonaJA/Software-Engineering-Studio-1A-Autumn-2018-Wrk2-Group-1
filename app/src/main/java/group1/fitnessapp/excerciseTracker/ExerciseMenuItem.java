package group1.fitnessapp.excerciseTracker;

public class ExerciseMenuItem {
    private final int drawableImage;
    private final String itemText;
    private final int reqCode;

    public ExerciseMenuItem(int drawableImage, String itemText, int reqCode) {
        this.drawableImage = drawableImage;
        this.itemText = itemText;
        this.reqCode = reqCode;
    }

    public int getDrawableImage() {
        return this.drawableImage;
    }

    public String getItemText() {
        return this.itemText;
    }

    public int getReqCode() {
        return this.reqCode;
    }

}
