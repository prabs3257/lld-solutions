package com.example.movieBooking.Services;

import com.example.movieBooking.CoreEntityClasses.Screen;
import com.example.movieBooking.CoreEntityClasses.Theatre;

import java.util.List;

public class TheatreService {

    public Theatre createTheatre(List<Screen> screens) {
        return new Theatre(screens);
    }
}
