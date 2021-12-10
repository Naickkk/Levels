package fun.eidon.levels.database;

import fun.eidon.levels.Levels;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    public static void update(String sql) {
        Bukkit.getScheduler().runTaskAsynchronously(Levels.getInstance(), () -> {
            try {
                Connection connection = Levels.getHikari().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                connection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public static void createTables() {
        Database.update("CREATE TABLE IF NOT EXISTS `levels` (`playerName` TEXT, `xp` DOUBLE)");
    }
}
