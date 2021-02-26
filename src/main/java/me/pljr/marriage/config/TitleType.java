package me.pljr.marriage.config;

import me.pljr.pljrapispigot.managers.ConfigManager;
import me.pljr.pljrapispigot.objects.PLJRTitle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum TitleType {
    DIVORCE_PLAYER(new PLJRTitle("&cDivorce", "&c✖ &fDivorced.", 20, 40, 20)),
    DIVORCE_PARTNER(new PLJRTitle("&cDivorce", "&c✖ &fDivorced.", 20, 40, 20)),
    MARRY_PLAYER(new PLJRTitle("&c&l❤ &aMarriage &c&l❤", "&fYou &baccepted &fa marry request from &b{name}&f.", 20, 40, 20)),
    MARRY_PARTNER(new PLJRTitle("&c&l❤ &aMarriage &c&l❤", "&b{name} &faccepted your marry request!", 20, 40, 20));

    private static HashMap<TitleType, PLJRTitle> titleTypes;
    private final PLJRTitle defaultValue;

    TitleType(PLJRTitle defaultValue){
        this.defaultValue = defaultValue;
    }

    public static void load(ConfigManager config){
        titleTypes = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (TitleType titleType : values()){
            if (!fileConfig.isSet(titleType.toString())){
                config.setPLJRTitle(titleType.toString(), titleType.defaultValue);
            }else{
                titleTypes.put(titleType, config.getPLJRTitle(titleType.toString()));
            }
        }
        config.save();
    }

    public PLJRTitle get(){
        return titleTypes.getOrDefault(this, defaultValue);
    }
}
