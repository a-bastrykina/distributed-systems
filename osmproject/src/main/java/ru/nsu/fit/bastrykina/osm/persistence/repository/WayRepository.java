package ru.nsu.fit.bastrykina.osm.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;

import java.util.List;

public interface WayRepository extends JpaRepository<Way, Long> {
    @Query(value = "select * from way offset ?1 limit ?2", nativeQuery = true)
    List<Way> findAllWithPagination(int offset, int limit);
}
