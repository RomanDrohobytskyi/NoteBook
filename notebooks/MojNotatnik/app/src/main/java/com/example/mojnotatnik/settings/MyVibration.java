package com.example.mojnotatnik.settings;

public class MyVibration {

    private boolean isActive = true;
    private long time = 100;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
