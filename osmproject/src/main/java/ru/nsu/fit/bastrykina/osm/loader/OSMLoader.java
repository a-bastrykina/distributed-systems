package ru.nsu.fit.bastrykina.osm.loader;

import org.apache.log4j.Logger;
import ru.nsu.fit.bastrykina.osm.persistence.dao.NodeDao;
import ru.nsu.fit.bastrykina.osm.persistence.dao.RelationDao;
import ru.nsu.fit.bastrykina.osm.persistence.dao.WayDao;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;

import static ru.nsu.fit.bastrykina.osm.persistence.model.converter.XMLToEntitiesConverter.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OSMLoader {
    private static int BATCH_SIZE = 1000;

    NodeDao nodeDao;
    WayDao wayDao;
    RelationDao relationDao;

    public OSMLoader(NodeDao nodeDao, WayDao wayDao, RelationDao relationDao) {
        this.nodeDao = nodeDao;
        this.wayDao = wayDao;
        this.relationDao = relationDao;
    }

    public void load() {
        Logger.getLogger(getClass()).info("Started filling database with objects");
        List<Node> nodesBatch = new ArrayList<>();
        List<Way> waysBatch = new ArrayList<>();
        List<Relation> relationsBatch = new ArrayList<>();
        try (var processor = new OSMProcessor()) {
            while (!processor.isEof()) {
                switch (processor.getCurrentType()) {
                   case NODE:
                       var node = processor.getNextNode();
                       if (nodesBatch.size() <= BATCH_SIZE) {
                           nodesBatch.add(convert(node));
                       } else {
                           nodeDao.insertNodes(nodesBatch);
                           nodesBatch.clear();
                       }
                       break;
                   case WAY:
                       var way = processor.getNextWay();
                       if (waysBatch.size() <= BATCH_SIZE) {
                           waysBatch.add(convert(way));
                       } else {
                           wayDao.insertWays(waysBatch);
                           waysBatch.clear();
                       }
                       break;
                    case RELATION:
                        var rel = processor.getNextRelation();
                        if (relationsBatch.size() <= BATCH_SIZE) {
                            relationsBatch.add(convert(rel));
                        } else {
                            relationDao.insertRelations(relationsBatch);
                            relationsBatch.clear();
                        }
                        break;
                }
            }
            nodeDao.insertNodes(nodesBatch);
            wayDao.insertWays(waysBatch);
            relationDao.insertRelations(relationsBatch);
        } catch (IOException | SQLException | XMLStreamException e) {
            Logger.getLogger(OSMLoader.class).error("Error loading osm to database");
            e.printStackTrace();
        }
        Logger.getLogger(getClass()).info("Finished filling database with objects");
    }

}
