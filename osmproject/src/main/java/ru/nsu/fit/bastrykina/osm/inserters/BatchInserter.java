package ru.nsu.fit.bastrykina.osm.inserters;

import ru.nsu.fit.bastrykina.osm.generated.Node;
import ru.nsu.fit.bastrykina.osm.utils.ModelUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class BatchInserter {
    private static final String STATEMENT = "insert into node (version, date_time, uid, username, changeset, lat, lon) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static void insert(Connection conn, int batchSize) throws SQLException {
        conn.setAutoCommit(false);

        var stat = conn.prepareStatement(STATEMENT);
        for (int i = 0; i < batchSize; i++) {
            stat.setLong(1, i);
            stat.setDate(2, new Date(System.currentTimeMillis()));
            stat.setLong(3, 0);
            stat.setString(4, "user" + i);
            stat.setLong(5, 0);
            stat.setDouble(6, 1.0);
            stat.setDouble(7, 1.0);
            stat.addBatch();
        }
        stat.executeBatch();
        conn.commit();

//        stat.setLong(1, node.getVersion().longValue());
//        stat.setDate(2, ModelUtils.toSqlDate(node.getTimestamp()));
//        stat.setLong(3, node.getUid().longValue());
//        stat.setString(4, node.getUser());
//        stat.setLong(5, node.getChangeset().longValue());
//        stat.setDouble(6, node.getLat());
//        stat.setDouble(7, node.getLon());
    }
}