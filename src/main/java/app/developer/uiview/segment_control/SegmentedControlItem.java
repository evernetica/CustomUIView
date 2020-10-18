package app.developer.uiview.segment_control;

public class SegmentedControlItem {
    private String name;
    private int iconRes;

    public SegmentedControlItem(String name) {
        this.name = name.toUpperCase();
    }

    public SegmentedControlItem(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
