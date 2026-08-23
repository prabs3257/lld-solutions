package com.example.movieBooking.CoreEntityClasses;

import java.util.List;

public class Theatre {

    List<Screen> screens;

    public Theatre(final List<Screen> screens) {
        this.screens = screens;
    }
    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(final List<Screen> screens) {
        this.screens = screens;
    }

}
