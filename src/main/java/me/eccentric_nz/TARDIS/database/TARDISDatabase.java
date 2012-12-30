package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISDatabase {

    private static TARDISDatabase instance = new TARDISDatabase();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized TARDISDatabase getInstance() {
        return instance;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTables() {
        try {
            statement = connection.createStatement();
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, size TEXT DEFAULT '', save_sign TEXT DEFAULT '')";
            statement.executeUpdate(queryTARDIS);
            String queryTravellers = "CREATE TABLE IF NOT EXISTS travellers (traveller_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, player TEXT COLLATE NOCASE)";
            statement.executeUpdate(queryTravellers);
            String queryChunks = "CREATE TABLE IF NOT EXISTS chunks (chunk_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);
            String queryDoors = "CREATE TABLE IF NOT EXISTS doors (door_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT, door_direction TEXT DEFAULT 'SOUTH')";
            statement.executeUpdate(queryDoors);
            String queryPlayers = "CREATE TABLE IF NOT EXISTS player_prefs (pp_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, player TEXT COLLATE NOCASE, sfx_on INTEGER DEFAULT 0, platform_on INTEGER DEFAULT 0, quotes_on INTEGER DEFAULT 0, arton_level INTEGER DEFAULT 0)";
            statement.executeUpdate(queryPlayers);
            String queryProtectBlocks = "CREATE TABLE IF NOT EXISTS blocks (b_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', block INTEGER DEFAULT 0, data INTEGER DEFAULT 0)";
            statement.executeUpdate(queryProtectBlocks);
            String queryDestinations = "CREATE TABLE IF NOT EXISTS destinations (dest_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER)";
            statement.executeUpdate(queryDestinations);
            String queryPresets = "CREATE TABLE IF NOT EXISTS areas (area_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER)";
            statement.executeUpdate(queryPresets);
        } catch (SQLException e) {
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Create table error: " + e);
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}