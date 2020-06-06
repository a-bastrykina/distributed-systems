package ru.nsu.fit.bastrykina.osm.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.fit.bastrykina.osm.exception.NodeNotFoundException;
import ru.nsu.fit.bastrykina.osm.persistence.dao.NodeDao;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.persistence.repository.NodeRepository;

import java.util.List;
import java.util.Map;

@Service
public class NodeService {
    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private NodeDao nodeDao;

    public List<Node> getAllNodes(int offset, int limit) {
        return nodeRepository.findAllWithPagination(offset, limit);
    }

    public Node getNodeById(long id) {
        return nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    @SneakyThrows
    public long createNode(Node node) {
        return nodeDao.insertNode(node);
    }

    public List<Node> getAllNodesInRadius(double lon, double lat, double radius) {
        return nodeRepository.findByRadius(lon, lat, radius);
    }

    public void deleteNode(long id) {
        if (nodeRepository.existsById(id)) {
            nodeRepository.deleteById(id);
        } else throw new NodeNotFoundException(id);
    }

    public Node replaceNode(Node node, long id) {
        return nodeRepository.findById(id).map(n -> {
            n.setUid(node.getUid());
            n.setTimestamp(node.getTimestamp());
            n.setVersion(node.getVersion());
            n.setTags(node.getTags());
            n.setLat(node.getLat());
            n.setLon(node.getLon());
            n.setChangeset(node.getChangeset());
            return nodeRepository.save(n);
        }).orElseThrow(() -> new NodeNotFoundException(id));
    }

    public Map<String, String> getNodeTags(long id) {
        return nodeRepository.findById(id).map(n -> n.getTags()).orElseThrow(() -> new NodeNotFoundException(id));
    }

    public Map<String, String> addTags(Map<String, String> newTags, long id) {
        return nodeRepository.findById(id).map(n -> {
            n.getTags().putAll(newTags);
            return nodeRepository.save(n).getTags();
        }).orElseThrow(() -> new NodeNotFoundException(id));
    }

}
