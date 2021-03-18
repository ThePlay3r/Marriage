package me.pljr.marriage.listeners;

import lombok.AllArgsConstructor;
import me.pljr.marriage.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        playerManager.getPlayer(playerId, marriagePlayer -> {
            marriagePlayer.setLastSeen(System.currentTimeMillis());
            playerManager.setPlayer(playerId, marriagePlayer);
        });
        playerManager.savePlayer(playerId);
    }
}
