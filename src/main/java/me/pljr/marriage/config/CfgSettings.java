package me.pljr.marriage.config;

import me.pljr.pljrapi.managers.ConfigManager;

public class CfgSettings {
    public static boolean bungee;
    public static boolean particles;
    public static boolean sounds;
    public static int costMarry;
    public static int costDivorce;
    public static int cooldown;
    public static boolean sharedFood;
    public static boolean sharedXp;
    public static boolean togglepvp;
    public static boolean togglefood;
    public static boolean togglexp;
    public static boolean menu;

    public static void load(ConfigManager config){
        CfgSettings.bungee = config.getBoolean("settings.bungee");
        CfgSettings.particles = config.getBoolean("settings.particles");
        CfgSettings.sounds = config.getBoolean("settings.sounds");
        CfgSettings.costMarry = config.getInt("settings.cost-marry");
        CfgSettings.costDivorce = config.getInt("settings.cost-divorce");
        CfgSettings.cooldown = config.getInt("settings.cooldown");
        CfgSettings.sharedFood = config.getBoolean("settings.shared-food");
        CfgSettings.sharedXp = config.getBoolean("settings.shared-xp");
        CfgSettings.togglepvp = config.getBoolean("settings.toggle-pvp");
        CfgSettings.togglefood = config.getBoolean("settings.toggle-food");
        CfgSettings.togglexp = config.getBoolean("settings.toggle-xp");
        CfgSettings.menu = config.getBoolean("settings.menu");
    }
}
