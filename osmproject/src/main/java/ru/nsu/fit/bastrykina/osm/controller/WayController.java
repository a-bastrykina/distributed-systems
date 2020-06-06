package ru.nsu.fit.bastrykina.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;
import ru.nsu.fit.bastrykina.osm.service.WayService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ways")
public class WayController {

    @Autowired
    private WayService wayService;

    @GetMapping("")
    public ResponseEntity<List<Way>> getWays() {
        return ResponseEntity.ok(wayService.getAllWays(0, 50));
    }

    @GetMapping(params = {"offset", "limit"})
    public ResponseEntity<List<Way>> getWays(@RequestParam(value = "offset") int offset, @RequestParam(value = "limit") int limit) {
        return ResponseEntity.ok(wayService.getAllWays(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Way> getWayById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(wayService.getWayById(id));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> getWayTags(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(wayService.getWayTags(id));
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> addWayTags(@RequestBody Map<String, String> newTags, @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(wayService.addTags(newTags, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteWay(@PathVariable long id) {
        wayService.deleteWay(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<String> createWay(@RequestBody Way way) {
        long id = wayService.createWay(way);
        return ResponseEntity.ok("Created way with id " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Way> updateWay(@RequestBody Way way, @PathVariable long id) {
        return ResponseEntity.ok(wayService.replaceWay(way, id));
    }

}
