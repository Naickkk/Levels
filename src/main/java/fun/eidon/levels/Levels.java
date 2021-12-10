package fun.eidon.levels;

import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import fun.eidon.levels.database.Database;
import fun.eidon.levels.database.DatabaseUpdater;
import fun.eidon.levels.listeners.onKill;
import fun.eidon.levels.placeholders.Placeholders;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;

public final class Levels extends JavaPlugin {

    public static HashMap<Integer, Double> LEVELS = Maps.newHashMap();
    public static HashMap<String, Double> PLAYERS = Maps.newHashMap();
    @Getter
    private static Levels instance;
    @Getter
    private static HikariDataSource hikari;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        //config
        this.saveDefaultConfig();

        //database
        this.startDatabase();
        Database.createTables();
        DatabaseUpdater.loadPlayers();

        //levels
        this.loadLevels();

        //registers
        this.registerListeners();

        //placeholders
        new Placeholders().register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);

        if (hikari != null) {
            hikari.close();
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new onKill(), this);
    }

    private void loadLevels() {
        final double m1_10 = 1.5;
        final double m11_20 = 1.125;
        final double m21_30 = 1.1;
        final double m31_40 = 1.06;
        final double m41_50 = 1.035;
        final double m51_60 = 1.03;
        final double m61_71 = 1.025;
        final double m71_80 = 1.02;
        final double m81_90 = 1.015;
        final double m91_100 = 1.01;
        double current = m1_10;
        double xpLevel = 0;
        for (int i = 1; i < 101; i++) {
            if (i > 10) current = m11_20;
            if (i > 20) current = m21_30;
            if (i > 30) current = m31_40;
            if (i > 40) current = m41_50;
            if (i > 50) current = m51_60;
            if (i > 60) current = m61_71;
            if (i > 70) current = m71_80;
            if (i > 80) current = m81_90;
            if (i > 90) current = m91_100;

            xpLevel = xpLevel == 0 ? 50 : current * xpLevel;

            LEVELS.put(i, xpLevel);

        }
    }

    @SneakyThrows
    private void startDatabase() {

        if ((this.getConfig().getString("database.databaseName") == null)
                || (this.getConfig().getString("database.user") == null)
                || (this.getConfig().getString("database.password") == null)) {

            Bukkit.getConsoleSender().sendMessage("§4|§c Errore di configurazione del Database;");

            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getScheduler().cancelTasks(this);

            return;
        }

        hikari = new HikariDataSource();

        Locale locale = new Locale("en_US");
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", "localhost");
        hikari.addDataSourceProperty("port", 3306);
        hikari.addDataSourceProperty("databaseName", this.getConfig().getString("database.databaseName"));
        hikari.addDataSourceProperty("user", this.getConfig().getString("database.user"));
        hikari.addDataSourceProperty("password", this.getConfig().getString("database.password"));
        hikari.addDataSourceProperty("autoReconnect", true);
        hikari.addDataSourceProperty("useSSL", false);
        hikari.setConnectionTimeout(30000);
    }
}
