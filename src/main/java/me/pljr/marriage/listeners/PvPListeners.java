package me.pljr.marriage.listeners;

import me.pljr.marriage.config.ActionBarType;
import me.pljr.marriage.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListeners implements Listener {

    private final PlayerManager playerManager;

    public PvPListeners(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        playerManager.getPlayer(victim.getUniqueId(), marriageVictim -> {
            if (marriageVictim.getPartnerID() == null) return;
            Player attacker = (Player) event.getDamager();
            if (marriageVictim.getPartnerID() != attacker.getUniqueId()) return;
            playerManager.getPlayer(attacker.getUniqueId(), marriageAttacker -> {
                if (!marriageVictim.isPvp() || !marriageAttacker.isPvp()) event.setCancelled(true);
                ActionBarType.NO_PVP.get().send(attacker);
            });
        });
    }
}
