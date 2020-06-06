package ru.nsu.fit.bastrykina.osm.exception;

public class WayNotFoundException extends RuntimeException {
    public WayNotFoundException(long id) {
        super("Could not find way with id" + id);
    }
}
