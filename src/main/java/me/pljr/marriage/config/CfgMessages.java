package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.managers.ConfigManager;

import java.util.HashMap;
import java.util.List;

public class CfgMessages {
    private final static ConfigManager config = Marriage.getConfigManager();

    public static List<String> help;
    public static List<String> adminHelp;
    public static HashMap<Message, String> messages = new HashMap<>();

    public static void load(){
        CfgMessages.help = config.getStringList("help");
        CfgMessages.adminHelp = config.getStringList("admin-help");
        for (Message message : Message.values()){
            messages.put(message, config.getString("messages." + message.toString()));
        }
    }
}
