package ru.nsu.fit.bastrykina.osm.persistence.dao;

import org.apache.log4j.Logger;
import ru.nsu.fit.bastrykina.osm.persistence.db.Db;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.utils.ModelUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NodeDao {

    private static String INSERT_STAT = "insert into node (id, version, date_time, uid, username, changeset, lat, lon, tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning id";
    private static String DELETE_STAT = "delete from node where node.id = ?";
    private Db database;

    public NodeDao(Db database) {
        this.database = database;
    }

    public long insertNode(Node node) {
        var conn = database.getConnection();
        ResultSet rs;
        try {
            conn.setAutoCommit(false);
            var stat = conn.prepareStatement(INSERT_STAT);
            stat.setLong(1, node.getId());
            stat.setLong(2, node.getVersion());
            stat.setDate(3, node.getTimestamp());
            stat.setLong(4, node.getUid());
            stat.setString(5, node.getUser());
            stat.setLong(6, node.getChangeset());
            stat.setDouble(7, node.getLat());
            stat.setDouble(8, node.getLon());
            stat.setObject(9, node.getTags());
            rs = stat.executeQuery();
            rs.next();
            conn.commit();
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

    public void deleteNode(Long id) throws SQLException {
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(DELETE_STAT);
        stat.setLong(1, id);
        stat.executeUpdate();
        conn.commit();
    }

    public void insertNodes(List<Node> nodes) throws SQLException {
        String statement = "insert into node (id, version, date_time, uid, username, changeset, lat, lon, tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(statement);
        for (var node : nodes) {
            stat.setLong(1, node.getId());
            stat.setLong(2, node.getVersion());
            stat.setDate(3, node.getTimestamp());
            stat.setLong(4, node.getUid());
            stat.setString(5, node.getUser());
            stat.setLong(6, node.getChangeset());
            stat.setDouble(7, node.getLat());
            stat.setDouble(8, node.getLon());
            stat.setObject(9, node.getTags());
            stat.addBatch();
        }
        stat.executeBatch();
        conn.commit();
    }

}
