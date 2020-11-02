package me.pljr.marriage.listeners;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            if (event.getEntity() instanceof Player){
                CorePlayer damagerManager = Marriage.getPlayerManager().getCorePlayer(event.getDamager().getUniqueId());
                if (damagerManager.getPartner() == null) return;
                if (damagerManager.getPartner().equals(event.getEntity().getUniqueId())){
                    if (!damagerManager.isPvp()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
