package fun.eidon.levels.placeholders;

import fun.eidon.levels.Levels;
import fun.eidon.levels.objects.LevelPlayer;
import fun.eidon.levels.util.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "levels";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Naick";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        LevelPlayer levelPlayer = new LevelPlayer(player);
        if (identifier.equals("currentlevel")) {
            return String.valueOf(levelPlayer.getLevel());
        }
        if (identifier.equals("currentxp")) {
            return String.valueOf(levelPlayer.getXp());
        }
        if (identifier.equals("nextlevel")) {
            return String.valueOf(levelPlayer.getLevel() + 1);
        }
        if (identifier.equals("requiredxp")) {
            return new DecimalFormat("0.00").format(levelPlayer.getRequiredXp());
        }
        if (identifier.equals("levelxp")) {
            return new DecimalFormat("0.00").format(Levels.LEVELS.get(levelPlayer.getLevel() + 1));
        }
        if (identifier.equals("progress")) {
            return new DecimalFormat("0.00").format(levelPlayer.getProgress());
        }
        if (identifier.equals("percentage")) {
            return String.valueOf(new Util().calculatePercentage(levelPlayer.getProgress(), levelPlayer.getProgress() + levelPlayer.getRequiredXp()));
        }
        if (identifier.equals("progressbar")) {
            if (levelPlayer.getLevel() == 100) return "§a§lMAX";
            return Util.getProgressBar((int) Math.round(levelPlayer.getProgress()), (int) Math.round(levelPlayer.getProgress() + levelPlayer.getRequiredXp()), 20, '|', ChatColor.GREEN, ChatColor.GRAY);
        }
        if (identifier.equals("xp_formatted")) {
            return Util.format(levelPlayer.getXp().longValue());
        }
        if (identifier.equals("requiredxp_formatted")) {
            return Util.format(levelPlayer.getRequiredXp().longValue());
        }
        if (identifier.equals("levelxp_formatted")) {
            return Util.format(Levels.LEVELS.get(levelPlayer.getLevel() + 1).longValue());
        }
        if (identifier.equals("progress_formatted")) {
            return Util.format(levelPlayer.getProgress().longValue());
        }
        return null;
    }
}