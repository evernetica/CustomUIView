package app.developer.uiview.steps;

public class StepItem {
    private boolean isSelected;
    private boolean isCompleted;
    private int stepNumber;
    private String stepTitle;

    public StepItem(boolean isSelected, boolean isCompleted, int stepNumber, String stepTitle) {
        this.isSelected = isSelected;
        this.isCompleted = isCompleted;
        this.stepNumber = stepNumber;
        this.stepTitle = stepTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public void setStepTitle(String stepTitle) {
        this.stepTitle = stepTitle;
    }
}
