package org.mixit.cesar.site.model.session;


public enum Format {
    Talk(50),
    LightningTalk(5),
    Workshop(110),
    Random(25),
    Keynote(25);

    /**
     * Duration in minutes
     */
    private int duration;

    private Format(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
