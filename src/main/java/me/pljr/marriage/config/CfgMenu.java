package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.pljrapi.managers.ConfigManager;
import org.bukkit.inventory.ItemStack;

public class CfgMenu {
    private final static ConfigManager config = Marriage.getConfigManager();

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
        CfgMenu.title = config.getString("menu.title");
        CfgMenu.heart = config.getSimpleItemStack("menu.heart");
        CfgMenu.white = config.getSimpleItemStack("menu.white");
        CfgMenu.background = config.getSimpleItemStack("menu.background");
        CfgMenu.lastseen = config.getSimpleItemStack("menu.lastseen");
        CfgMenu.sharedxpinfo = config.getSimpleItemStack("menu.sharedxpinfo");
        CfgMenu.tphome = config.getSimpleItemStack("menu.tphome");
        CfgMenu.gift = config.getSimpleItemStack("menu.gift");
        CfgMenu.tp = config.getSimpleItemStack("menu.tp");
        CfgMenu.pvp = config.getSimpleItemStack("menu.pvp");
        CfgMenu.sethome = config.getSimpleItemStack("menu.sethome");
        CfgMenu.sharedfoodinfo = config.getSimpleItemStack("menu.sharedfoodinfo");
        CfgMenu.privatechatinfo = config.getSimpleItemStack("menu.privatechatinfo");
    }
}
