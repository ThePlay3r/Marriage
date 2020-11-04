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
    public static boolean defaultPvP;
    public static boolean defaultFood;
    public static boolean defaultXP;

    public static void load(ConfigManager config){
        bungee = config.getBoolean("settings.bungee");
        particles = config.getBoolean("settings.particles");
        sounds = config.getBoolean("settings.sounds");
        costMarry = config.getInt("settings.cost-marry");
        costDivorce = config.getInt("settings.cost-divorce");
        cooldown = config.getInt("settings.cooldown");
        sharedFood = config.getBoolean("settings.shared-food");
        sharedXp = config.getBoolean("settings.shared-xp");
        togglepvp = config.getBoolean("settings.toggle-pvp");
        togglefood = config.getBoolean("settings.toggle-food");
        togglexp = config.getBoolean("settings.toggle-xp");
        menu = config.getBoolean("settings.menu");
        defaultPvP = config.getBoolean("settings.default-pvp");
        defaultFood = config.getBoolean("settings.default-food");
        defaultXP = config.getBoolean("settings.default-xp");
    }
}
