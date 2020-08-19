package me.pljr.marriage.listeners;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.managers.ActionBarManager;
import me.pljr.pljrapi.objects.PLJRActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    @EventHandler
    public void onChange(FoodLevelChangeEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            CorePlayer corePlayer = Marriage.getPlayerManager().getPlayerManager(player.getUniqueId());
            if (!corePlayer.isFood()) return;
            if (corePlayer.getPartner() != null){
                Player partner = Bukkit.getPlayer(corePlayer.getPartner());
                if (partner != null && partner.isOnline()){
                    if (partner.getFoodLevel() < 20){
                        int amount = (event.getFoodLevel() - player.getFoodLevel()) / 2;
                        if (amount > 1){
                            event.setFoodLevel(player.getFoodLevel() + amount);
                            partner.setFoodLevel(partner.getFoodLevel() + amount);
                            ActionBarManager.send(partner, new PLJRActionBar("§c§l❤ §aSvadba §8» §fObdržal/a si §b" + amount + " §fjedla", 20));
                        }
                    }
                }
            }
        }
    }
}
