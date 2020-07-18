package me.pljr.marriage.listeners;

import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.PlayerUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChangeListener implements Listener {

    @EventHandler
    public void onChange(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(player.getName());
        if (playerManager.getPartner() != null){
            String partnerName = playerManager.getPartner();
            Player partner = Bukkit.getPlayer(partnerName);
            if (partner != null && partner.isOnline()){
                if (event.getAmount() > 2){
                    int halfAmount = event.getAmount() / 2;
                    event.setAmount(halfAmount);
                    partner.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l❤ §aSvadba §8» §fObdržal/a si §b" + halfAmount + "§fXP"));
                }
            }
        }
    }
}
