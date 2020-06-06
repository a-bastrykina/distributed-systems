package ru.nsu.fit.bastrykina.osm.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    @Query(value = "select * from relation offset ?1 limit ?2", nativeQuery = true)
    List<Relation> findAllWithPagination(int offset, int limit);
}
