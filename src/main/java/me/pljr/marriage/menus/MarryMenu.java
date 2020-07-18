package me.pljr.marriage.menus;

import me.pljr.marriage.Marriage;
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
    private static final String title = "§a§lSvadba";

    public static Inventory getMenu(String username){
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        PlayerManager playerManager = PlayerUtil.getPlayerManager(username);
        String partner = playerManager.getPartner();

        ItemStack background = GuiUtil.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§c");
        for (int i = 0; i<45; i++){
            inventory.setItem(i, background);
        }

        ItemStack heart = GuiUtil.createItem(Material.RED_STAINED_GLASS_PANE, "§c");
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

        ItemStack white = GuiUtil.createItem(Material.WHITE_STAINED_GLASS_PANE, "§f");
        inventory.setItem(11, white);
        inventory.setItem(12, white);
        inventory.setItem(14, white);
        inventory.setItem(15, white);
        inventory.setItem(21, white);
        inventory.setItem(23, white);
        inventory.setItem(31, white);

        ItemStack lastseen = GuiUtil.createItem(Material.REDSTONE_LAMP, "§eNaposledy videný/ná", "§8» §fKlikni pre zistenie,", "§fkedy bol/a partner/ka", "§fnaposledy na serveri.");
        inventory.setItem(45, lastseen);

        ItemStack sharedxpinfo = GuiUtil.createItem(Material.EXPERIENCE_BOTTLE, "§eZdieľané skúsenosti", "§8» §fZískané skúsenosti sa delia", "§f50/50 medzi Vami a partnerom/kou.");
        inventory.setItem(46, sharedxpinfo);

        ItemStack tphome = GuiUtil.createItem(Material.RED_BED, "§eTeleportacia domov", "§8» §fKlikni pre teleportovanie na domov.");
        inventory.setItem(47, tphome);

        ItemStack gift = GuiUtil.createItem(Material.CHEST, "§eDárek", "§8» §fKlikni pre darovanie predmetu v ruke.");
        inventory.setItem(48, gift);

        ItemStack tp = GuiUtil.createItem(Material.ENDER_PEARL, "§eTeleportacia", "§8» §fKlikni pre teleportovanie.");
        inventory.setItem(49, tp);

        ItemStack pvp = GuiUtil.createItem(Material.DIAMOND_SWORD, "§ePvP", "§8» §fKlikni pre zapnutie/vypnutie PvP.");
        inventory.setItem(50, pvp);

        ItemStack sethome = GuiUtil.createItem(Material.OAK_DOOR, "§eNastavenie domu", "§8» §fKlikni pre nastavenie domu.");
        inventory.setItem(51, sethome);

        ItemStack sharedfoodinfo = GuiUtil.createItem(Material.COOKED_PORKCHOP, "§eZdieľané jedlo", "§8» §fZískané jedlo sa deli", "§f50/50 medzi Vami a partnerom/kou.");
        inventory.setItem(52, sharedfoodinfo);

        ItemStack privatechatinfo = GuiUtil.createItem(Material.PAPER, "§eSpoločný chat", "§8» §fKlikni pre pisanie do spoločnéh chatu.");
        inventory.setItem(53, privatechatinfo);

        ItemStack head;
        if (partner == null){
            head = GuiUtil.createHead("Steve", "§cNemáš partnera/ku");
        }else{
            head = GuiUtil.createHead(partner, "§e" + partner, "§8» §fKlikni pre zrušenie manželstva.");
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
            player.sendMessage("§c§l❤ §aSvadba §8» §bPoužitie: §f/marry c <text>");
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);
        }
    }
}
