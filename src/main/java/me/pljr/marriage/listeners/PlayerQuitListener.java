package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerId);
        corePlayer.setLastseen(System.currentTimeMillis());
        PlayerManager.setPlayerManager(playerId, corePlayer);
        PlayerManager.savePlayer(playerId);
    }
}
