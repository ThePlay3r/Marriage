package me.pljr.marriage.config;

import lombok.Getter;
import me.pljr.pljrapispigot.managers.ConfigManager;

@Getter
public class Settings {
    private final static String PATH = "settings";

    private final boolean bungee;
    private final boolean particles;
    private final boolean sounds;
    private final boolean toggleXP;
    private final boolean togglePVP;
    private final boolean toggleFood;
    private final boolean menu;
    private final int costMarry;
    private final int costDivorce;
    private final boolean cachePlayers;

    public Settings(ConfigManager config){
        bungee = config.getBoolean(PATH+".bungee");
        particles = config.getBoolean(PATH+".particles");
        sounds = config.getBoolean(PATH+".sounds");
        toggleXP = config.getBoolean(PATH+".toggle-xp");
        togglePVP = config.getBoolean(PATH+".toggle-pvp");
        toggleFood = config.getBoolean(PATH+".toggle-food");
        menu = config.getBoolean(PATH+".menu");
        costMarry = config.getInt(PATH+".cost-marry");
        costDivorce = config.getInt(PATH+".cost-divorce");
        cachePlayers = config.getBoolean(PATH+".cache-players");
    }
}
