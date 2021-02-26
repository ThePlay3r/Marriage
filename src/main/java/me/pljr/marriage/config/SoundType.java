package me.pljr.marriage.config;

import me.pljr.pljrapispigot.builders.SoundBuilder;
import me.pljr.pljrapispigot.managers.ConfigManager;
import me.pljr.pljrapispigot.objects.PLJRSound;
import me.pljr.pljrapispigot.xseries.XSound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum SoundType {
    DIVORCE(new SoundBuilder(XSound.ITEM_SHIELD_BREAK, 1, 1).create()),
    MARRY_ACCEPT(new SoundBuilder(XSound.ENTITY_PLAYER_LEVELUP, 1, 1).create()),
    NOTIFY(new SoundBuilder(XSound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1).create());

    private static HashMap<SoundType, PLJRSound> soundTypes;
    private final PLJRSound defaultValue;

    SoundType(PLJRSound defaultValue){
        this.defaultValue = defaultValue;
    }

    public static void load(ConfigManager config){
        soundTypes = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (SoundType soundType : values()){
            if (!fileConfig.isSet(soundType.toString())){
                config.setPLJRSound(soundType.toString(), new SoundBuilder(soundType.defaultValue).create());
            }else{
                soundTypes.put(soundType, config.getPLJRSound(soundType.toString()));
            }
        }
        config.save();
    }

    public PLJRSound get(){
        return soundTypes.getOrDefault(this, defaultValue);
    }}
