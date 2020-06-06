package ru.nsu.fit.bastrykina.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.bastrykina.osm.SpeedTest;
import ru.nsu.fit.bastrykina.osm.service.OSMService;

@RestController
@RequestMapping
public class OSMController {

    @Qualifier("osmService")
    @Autowired
    private OSMService service;

    @GetMapping("/")
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("Welcome to Open Street Map application!");
    }

    @GetMapping("/speedtest")
    public ResponseEntity<String> performTesting() {
        return ResponseEntity.ok(service.performTesting());
    }

    @GetMapping("/load")
    public ResponseEntity<String> fillDb() {
        service.doLoad();
        return ResponseEntity.ok("Filling db with objects....");
    }
}
