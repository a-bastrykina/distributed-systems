package ru.nsu.fit.bastrykina.osm.persistence.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Db {
    private Connection connection = null;
    private static String connectionUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static String user = "postgres";
    private static String password = "secretpassword";

    private static Properties  props = new Properties();

    static {
        props.put("user", user);
        props.put("password", password);
        props.put("stringtype", "unspecified");
//        props.put("stringtype", "unspecified");
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connectionUrl, props);
            } catch (SQLException throwables) {
                Logger.getLogger(Db.class).error("Error connecting to database");
                throwables.printStackTrace();
                throw new RuntimeException(throwables);
            }
        }
        return connection;
    }
}
