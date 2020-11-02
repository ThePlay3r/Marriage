package me.pljr.marriage.config;

import me.pljr.pljrapi.managers.ConfigManager;

public class CfgDefaulthome {
    public static String world;
    public static int x;
    public static int y;
    public static int z;
    public static int yaw;
    public static int pitch;

    public static void load(ConfigManager config){
        CfgDefaulthome.world = config.getString("defaulthome.world");
        CfgDefaulthome.x = config.getInt("defaulthome.x");
        CfgDefaulthome.y = config.getInt("defaulthome.y");
        CfgDefaulthome.z = config.getInt("defaulthome.z");
        CfgDefaulthome.yaw = config.getInt("defaulthome.yaw");
        CfgDefaulthome.pitch = config.getInt("defaulthome.pitch");
    }
}
