package me.pljr.marriage.config;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.managers.ConfigManager;

import java.util.HashMap;

public class CfgMessages {
    private final static ConfigManager config = Marriage.getConfigManager();

    public static HashMap<Message, String> messages = new HashMap<>();

    public static void load(){
        for (Message message : Message.values()){
            messages.put(message, config.getString("messages." + message.toString()));
        }
    }
}
