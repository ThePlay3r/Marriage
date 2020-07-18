package me.pljr.marriage.utils;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.database.QueryManager;
import me.pljr.marriage.managers.PlayerManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerUtil {
    private static HashMap<String, PlayerManager> players = new HashMap<>();
    private static List<String> initialized = new ArrayList<>();
    private static final QueryManager query = Marriage.getQuery();
    private static HashMap<String, String> requests = new HashMap<>();

    public static List<String> getInitialized(){
        return initialized;
    }

    public static void setInitialized(List<String> initialized){
        PlayerUtil.initialized = initialized;
    }

    public static PlayerManager getPlayerManager(String pName){
        if (players.containsKey(pName)){
            return players.get(pName);
        }
        query.loadPlayerSync(pName);
        return getPlayerManager(pName);
    }

    public static void setPlayerManager(String pName, PlayerManager playerManager){
        players.put(pName, playerManager);
    }

    public static void savePlayer(String pName){
        if (!players.containsKey(pName)) return;
        query.savePlayer(pName);
    }

    public static HashMap<String, String> getRequests() {
        return requests;
    }

    public static void setRequests(HashMap<String, String> requests) {
        PlayerUtil.requests = requests;
    }
}
