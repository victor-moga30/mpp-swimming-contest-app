package ro.mpp2026.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SelectableEventRow {
    private final long eventId;
    private final String eventName;
    private final String ageGroup;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public SelectableEventRow(long eventId, String eventName, String ageGroup) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.ageGroup = ageGroup;
    }

    public long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}