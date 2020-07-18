package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            if (event.getEntity() instanceof Player){
                PlayerManager damagerManager = PlayerUtil.getPlayerManager(event.getDamager().getName());
                if (damagerManager.getPartner() == null) return;
                if (damagerManager.getPartner().equals(event.getEntity().getName())){
                    if (!damagerManager.isPvp()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
