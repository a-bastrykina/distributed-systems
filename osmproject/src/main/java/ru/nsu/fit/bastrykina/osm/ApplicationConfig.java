package ru.nsu.fit.bastrykina.osm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.nsu.fit.bastrykina.osm.loader.OSMLoader;
import ru.nsu.fit.bastrykina.osm.persistence.dao.NodeDao;
import ru.nsu.fit.bastrykina.osm.persistence.dao.RelationDao;
import ru.nsu.fit.bastrykina.osm.persistence.dao.WayDao;
import ru.nsu.fit.bastrykina.osm.persistence.db.Db;
import ru.nsu.fit.bastrykina.osm.service.OSMService;

@Configuration
public class ApplicationConfig {

    @Bean
    public Db db() {
        return new Db();
    }

    @Bean
    public NodeDao nodeDao() {
        return new NodeDao(db());
    }

    @Bean
    public WayDao wayDao() {
        return new WayDao(db());
    }

    @Bean
    public RelationDao relationDao() {
        return new RelationDao(db());
    }

    @Bean
    public SpeedTest speedTest() {
        return new SpeedTest(db());
    }

    @Bean
    public OSMLoader osmLoader() {
        return new OSMLoader(nodeDao(), wayDao(), relationDao());
    }

    @Bean
    public OSMService osmService() {
        return new OSMService(speedTest(), osmLoader());
    }

}
