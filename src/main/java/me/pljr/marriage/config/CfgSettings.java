package me.pljr.marriage.config;

import lombok.Getter;
import me.pljr.pljrapispigot.managers.ConfigManager;

@Getter
public class CfgSettings {
    private final boolean bungee;
    private final boolean particles;
    private final boolean sounds;
    private final boolean toggleXP;
    private final boolean togglePVP;
    private final boolean toggleFood;
    private final boolean menu;
    private final int costMarry;
    private final int costDivorce;

    public CfgSettings(ConfigManager config){
        bungee = config.getBoolean("settings.bungee");
        particles = config.getBoolean("settings.particles");
        sounds = config.getBoolean("settings.sounds");
        toggleXP = config.getBoolean("settings.toggle-xp");
        togglePVP = config.getBoolean("settings.toggle-pvp");
        toggleFood = config.getBoolean("settings.toggle-food");
        menu = config.getBoolean("settings.menu");
        costMarry = config.getInt("settings.cost-marry");
        costDivorce = config.getInt("settings.cost-divorce");
    }
}
