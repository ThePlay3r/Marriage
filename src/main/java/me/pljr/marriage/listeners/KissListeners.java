package me.pljr.marriage.listeners;

import me.pljr.marriage.config.ActionBarType;
import me.pljr.marriage.config.Settings;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.xenondevs.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class  KissListeners implements Listener {

    private final Plugin plugin;
    private final PlayerManager playerManager;
    private final Settings settings;

    private final List<Player> kissing;


    public KissListeners(Plugin plugin, PlayerManager playerManager, Settings settings){
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.settings = settings;

        this.kissing = new ArrayList<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event){
        if (!(event.getRightClicked() instanceof Player)) return;
        Player player = event.getPlayer();
        MarriagePlayer marriagePlayer = playerManager.getPlayer(player.getUniqueId());
        if (marriagePlayer.getPartnerID() == null) return;
        Player target = (Player) event.getRightClicked();
        // Converted to String, because comparing as UUIDs doesn't work, why should it, right? RIGHT?! ¯\_(ツ)_/¯
        if (!marriagePlayer.getPartnerID().toString().equals(target.getUniqueId().toString())) return;
        if (kissing.contains(player) || kissing.contains(target)) return;
        kissing.add(player);
        kissing.add(target);
        if (settings.isParticles()){
            ParticleEffect.HEART.display(player.getLocation().clone().add(0,1,0),
                    0.3f, 0.3f, 0.3f, 1, 4, null);
            ParticleEffect.HEART.display(target.getLocation().clone().add(0,1,0),
                    0.3f, 0.3f, 0.3f, 1, 4, null);
        }
        ActionBarType.KISS.get().send(player);
        ActionBarType.KISS_PARTNER.get().send(target);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, ()->{
            kissing.remove(player);
            kissing.remove(target);
        }, 20*3);
    }
}
