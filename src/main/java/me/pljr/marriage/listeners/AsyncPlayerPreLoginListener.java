package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    private final PlayerManager playerManager;

    public AsyncPlayerPreLoginListener(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event){
        playerManager.getPlayer(event.getUniqueId());
    }
}
