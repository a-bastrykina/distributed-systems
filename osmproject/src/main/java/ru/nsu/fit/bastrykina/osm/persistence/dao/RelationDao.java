package ru.nsu.fit.bastrykina.osm.persistence.dao;

import org.apache.log4j.Logger;
import ru.nsu.fit.bastrykina.osm.persistence.db.Db;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;

import java.sql.SQLException;
import java.util.List;

public class RelationDao {
    private static String INSERT_STAT = "insert into relation (id, version, date_time, uid, username, changeset, tags) VALUES (?, ?, ?, ?, ?, ?, ?) returning id";
    private static String DELETE_STAT = "delete from relation where relation.id = ?";

    private Db database;

    public RelationDao(Db database) {
        this.database = database;
    }

    public long insertRelation(Relation rel) {
        var conn = database.getConnection();
        try {
            conn.setAutoCommit(false);
            var stat = conn.prepareStatement(INSERT_STAT);
            stat.setLong(1, rel.getId());
            stat.setLong(2, rel.getVersion());
            stat.setDate(3, rel.getTimestamp());
            stat.setLong(4, rel.getUid());
            stat.setString(5, rel.getUser());
            stat.setLong(6, rel.getChangeset());
            stat.setObject(7, rel.getTags());
            var rs = stat.executeQuery();

            String memStat = "insert into member (type, role, ref, relation_id) values (?, ?, ?, ?)";
            if (!rel.getMembers().isEmpty()) {
                stat = conn.prepareStatement(memStat);
                for (var member : rel.getMembers()) {
                    stat.setString(1, member.getType());
                    stat.setString(2, member.getRole());
                    stat.setLong(3, member.getRef());
                    stat.setLong(4, rel.getId());
                    stat.addBatch();
                }
                stat.executeUpdate();
            }

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
                Logger.getLogger(getClass()).error("Error executing query :" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteRelation(long id) throws SQLException {
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(DELETE_STAT);
        stat.setLong(1, id);
        stat.executeUpdate();
        conn.commit();
    }

    public void insertRelations(List<Relation> rels) throws SQLException {
        String statement = "insert into relation (id, version, date_time, uid, username, changeset, tags) VALUES (?, ?, ?, ?, ?, ?, ?)";
        var conn = database.getConnection();
        conn.setAutoCommit(false);
        var stat = conn.prepareStatement(statement);
        for (var rel : rels) {
            stat.setLong(1, rel.getId());
            stat.setLong(2, rel.getVersion());
            stat.setDate(3, rel.getTimestamp());
            stat.setLong(4, rel.getUid());
            stat.setString(5, rel.getUser());
            stat.setLong(6, rel.getChangeset());
            stat.setObject(7, rel.getTags());
            stat.addBatch();
        }
        stat.executeBatch();

        String memStat = "insert into member (type, role, ref, relation_id) values (?, ?, ?, ?)";
        stat = conn.prepareStatement(memStat);
        for (var rel : rels) {
            if (!rel.getMembers().isEmpty()) {
                for (var member : rel.getMembers()) {
                    stat.setString(1, member.getType());
                    stat.setString(2, member.getRole());
                    stat.setLong(3, member.getRef());
                    stat.setLong(4, rel.getId());
                    stat.addBatch();
                }
            }
        }
        stat.executeBatch();
        conn.commit();
    }
}
