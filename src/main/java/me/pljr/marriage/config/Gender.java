package me.pljr.marriage.config;

import me.pljr.pljrapispigot.managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum Gender {
    MALE("Male", "♂", "&b"),
    FEMALE("Female", "♀", "&d"),
    NONE("None", "●", "&7");

    private static HashMap<Gender, String> names;
    private static HashMap<Gender, String> symbols;
    private static HashMap<Gender, String> colors;
    private final String defaultName;
    private final String defaultSymbol;
    private final String defaultColor;

    Gender(String defaultName, String defaultSymbol, String defaultColor){
        this.defaultName = defaultName;
        this.defaultSymbol = defaultSymbol;
        this.defaultColor = defaultColor;
    }

    public static void load(ConfigManager config){
        names = new HashMap<>();
        symbols = new HashMap<>();
        colors = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (Gender gender : values()){
            if (!fileConfig.isSet(gender.toString())){
                fileConfig.set(gender.toString()+".name", gender.getDefaultName());
                fileConfig.set(gender.toString()+".symbol", gender.getDefaultSymbol());
                fileConfig.set(gender.toString()+".color", gender.getDefaultColor());
            }
            names.put(gender, config.getString(gender.toString()+".name"));
            symbols.put(gender, config.getString(gender.toString()+".symbol"));
            colors.put(gender, config.getString(gender.toString()+".color"));
        }
        config.save();
    }

    public String getName(){
        return names.get(this);
    }

    public String getSymbol(){
        return symbols.get(this);
    }

    public String getColor(){
        return colors.get(this);
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    public String getDefaultColor() {
        return defaultColor;
    }
}
