package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerName);
        corePlayer.setLastseen(System.currentTimeMillis());
        PlayerManager.setPlayerManager(playerName, corePlayer);
        PlayerManager.savePlayer(event.getPlayer().getName());
    }
}
