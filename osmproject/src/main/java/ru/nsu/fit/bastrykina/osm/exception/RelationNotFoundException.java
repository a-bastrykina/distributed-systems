package ru.nsu.fit.bastrykina.osm.exception;

public class RelationNotFoundException extends RuntimeException {
    public RelationNotFoundException(long id) {
        super("Could not find relation with id" + id);
    }
}
