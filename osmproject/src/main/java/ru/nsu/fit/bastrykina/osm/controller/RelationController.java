package ru.nsu.fit.bastrykina.osm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Member;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;
import ru.nsu.fit.bastrykina.osm.service.RelationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relations")
public class RelationController {
    @Autowired
    private RelationService relationService;

    @GetMapping("")
    public ResponseEntity<List<Relation>> getRelations() {
        return ResponseEntity.ok(relationService.getAllRelations(0, 10));
    }

    @GetMapping(params = {"offset", "limit"})
    public ResponseEntity<List<Relation>> getRelations(@RequestParam(value = "offset") int offset, @RequestParam(value = "limit") int limit) {
        return ResponseEntity.ok(relationService.getAllRelations(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relation> getRelById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(relationService.getRelationById(id));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> getRelationTags(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(relationService.getRelationTags(id));
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Map<String, String>> addRelationTags(@RequestBody Map<String, String> newTags, @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(relationService.addTags(newTags, id));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<Member>> getRelationMembers(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(relationService.getRelationMembers(id));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<List<Member>> addRelationMember(@RequestBody Member newMember, @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(relationService.addRelationMember(newMember, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRelation(@PathVariable long id) {
        relationService.deleteRelation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<String> createRelation(@RequestBody Relation relation) {
        long id = relationService.createRelation(relation);
        return ResponseEntity.ok("Created relation with id " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relation> updateRelation(@RequestBody Relation relation, @PathVariable long id) {
        return ResponseEntity.ok(relationService.replaceRelation(relation, id));
    }

}
