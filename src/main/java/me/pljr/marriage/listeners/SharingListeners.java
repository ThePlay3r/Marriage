package me.pljr.marriage.listeners;

import me.pljr.marriage.config.ActionBarType;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.builders.ActionBarBuilder;
import me.pljr.pljrapispigot.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class SharingListeners implements Listener {

    private final PlayerManager playerManager;

    public SharingListeners(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        MarriagePlayer marriagePlayer = playerManager.getPlayer(player);
        if (!marriagePlayer.isSharedFood()) return;
        if (marriagePlayer.getPartnerID() == null) return;
        if (!PlayerUtil.isPlayer(marriagePlayer.getPartnerID())) return;
        Player partner = Bukkit.getPlayer(marriagePlayer.getPartnerID());
        if (partner.getFoodLevel() >= 20) return;
        int amount = (event.getFoodLevel() - player.getFoodLevel()) / 2;
        if (amount > 1){
            event.setFoodLevel(player.getFoodLevel() + amount);
            partner.setFoodLevel(partner.getFoodLevel() + amount);
            new ActionBarBuilder(ActionBarType.SHARE_FOOD.get())
                    .replaceMessage("{amount}", amount+"")
                    .create().send(player);
            new ActionBarBuilder(ActionBarType.SHARE_FOOD_PARTNER.get())
                    .replaceMessage("{amount}", amount+"")
                    .create().send(partner);
        }
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        MarriagePlayer marriagePlayer = playerManager.getPlayer(player);
        if (!marriagePlayer.isSharedFood()) return;
        if (marriagePlayer.getPartnerID() == null) return;
        if (!PlayerUtil.isPlayer(marriagePlayer.getPartnerID())) return;
        Player partner = Bukkit.getPlayer(marriagePlayer.getPartnerID());
        if (event.getAmount() > 2){
            int halfAmount = event.getAmount() / 2;
            event.setAmount(halfAmount);
            partner.giveExp(halfAmount);
            new ActionBarBuilder(ActionBarType.SHARE_XP.get())
                    .replaceMessage("{amount}", halfAmount+"")
                    .create().send(player);
            new ActionBarBuilder(ActionBarType.SHARE_XP_PARTNER.get())
                    .replaceMessage("{amount}", halfAmount+"")
                    .create().send(partner);
        }
    }
}
