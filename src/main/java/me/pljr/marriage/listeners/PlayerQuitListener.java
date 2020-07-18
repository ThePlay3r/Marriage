package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
        playerManager.setLastseen(System.currentTimeMillis());
        PlayerUtil.setPlayerManager(playerName, playerManager);
        PlayerUtil.savePlayer(event.getPlayer().getName());
    }
}
