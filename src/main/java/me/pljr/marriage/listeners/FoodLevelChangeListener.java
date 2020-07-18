package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.PlayerUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
            PlayerManager playerManager = PlayerUtil.getPlayerManager(player.getName());
            if (playerManager.getPartner() != null){
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner != null && partner.isOnline()){
                    if (partner.getFoodLevel() < 20){
                        int amount = (event.getFoodLevel() - player.getFoodLevel()) / 2;
                        if (amount > 1){
                            event.setFoodLevel(player.getFoodLevel() + amount);
                            partner.setFoodLevel(partner.getFoodLevel() + amount);
                            partner.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l❤ §aSvadba §8» §fObdržal/a si §b" + amount + " §fjedla"));
                        }
                    }
                }
            }
        }
    }
}
