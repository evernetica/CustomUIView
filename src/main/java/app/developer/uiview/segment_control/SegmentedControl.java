package app.developer.uiview.segment_control;


public interface SegmentedControl {
    int getCount();
    SegmentedControlItem getItem(int position);
    String getName(int position);
    Integer getIcon(int position);
}
