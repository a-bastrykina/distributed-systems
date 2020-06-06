package ru.nsu.fit.bastrykina.osm;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.bastrykina.osm.persistence.db.Db;
import ru.nsu.fit.bastrykina.osm.inserters.BatchInserter;
import ru.nsu.fit.bastrykina.osm.inserters.PreparedStatementInserter;
import ru.nsu.fit.bastrykina.osm.inserters.StringStatementInserter;

public class SpeedTest {

    private final Db database;

    private static int COUNT = 100;

    public SpeedTest(Db database) {
        this.database = database;
    }

    public String test() {
        StopWatch preparedStopWatch = new StopWatch();
        StopWatch strStopWatch = new StopWatch();
        StopWatch batchStopWatch = new StopWatch();
        try {
            strStopWatch.start();
            StringStatementInserter.insert(database.getConnection(), COUNT);
            strStopWatch.stop();

            preparedStopWatch.start();
            PreparedStatementInserter.insert(database.getConnection(), COUNT);
            preparedStopWatch.stop();

            batchStopWatch.start();
            BatchInserter.insert(database.getConnection(), COUNT);
            batchStopWatch.stop();;

            return buildResult(strStopWatch, preparedStopWatch, batchStopWatch);
        } catch (Exception e) {
            Logger.getLogger(SpeedTest.class).error("Error during test");
            e.printStackTrace();
            return null;
        }
    }
        private static String buildResult(StopWatch strStopWatch, StopWatch preparedStopWatch, StopWatch batchStopWatch) {
            String delim = "-----------------------------------------------------\r\n";
            StringBuilder result = new StringBuilder();

            result.append(delim);
            result.append("Insert speed test for " + COUNT + "insert operations\r\n");
            result.append(delim);
            result.append("String statement elapsed: " + strStopWatch.getTime() + "ms\r\n");
            result.append(delim);
            result.append("Prepared statement elapsed: " + preparedStopWatch.getTime() + "ms\r\n");
            result.append(delim);
            result.append("Batch statement elapsed: " + batchStopWatch.getTime() + "ms\r\n");
            result.append(delim);
            return result.toString();
        }
}
