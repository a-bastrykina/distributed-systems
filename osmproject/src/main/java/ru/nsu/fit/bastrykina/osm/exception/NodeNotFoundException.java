package ru.nsu.fit.bastrykina.osm.exception;

public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(long id) {
        super("Could not find node with id " + id);
    }
}
