package ru.nsu.fit.bastrykina.osm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.fit.bastrykina.osm.exception.WayNotFoundException;
import ru.nsu.fit.bastrykina.osm.persistence.dao.WayDao;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;
import ru.nsu.fit.bastrykina.osm.persistence.repository.WayRepository;

import java.util.List;
import java.util.Map;

@Service
public class WayService {
    @Autowired
    private WayDao wayDao;

    @Autowired
    private WayRepository wayRepository;

    public List<Way> getAllWays(int offset, int limit) {
        return wayRepository.findAllWithPagination(offset, limit);
    }

    public Way getWayById(long id) {
        return wayRepository.findById(id).orElseThrow(() -> new WayNotFoundException(id));
    }

    public long createWay(Way way) {
        return wayDao.insertWay(way);
    }

    public void deleteWay(long id) {
        if (wayRepository.existsById(id)) {
            wayRepository.deleteById(id);
        } else throw new WayNotFoundException(id);
    }

    public Way replaceWay(Way way, long id) {
        return wayRepository.findById(id).map(w -> {
            w.setUid(way.getUid());
            w.setTimestamp(way.getTimestamp());
            w.setVersion(way.getVersion());
            w.setTags(way.getTags());
            w.setChangeset(way.getChangeset());
            return wayRepository.save(w);
        }).orElseThrow(() -> new WayNotFoundException(id));
    }

    public Map<String, String> getWayTags(long id) {
        return wayRepository.findById(id).map(way -> way.getTags()).orElseThrow(() -> new WayNotFoundException(id));
    }

    public Map<String, String> addTags(Map<String, String> newTags, long id) {
        return wayRepository.findById(id).map(way -> {
            way.getTags().putAll(newTags);
            return wayRepository.save(way).getTags();
        }).orElseThrow(() -> new WayNotFoundException(id));
    }

}
