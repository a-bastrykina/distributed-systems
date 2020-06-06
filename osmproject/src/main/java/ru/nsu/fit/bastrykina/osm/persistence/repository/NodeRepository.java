package ru.nsu.fit.bastrykina.osm.persistence.repository;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;

import java.util.List;
import java.util.Map;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    @Query(value = "select * from node offset ?1 limit ?2", nativeQuery = true)
    List<Node> findAllWithPagination(int offset, int limit);

    @Query(value = "select * from node where (point(node.lon,node.lat) <@> point(?1,?2)) < ?3 order by (point(node.lon,node.lat) <@> point(?1,?2))", nativeQuery = true)
    List<Node> findByRadius(double lon, double lat, double radius);
}
