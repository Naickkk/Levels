package fun.eidon.levels.listeners;

import fun.eidon.levels.objects.LevelPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static fun.eidon.levels.Levels.LEVELS;

public class onKill implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();
        LevelPlayer levelPlayer = LevelPlayer.fromPlayer(player);
        levelPlayer.addXp(100.0);

        player.sendMessage("Level: " + levelPlayer.getLevel() + " (" + LEVELS.get(levelPlayer.getLevel()) + ")");
        player.sendMessage("XP: " + levelPlayer.getXp());
        player.sendMessage("Next Level: " + (levelPlayer.getLevel() + 1));
        player.sendMessage("Richiesta: " + levelPlayer.getRequiredXp() + " Progresso: " + levelPlayer.getProgress());
    }
}
