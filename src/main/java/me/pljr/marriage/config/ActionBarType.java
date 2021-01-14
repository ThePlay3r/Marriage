package me.pljr.marriage.config;

import me.pljr.pljrapispigot.managers.ConfigManager;
import me.pljr.pljrapispigot.objects.PLJRActionBar;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum ActionBarType {
    NO_PVP(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYou can't attack your partner.", 20)),
    KISS(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYou kissed your partner.", 20)),
    KISS_PARTNER(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYour partner has kissed you.", 20)),
    SHARE_FOOD(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYou shared half of your food. &7({amount})", 20)),
    SHARE_FOOD_PARTNER(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYour partner has shared food with you. &7({amount})", 20)),
    SHARE_XP(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYou shared half of your exp. &7({amount})", 20)),
    SHARE_XP_PARTNER(new PLJRActionBar("§c§l❤ §aMarriage §8» &fYour partner has shared exp with you. &7({amount})", 20));

    private static HashMap<ActionBarType, PLJRActionBar> actionBars;
    private final PLJRActionBar defaultValue;

    ActionBarType(PLJRActionBar defaultValue){
        this.defaultValue = defaultValue;
    }

    public static void load(ConfigManager config){
        actionBars = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (ActionBarType actionBarType : values()){
            if (!fileConfig.isSet(actionBarType.toString())){
                config.setPLJRActionBar(actionBarType.toString(), actionBarType.getDefault());
            }
            actionBars.put(actionBarType, config.getPLJRActionBar(actionBarType.toString()));
        }
        config.save();
    }

    public PLJRActionBar get(){
        return actionBars.get(this);
    }

    public PLJRActionBar getDefault(){
        return this.defaultValue;
    }
}
