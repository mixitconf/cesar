package org.mixit.cesar.site.model.planning;

public enum SlotDuration {

    KEYNOTE(20),
    TALK(50),
    WORKSHOP(110);

    /**
     * Duration in minutes
     */
    private int duration;

    private SlotDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
