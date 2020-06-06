import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        var logger = Logger.getLogger(Main.class);
        logger.info("Hello world!");

        var logger2 = LoggerFactory.getLogger(Main.class);
        logger2.info("Hello world!");

        var processor = new OSMProcessor(Path.of("/Users/user/IdeaProjects/task1/src/main/resources/RU-NVS.osm.bz2"));
        processor.printStatistics();
    }
}
