package me.pljr.marriage.config;

import me.pljr.pljrapispigot.managers.ConfigManager;

public class CfgSettings {
    public static boolean BUNGEE;
    public static boolean PARTICLES;
    public static boolean SOUNDS;
    public static boolean TOGGLE_XP;
    public static boolean TOGGLE_PVP;
    public static boolean TOGGLE_FOOD;
    public static boolean MENU;
    public static int COST_MARRY;
    public static int COST_DIVORCE;

    public static void load(ConfigManager config){
        BUNGEE = config.getBoolean("settings.bungee");
        PARTICLES = config.getBoolean("settings.particles");
        SOUNDS = config.getBoolean("settings.sounds");
        TOGGLE_XP = config.getBoolean("settings.toggle-xp");
        TOGGLE_PVP = config.getBoolean("settings.toggle-pvp");
        TOGGLE_FOOD = config.getBoolean("settings.toggle-food");
        MENU = config.getBoolean("settings.menu");
        COST_MARRY = config.getInt("settings.cost-marry");
        COST_DIVORCE = config.getInt("settings.cost-divorce");
    }
}
