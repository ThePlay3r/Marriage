package me.pljr.marriage.menus;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgMenu;
import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.GuiUtil;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MarryMenu implements Listener {
    private static final String title = CfgMenu.title;

    public static Inventory getMenu(String username){
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        PlayerManager playerManager = PlayerUtil.getPlayerManager(username);
        String partner = playerManager.getPartner();

        ItemStack background = CfgMenu.background;
        for (int i = 0; i<45; i++){
            inventory.setItem(i, background);
        }

        ItemStack heart = CfgMenu.heart;
        inventory.setItem(2, heart);
        inventory.setItem(3, heart);
        inventory.setItem(5, heart);
        inventory.setItem(6, heart);
        inventory.setItem(10, heart);
        inventory.setItem(13, heart);
        inventory.setItem(16, heart);
        inventory.setItem(20, heart);
        inventory.setItem(24, heart);
        inventory.setItem(30, heart);
        inventory.setItem(32, heart);
        inventory.setItem(40, heart);

        ItemStack white = CfgMenu.white;
        inventory.setItem(11, white);
        inventory.setItem(12, white);
        inventory.setItem(14, white);
        inventory.setItem(15, white);
        inventory.setItem(21, white);
        inventory.setItem(23, white);
        inventory.setItem(31, white);

        ItemStack lastseen = CfgMenu.lastseen;
        inventory.setItem(45, lastseen);

        ItemStack sharedxpinfo = CfgMenu.sharedxpinfo;
        inventory.setItem(46, sharedxpinfo);

        ItemStack tphome = CfgMenu.tphome;
        inventory.setItem(47, tphome);

        ItemStack gift = CfgMenu.gift;
        inventory.setItem(48, gift);

        ItemStack tp = CfgMenu.tp;
        inventory.setItem(49, tp);

        ItemStack pvp = CfgMenu.pvp;
        inventory.setItem(50, pvp);

        ItemStack sethome = CfgMenu.sethome;
        inventory.setItem(51, sethome);

        ItemStack sharedfoodinfo = CfgMenu.sharedfoodinfo;
        inventory.setItem(52, sharedfoodinfo);

        ItemStack privatechatinfo = CfgMenu.privatechatinfo;
        inventory.setItem(53, privatechatinfo);

        ItemStack head;
        if (partner == null){
            head = GuiUtil.createHead("Steve", CfgMessages.messages.get(Message.NO_PARTNER));
        }else{
            head = GuiUtil.createHead(partner, "§e" + partner, CfgMessages.messages.get(Message.CLICK_TO_DIVORCE));
        }
        inventory.setItem(22, head);

        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player) || !event.getView().getTitle().equals(title)) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (slot == 22){
            Bukkit.dispatchCommand(player, "marry divorce");
        } else if (slot == 45){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry seen");
        } else if (slot == 47){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry home");
        } else if (slot == 48){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry give");
        } else if (slot == 49){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry tp");
        } else if (slot == 50){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry pvp");
        } else if (slot == 51){
            player.closeInventory();
            Bukkit.dispatchCommand(player, "marry sethome");
        } else if (slot == 53){
            player.closeInventory();
            player.sendMessage("");
            player.sendMessage(CfgMessages.messages.get(Message.CHAT_USAGE));
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);
        }
    }
}
