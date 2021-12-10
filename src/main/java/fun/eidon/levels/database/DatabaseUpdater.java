package fun.eidon.levels.database;

import fun.eidon.levels.Levels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseUpdater {

    public static void loadPlayers() {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = Levels.getHikari().getConnection();
            ps = connection.prepareStatement("SELECT * FROM levels");
            ps.execute();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Levels.PLAYERS.put(rs.getString("playerName"), rs.getDouble("xp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
