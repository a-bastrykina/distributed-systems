package ru.nsu.fit.bastrykina.osm.inserters;

//import org.intellij.lang.annotations.Language;

import ru.nsu.fit.bastrykina.osm.generated.Node;

import java.sql.Connection;
import java.sql.SQLException;

public class StringStatementInserter {
    //    @Language("sql")
    private static final String STATEMENT = "insert into node (version, date_time, uid, username, changeset, lat, lon) VALUES (%s, now(), %s, '%s', %s, %s, %s)";

    public static void insert(Connection conn, int size) throws SQLException {
        conn.setAutoCommit(false);
        for (int i = 0; i < size; i++) {
            var statString = String.format(STATEMENT, i, 0, "user" + i, 0, 1.0, 1.0);
            conn.createStatement().executeUpdate(statString);
        }
        conn.commit();
    }
}
