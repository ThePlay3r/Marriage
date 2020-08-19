package me.pljr.marriage.listeners;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.managers.ActionBarManager;
import me.pljr.pljrapi.objects.PLJRActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChangeListener implements Listener {

    @EventHandler
    public void onChange(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        CorePlayer corePlayer = Marriage.getPlayerManager().getPlayerManager(player.getUniqueId());
        if (!corePlayer.isXp()) return;
        if (corePlayer.getPartner() != null){
            Player partner = Bukkit.getPlayer(corePlayer.getPartner());
            if (partner != null && partner.isOnline()){
                if (event.getAmount() > 2){
                    int halfAmount = event.getAmount() / 2;
                    event.setAmount(halfAmount);
                    ActionBarManager.send(partner, new PLJRActionBar("§c§l❤ §aSvadba §8» §fObdržal/a si §b" + halfAmount + "§fXP", 20));
                }
            }
        }
    }
}
