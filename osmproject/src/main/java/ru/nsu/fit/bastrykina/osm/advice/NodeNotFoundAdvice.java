package ru.nsu.fit.bastrykina.osm.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nsu.fit.bastrykina.osm.exception.NodeNotFoundException;

@ControllerAdvice
public class NodeNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(NodeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String nodeNotFoundHandler(NodeNotFoundException e) {
        return e.getMessage();
    }
}
