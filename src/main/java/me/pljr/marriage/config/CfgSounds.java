package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.ConfigManager;
import org.bukkit.Sound;

import java.util.HashMap;

public class CfgSounds {
    private final static ConfigManager config = Marriage.getConfigManager();

    public static HashMap<Sounds, Sound> sounds = new HashMap<>();

    public static void load(){
        for (Sounds sound : Sounds.values()){
            sounds.put(sound, config.getSound("sounds." + sound.toString()));
        }
    }
}
