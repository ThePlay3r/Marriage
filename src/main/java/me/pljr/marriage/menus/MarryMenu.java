package me.pljr.marriage.menus;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.config.CfgMenu;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.XMaterial;
import me.pljr.pljrapi.builders.GUIBuilder;
import me.pljr.pljrapi.builders.ItemBuilder;
import me.pljr.pljrapi.managers.GUIManager;
import me.pljr.pljrapi.objects.GUI;
import me.pljr.pljrapi.objects.GUIItem;
import me.pljr.pljrapi.utils.ChatUtil;
import me.pljr.pljrapi.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MarryMenu {

    public static void open(Player player){
        PlayerManager playerManager = Marriage.getPlayerManager();
        CorePlayer corePlayer = playerManager.getCorePlayer(player.getUniqueId());
        UUID partnerId = corePlayer.getPartner();

        GUIBuilder guiBuilder = new GUIBuilder(CfgMenu.title, 6);

        ItemStack background = CfgMenu.background;
        for (int i = 0; i<45; i++){
            guiBuilder.setItem(i, background);
        }

        ItemStack heart = CfgMenu.heart;
        guiBuilder.setItem(2, heart);
        guiBuilder.setItem(3, heart);
        guiBuilder.setItem(5, heart);
        guiBuilder.setItem(6, heart);
        guiBuilder.setItem(10, heart);
        guiBuilder.setItem(13, heart);
        guiBuilder.setItem(16, heart);
        guiBuilder.setItem(20, heart);
        guiBuilder.setItem(24, heart);
        guiBuilder.setItem(30, heart);
        guiBuilder.setItem(32, heart);
        guiBuilder.setItem(40, heart);

        ItemStack white = CfgMenu.white;
        guiBuilder.setItem(11, white);
        guiBuilder.setItem(12, white);
        guiBuilder.setItem(14, white);
        guiBuilder.setItem(15, white);
        guiBuilder.setItem(21, white);
        guiBuilder.setItem(23, white);
        guiBuilder.setItem(31, white);

        ItemStack lastseen = CfgMenu.lastseen;
        guiBuilder.setItem(45, new GUIItem(lastseen, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry seen");
            }
        }));

        ItemStack sharedxpinfo = CfgMenu.sharedxpinfo;
        guiBuilder.setItem(46, new GUIItem(sharedxpinfo, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry xp");
            }
        }));

        ItemStack tphome = CfgMenu.tphome;
        guiBuilder.setItem(47, new GUIItem(tphome, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry home");
            }
        }));

        ItemStack gift = CfgMenu.gift;
        guiBuilder.setItem(48, new GUIItem(gift, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry give");
            }
        }));

        ItemStack tp = CfgMenu.tp;
        guiBuilder.setItem(49, new GUIItem(tp, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry tp");
            }
        }));

        ItemStack pvp = CfgMenu.pvp;
        guiBuilder.setItem(50, new GUIItem(pvp, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry pvp");
            }
        }));

        ItemStack sethome = CfgMenu.sethome;
        guiBuilder.setItem(51, new GUIItem(sethome, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry sethome");
            }
        }));

        ItemStack sharedfoodinfo = CfgMenu.sharedfoodinfo;
        guiBuilder.setItem(52, new GUIItem(sharedfoodinfo, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "marry food");
            }
        }));

        ItemStack privatechatinfo = CfgMenu.privatechatinfo;
        guiBuilder.setItem(53, new GUIItem(privatechatinfo, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                player.closeInventory();
                ChatUtil.sendMessageClean(player, "");
                ChatUtil.sendMessage(player, CfgLang.lang.get(Lang.CHAT_USAGE));
                ChatUtil.sendMessageClean(player, "");
                player.playSound(player.getLocation(), CfgSounds.sounds.get(Sounds.MENU_CLICK), 2, 1);
            }
        }));

        ItemStack head;
        if (partnerId == null){
            head = new ItemBuilder(XMaterial.PLAYER_HEAD)
                    .withOwner(player.getName())
                    .withName(CfgLang.lang.get(Lang.NO_PARTNER))
                    .create();
        }else{
            String partnerName = PlayerUtil.getName(Bukkit.getOfflinePlayer(partnerId));
            head = new ItemBuilder(XMaterial.PLAYER_HEAD)
                    .withOwner(partnerName)
                    .withName("§e" + partnerName)
                    .withLore(CfgLang.lang.get(Lang.CLICK_TO_DIVORCE))
                    .create();
        }
        guiBuilder.setItem(22, new GUIItem(head, new GUIManager.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent inventoryClickEvent) {
                Bukkit.dispatchCommand(player, "marry divorce");
            }
        }));

        GUI gui = guiBuilder.create();
        gui.open(player);
    }
}
