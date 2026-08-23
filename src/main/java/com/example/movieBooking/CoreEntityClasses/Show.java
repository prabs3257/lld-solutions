package com.example.movieBooking.CoreEntityClasses;

import java.util.UUID;

public class Show {
    private UUID showId;
    private Movie movie;
    private Screen screen;

    public Show(final Movie movie, final Screen screen, final UUID showId) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(final Screen screen) {
        this.screen = screen;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(final Movie movie) {
        this.movie = movie;
    }

    public UUID getShowId() {
        return showId;
    }

    public void setShowId(final UUID showId) {
        this.showId = showId;
    }
}
