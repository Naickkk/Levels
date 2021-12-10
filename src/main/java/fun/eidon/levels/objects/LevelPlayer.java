package fun.eidon.levels.objects;

import fun.eidon.levels.Levels;
import fun.eidon.levels.database.Database;
import fun.eidon.levels.util.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static fun.eidon.levels.Levels.LEVELS;
import static fun.eidon.levels.Levels.PLAYERS;

public class LevelPlayer {

    @Getter
    public final String playerName;
    @Getter
    public final Player player;
    @Getter
    public final Integer level;
    @Getter
    public final Double requiredXp;
    @Getter
    public Double xp;
    @Getter
    public Double progress;

    public LevelPlayer(String playerName) {
        this.playerName = playerName;
        this.player = null;
        this.xp = PLAYERS.getOrDefault(playerName, 0.0);
        this.level = this.calcCurrentLevel(this.playerName);
        this.requiredXp = this.calcRequired(this.playerName);
        this.progress = this.calcProgress(this.playerName);
    }

    public LevelPlayer(Player player) {
        this.playerName = player.getName();
        this.player = player;
        this.xp = PLAYERS.getOrDefault(playerName, 0.0);
        this.level = this.calcCurrentLevel(this.playerName);
        this.requiredXp = this.calcRequired(this.playerName);
        this.progress = this.calcProgress(this.playerName);
    }

    public static LevelPlayer fromPlayer(Player player) {
        if (!PLAYERS.containsKey(player.getName()))
            Database.update("INSERT INTO `levels` (playerName, xp) values ('" + player.getName() + "', '" + 0 + "')");
        return new LevelPlayer(player);
    }

    public void addXp(Double xp) {
        this.xp += xp;
        boolean levelup = this.calcRequired(playerName) < 0;
        Database.update("UPDATE `levels` SET xp='" + this.xp + "' WHERE playerName='" + this.playerName + "'");
        PLAYERS.put(this.playerName, this.xp);

        if (levelup) {
            Bukkit.getScheduler().runTaskLater(Levels.getInstance(), () -> {
                this.player.sendTitle("§c§lL   §6§lE   §e§lV   §d§lE   §5§lL   §3§lU   §b§lP", "§fSei salito a livello §7" + (this.level + 1) + "§f!");
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.7F);
            }, 20L);
            Bukkit.getScheduler().runTaskLater(Levels.getInstance(), () -> {
                this.player.sendTitle("§c§lL  §6§lE  §e§lV  §d§lE  §5§lL  §3§lU  §b§lP", "§fSei salito a livello §7" + (this.level + 1) + "§f!");
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.8F);
            }, 25L);
            Bukkit.getScheduler().runTaskLater(Levels.getInstance(), () -> {
                this.player.sendTitle("§c§lL §6§lE §e§lV §d§lE §5§lL §3§lU §b§lP", "§fSei salito a livello §7" + (this.level + 1) + "§f!");
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.9F);
            }, 30L);
            Bukkit.getScheduler().runTaskLater(Levels.getInstance(), () -> {
                this.player.sendTitle("§c§lL§6§lE§e§lV§d§lE§5§lL§3§lU§b§lP", "§fSei salito a livello §7" + (this.level + 1) + "§f!");
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 1F);
            }, 35L);
            Bukkit.getScheduler().runTaskLater(Levels.getInstance(), () -> {
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            }, 40L);
        }
    }

    private Double calcRequired(String playerName) {
        if (this.level == 100) return 0.0;
        return Util.getXPLevel(this.level + 1) - this.xp;
    }

    private Double calcProgress(String playerName) {
        if (this.level == 100) return 0.0;
        return LEVELS.get(this.level + 1) - this.requiredXp;
    }

    private Integer calcCurrentLevel(String playerName) {
        int level = 1;
        for (Integer integer : LEVELS.keySet()) {
            if (this.xp >= Util.getXPLevel(100)) return 100;
            if (Util.getXPLevel(integer) > this.xp) {
                level = integer - 1;
                break;
            }
        }
        return level;
    }

}
