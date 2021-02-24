package me.pljr.marriage.config;

import lombok.Getter;
import me.pljr.pljrapispigot.managers.ConfigManager;

public class CfgSettings {
    @Getter
    private static boolean BUNGEE = false;
    @Getter
    private static boolean PARTICLES = true;
    @Getter
    private static boolean SOUNDS = true;
    @Getter
    private static boolean TOGGLE_XP = true;
    @Getter
    private static boolean TOGGLE_PVP = true;
    @Getter
    private static boolean TOGGLE_FOOD = true;
    @Getter
    private static boolean MENU = true;
    @Getter
    private static int COST_MARRY = 7500;
    @Getter
    private static int COST_DIVORCE = 5000;

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
