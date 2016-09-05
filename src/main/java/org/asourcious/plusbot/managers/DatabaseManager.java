package org.asourcious.plusbot.managers;

import org.asourcious.plusbot.PlusBot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            List<String> lines = Files.readAllLines(new File("credentials.txt").toPath(), Charset.defaultCharset());
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/discorddatabase?autoReconnect=true&useSSL=false", lines.get(0), lines.get(1));
        } catch (ClassNotFoundException | SQLException ex) {
            PlusBot.LOG.fatal("Error establishing connection to database");
            PlusBot.LOG.log(ex);
            System.exit(-1);
        } catch (IOException e) {
            PlusBot.LOG.fatal("Error accessing credentials file");
            PlusBot.LOG.log(e);
            System.exit(-1);
        }
    }

    public HashMap<String, List<String>> loadDataFromTable(String tableName) {
        HashMap<String, List<String>> cache = new HashMap<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s", tableName));

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
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format("INSERT INTO %s (container_id, entry_id) VALUES ('%s', '%s');", table, containerID, entryID));
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
        }
    }

    public void removeEntryFromTable(String containerID, String entryID, String table) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format("DELETE FROM %s WHERE container_id = '%s' AND entry_id = '%s'", table, containerID, entryID));
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
        }
    }

    public String getToken(String tokenName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM main WHERE data_key = '%s_token';", tokenName));
            resultSet.next();
            return resultSet.getString(2);
        } catch (SQLException ex) {
            PlusBot.LOG.log(ex);
            System.exit(-1);
        }
        return null;
    }
}
