package ru.nsu.fit.bastrykina.osm.persistence.dao;

import org.apache.log4j.Logger;
import ru.nsu.fit.bastrykina.osm.persistence.db.Db;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;

import java.sql.SQLException;
import java.util.List;

public class WayDao {

    private static String INSERT_STAT = "insert into way (id, version, date_time, uid, username, changeset, tags) VALUES (?, ?, ?, ?, ?, ?, ?) returning id";
    private static String DELETE_STAT = "delete from way where way.id = ?";

    private Db database;

    public WayDao(Db database) {
        this.database = database;
    }

    public long insertWay(Way way) {
        var conn = database.getConnection();
        try {
            conn.setAutoCommit(false);
            var stat = conn.prepareStatement(INSERT_STAT);
            stat.setLong(1, way.getId());
            stat.setLong(2, way.getVersion());
            stat.setDate(3, way.getTimestamp());
            stat.setLong(4, way.getUid());
            stat.setString(5, way.getUser());
            stat.setLong(6, way.getChangeset());
            stat.setObject(7, way.getTags());
            var rs = stat.executeQuery();
            conn.commit();
            rs.next();
            return rs.getLong("id");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
                Logger.getLogger(getClass()).error("Error while rollback :" + ignored.getMessage());
                ignored.printStackTrace();
            } finally {
                Logger.getLogger(getClass()).error("Error while executing query:" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteWay(long id) throws SQLException {
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(DELETE_STAT);
        stat.setLong(1, id);
        stat.executeUpdate();
        conn.commit();
    }

    public void insertWays(List<Way> ways) throws SQLException {
        String statement = "insert into way (id, version, date_time, uid, username, changeset, tags) VALUES (?, ?, ?, ?, ?, ?, ?)";
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(statement);
        for (var way : ways) {
            stat.setLong(1, way.getId());
            stat.setLong(2, way.getVersion());
            stat.setDate(3, way.getTimestamp());
            stat.setLong(4, way.getUid());
            stat.setString(5, way.getUser());
            stat.setLong(6, way.getChangeset());
            stat.setObject(7, way.getTags());
            stat.addBatch();
        }
        stat.executeBatch();
        conn.commit();
    }
}
