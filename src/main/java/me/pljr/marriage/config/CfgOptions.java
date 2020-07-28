package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.managers.ConfigManager;

public class CfgOptions {
    private final static ConfigManager config = Marriage.getConfigManager();

    public static boolean particles;
    public static boolean sounds;
    public static int costMarry;
    public static int costDivorce;
    public static int cooldown;
    public static boolean sharedFood;
    public static boolean sharedXp;
    public static boolean togglepvp;
    public static boolean bungee;

    public static void load(){
        CfgOptions.particles = config.getBoolean("options.particles");
        CfgOptions.sounds = config.getBoolean("options.sounds");
        CfgOptions.costMarry = config.getInt("options.cost.marry");
        CfgOptions.costDivorce = config.getInt("options.cost.divorce");
        CfgOptions.cooldown = config.getInt("options.cooldown");
        CfgOptions.sharedFood = config.getBoolean("options.shared.food");
        CfgOptions.sharedXp = config.getBoolean("options.shared.xp");
        CfgOptions.togglepvp = config.getBoolean("options.toggle-pvp");
        CfgOptions.bungee = config.getBoolean("options.bungee");
    }
}
