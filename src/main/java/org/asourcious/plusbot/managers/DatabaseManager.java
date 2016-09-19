package org.asourcious.plusbot.managers;

import org.asourcious.plusbot.PlusBot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {
    private Connection connection;

    private Map<String, PreparedStatement> addMap;
    private Map<String, PreparedStatement> removeMap;

    public DatabaseManager() {
        try {
            List<String> lines = Files.readAllLines(new File("credentials.txt").toPath(), Charset.defaultCharset());
            connection = DriverManager.getConnection("jdbc:mysql://localhost/discorddatabase?autoReconnect=true&useSSL=false&serverTimezone=UTC", lines.get(1), lines.get(2));
            addMap = new HashMap<>();
            removeMap = new HashMap<>();
        } catch (SQLException | IOException ex) {
            PlusBot.LOG.log(ex);
            System.exit(-1);
        }
    }

    public Map<String, List<String>> loadDataFromTable(String tableName) {
        ConcurrentHashMap<String, List<String>> cache = new ConcurrentHashMap<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            while (resultSet.next()) {
                String containerID = resultSet.getString(2);
                String entryID = resultSet.getString(3);
                if (!cache.containsKey(containerID))
                    cache.put(containerID, new ArrayList<>());

                cache.get(containerID).add(entryID);
            }
            return cache;
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
            System.exit(-1);
        }
        return null;
    }

    public void addEntryToTable(String containerID, String entryID, String table) {
        try {
            if (!addMap.containsKey(table))
                addMap.put(table, connection.prepareStatement("INSERT INTO " + table + "(container_id, entry_id) VALUES (?, ?);"));

            executeStatement(addMap.get(table), containerID, entryID);
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
        }
    }

    public void removeEntryFromTable(String containerID, String entryID, String table) {
        try {
            if (!removeMap.containsKey(table))
                removeMap.put(table, connection.prepareStatement("DELETE FROM " + table + " WHERE container_id = ? AND entry_id = ?"));
            executeStatement(removeMap.get(table), containerID, entryID);
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
        }
    }

    private void executeStatement(PreparedStatement statement, String containerID, String entryID) throws SQLException {
        statement.setString(1, containerID);
        statement.setString(2, entryID);
        statement.execute();
        statement.clearParameters();
    }
}
