package ru.nsu.fit.bastrykina.osm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.fit.bastrykina.osm.exception.RelationNotFoundException;
import ru.nsu.fit.bastrykina.osm.persistence.dao.RelationDao;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Member;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;
import ru.nsu.fit.bastrykina.osm.persistence.repository.RelationRepository;

import java.util.List;
import java.util.Map;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RelationDao relationDao;

    public List<Relation> getAllRelations(int offset, int limit) {
        return relationRepository.findAllWithPagination(offset, limit);
    }

    public Relation getRelationById(long id) {
        return relationRepository.findById(id).orElseThrow(() -> new RelationNotFoundException(id));
    }

    public long createRelation(Relation rel) {
        return relationDao.insertRelation(rel);
    }

    public void deleteRelation(long id) {
        if (relationRepository.existsById(id)) {
            relationRepository.deleteById(id);
        } else throw new RelationNotFoundException(id);
    }

    public Relation replaceRelation(Relation relation, long id) {
        return relationRepository.findById(id).map(r -> {
            r.setUid(relation.getUid());
            r.setTimestamp(relation.getTimestamp());
            r.setVersion(relation.getVersion());
            r.setTags(relation.getTags());
            r.setChangeset(relation.getChangeset());
            r.setMembers(relation.getMembers());
            return relationRepository.save(r);
        }).orElseThrow(() -> new RelationNotFoundException(id));
    }

    public Map<String, String> getRelationTags(long id) {
        return relationRepository.findById(id).map(r -> r.getTags()).orElseThrow(() -> new RelationNotFoundException(id));
    }

    public Map<String, String> addTags(Map<String, String> newTags, long id) {
        return relationRepository.findById(id).map(r -> {
            r.getTags().putAll(newTags);
            return relationRepository.save(r).getTags();
        }).orElseThrow(() -> new RelationNotFoundException(id));
    }

    public List<Member> getRelationMembers(long id) {
        return relationRepository.findById(id).map(r -> r.getMembers()).orElseThrow(() -> new RelationNotFoundException(id));
    }

    public List<Member> addRelationMember(Member member, long id) {
        return relationRepository.findById(id).map(r -> {
            r.getMembers().add(member);
            member.setRelation(r);
            return relationRepository.save(r).getMembers();
        }).orElseThrow(() -> new RelationNotFoundException(id));
    }

}
