package me.pljr.marriage.config;

import me.pljr.marriage.enums.Sounds;
import me.pljr.pljrapi.managers.ConfigManager;
import org.bukkit.Sound;

import java.util.HashMap;

public class CfgSounds {
    public static HashMap<Sounds, Sound> sounds = new HashMap<>();

    public static void load(ConfigManager config){
        for (Sounds sound : Sounds.values()){
            sounds.put(sound, config.getSound("sounds." + sound.toString()));
        }
    }
}
