package ru.nsu.fit.bastrykina.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.service.NodeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @GetMapping("")
    public ResponseEntity<List<Node>> getNodes() {
        return ResponseEntity.ok(nodeService.getAllNodes(0, 50));
    }

    @GetMapping(params = {"offset", "limit"})
    public ResponseEntity<List<Node>> getNodes(@RequestParam(value = "offset") int offset, @RequestParam(value = "limit") int limit) {
        return ResponseEntity.ok(nodeService.getAllNodes(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Node> getNodeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(nodeService.getNodeById(id));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> getNodeTags(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(nodeService.getNodeTags(id));
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> addNodeTags(@RequestBody Map<String, String> newTags, @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(nodeService.addTags(newTags, id));
    }

    @GetMapping(params = {"lat", "lon", "radius"})
    public ResponseEntity<List<Node>> getNodesInRadius(@RequestParam(name = "lon") double lon, @RequestParam(name = "lat") double lat, @RequestParam(name = "radius") double radius) {
        return ResponseEntity.ok(nodeService.getAllNodesInRadius(lon, lat, radius));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNode(@PathVariable long id) {
        nodeService.deleteNode(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<String> createNode(@RequestBody Node node) {
        long id = nodeService.createNode(node);
        return ResponseEntity.ok("Created node with id " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Node> updateNode(@RequestBody Node node, @PathVariable long id) {
        return ResponseEntity.ok(nodeService.replaceNode(node, id));
    }

}
