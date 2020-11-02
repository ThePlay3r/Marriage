package me.pljr.marriage.listeners;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.marriage.utils.MarryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteractEntityListener implements Listener {
    private final List<Player> kissing = new ArrayList<>();

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event){
        if (event.getRightClicked() instanceof Player){
            Player player1 = event.getPlayer();
            Player player2 = (Player) event.getRightClicked();
            CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(player1.getUniqueId());
            if (corePlayer.getPartner() != null && corePlayer.getPartner().equals(player2.getUniqueId())){
                if (!kissing.contains(player1)){
                    MarryUtil.kiss(player1, player2);
                    kissing.add(player1);
                    kissing.add(player2);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Marriage.getInstance(), () ->{
                        kissing.remove(player1);
                        kissing.remove(player2);
                    }, 20*3);
                }
            }
        }
    }
}
