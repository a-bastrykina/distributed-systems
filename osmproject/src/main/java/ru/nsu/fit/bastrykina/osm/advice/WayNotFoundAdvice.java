package ru.nsu.fit.bastrykina.osm.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nsu.fit.bastrykina.osm.exception.WayNotFoundException;

@ControllerAdvice
public class WayNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(WayNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String wayNotFoundHandler(WayNotFoundException e) {
        return e.getMessage();
    }
}
