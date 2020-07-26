package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.managers.ConfigManager;
import org.bukkit.inventory.ItemStack;

public class CfgMenu {
    private final static ConfigManager config = Marriage.getConfigManager();

    public static boolean oldHead;
    public static String title;
    public static ItemStack heart;
    public static ItemStack white;
    public static ItemStack background;
    public static ItemStack lastseen;
    public static ItemStack sharedxpinfo;
    public static ItemStack tphome;
    public static ItemStack gift;
    public static ItemStack tp;
    public static ItemStack pvp;
    public static ItemStack sethome;
    public static ItemStack sharedfoodinfo;
    public static ItemStack privatechatinfo;

    public static void load(){
        CfgMenu.oldHead = config.getBoolean("menu.old-head");
        CfgMenu.title = config.getString("menu.title");
        CfgMenu.heart = config.getItemStack("menu.heart");
        CfgMenu.white = config.getItemStack("menu.white");
        CfgMenu.background = config.getItemStack("menu.background");
        CfgMenu.lastseen = config.getItemStack("menu.lastseen");
        CfgMenu.sharedxpinfo = config.getItemStack("menu.sharedxpinfo");
        CfgMenu.tphome = config.getItemStack("menu.tphome");
        CfgMenu.gift = config.getItemStack("menu.gift");
        CfgMenu.tp = config.getItemStack("menu.tp");
        CfgMenu.pvp = config.getItemStack("menu.pvp");
        CfgMenu.sethome = config.getItemStack("menu.sethome");
        CfgMenu.sharedfoodinfo = config.getItemStack("menu.sharedfoodinfo");
        CfgMenu.privatechatinfo = config.getItemStack("menu.privatechatinfo");
    }
}
