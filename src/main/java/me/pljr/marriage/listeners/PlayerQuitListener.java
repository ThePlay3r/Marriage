package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final PlayerManager playerManager;

    public PlayerQuitListener(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        UUID playerId = event.getPlayer().getUniqueId();
        MarriagePlayer marriagePlayer = playerManager.getPlayer(playerId);
        marriagePlayer.setLastSeen(System.currentTimeMillis());
        playerManager.setPlayer(playerId, marriagePlayer);
    }
}
