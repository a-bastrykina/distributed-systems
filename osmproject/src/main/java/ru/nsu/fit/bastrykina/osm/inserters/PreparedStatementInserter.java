package ru.nsu.fit.bastrykina.osm.inserters;

import ru.nsu.fit.bastrykina.osm.generated.Node;
import ru.nsu.fit.bastrykina.osm.utils.ModelUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class PreparedStatementInserter {
    private static final String STATEMENT = "insert into node (version, date_time, uid, username, changeset, lat, lon) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static void insert(Connection conn, int size) throws SQLException {
        conn.setAutoCommit(false);
        for (int i = 0; i < size; i++) {
            var stat = conn.prepareStatement(STATEMENT);
            stat.setLong(1, i);
            stat.setDate(2, new Date(System.currentTimeMillis()));
            stat.setLong(3, 0);
            stat.setString(4, "user" + i);
            stat.setLong(5, 0);
            stat.setDouble(6, 1.0);
            stat.setDouble(7, 1.0);
            stat.executeUpdate();
        }
        conn.commit();
    }
}
