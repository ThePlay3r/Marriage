package me.pljr.marriage.menus;

import me.pljr.marriage.config.Lang;
import me.pljr.marriage.config.MenuItem;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.builders.GUIBuilder;
import me.pljr.pljrapispigot.builders.ItemBuilder;
import me.pljr.pljrapispigot.objects.GUIItem;
import me.pljr.pljrapispigot.utils.ChatUtil;
import me.pljr.pljrapispigot.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarryMenu {

    public MarryMenu(MarriagePlayer marriagePlayer){
        Player player = Bukkit.getPlayer(marriagePlayer.getUniqueId());
        if (player == null) return;

        GUIBuilder builder = new GUIBuilder(Lang.MARRY_MENU_TITLE.get(), 6);
        for (int i = 0; i<45;i++){
            builder.setItem(i, MenuItem.MARRY_BACKGROUND.get());
        }

        ItemStack heart = MenuItem.MARRY_HEART.get();
        builder.setItem(2, heart);
        builder.setItem(3, heart);
        builder.setItem(5, heart);
        builder.setItem(6, heart);
        builder.setItem(10, heart);
        builder.setItem(13, heart);
        builder.setItem(16, heart);
        builder.setItem(20, heart);
        builder.setItem(24, heart);
        builder.setItem(30, heart);
        builder.setItem(32, heart);
        builder.setItem(40, heart);

        ItemStack white = MenuItem.MARRY_WHITE.get();
        builder.setItem(11, white);
        builder.setItem(12, white);
        builder.setItem(14, white);
        builder.setItem(15, white);
        builder.setItem(21, white);
        builder.setItem(23, white);
        builder.setItem(31, white);

        builder.setItem(45, new GUIItem(MenuItem.MARRY_LAST_SEEN.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry seen");
        }));
        builder.setItem(46, new GUIItem(MenuItem.MARRY_SHARED_XP.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry xp");
        }));
        builder.setItem(47, new GUIItem(MenuItem.MARRY_TP_HOME.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry home");
        }));
        builder.setItem(48, new GUIItem(MenuItem.MARRY_GIFT.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry give");
        }));
        builder.setItem(49, new GUIItem(MenuItem.MARRY_TP.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry tp");
        }));
        builder.setItem(50, new GUIItem(MenuItem.MARRY_PVP.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry pvp");
        }));
        builder.setItem(51, new GUIItem(MenuItem.MARRY_SET_HOME.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry sethome");
        }));
        builder.setItem(52, new GUIItem(MenuItem.MARRY_SHARED_FOOD.get(), run -> {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry food");
        }));
        builder.setItem(53, new GUIItem(MenuItem.MARRY_PRIVATE_CHAT.get(), run -> {
            player.closeInventory();
            ChatUtil.sendMessage(player, Lang.CHAT_USAGE.get());
        }));

        if (marriagePlayer.getPartnerID() == null){
            builder.setItem(22, new ItemBuilder(MenuItem.PLAYER_HEAD.get())
                    .withOwner(player.getName())
                    .replaceName("{name}", player.getName())
                    .withLore()
                    .create());
        }else{
            String partnerName = PlayerUtil.getName(marriagePlayer.getPartnerID());
            builder.setItem(22, new GUIItem(new ItemBuilder(MenuItem.PLAYER_HEAD.get())
                    .withOwner(partnerName)
                    .replaceName("{name}", partnerName)
                    .create(),
                    run -> Bukkit.dispatchCommand(player, "marry divorce")));
        }
        builder.create().open(player);
    }
}
