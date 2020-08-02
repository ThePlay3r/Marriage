package me.pljr.marriage.managers;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;

import java.util.HashMap;

public class PlayerManager {
    private static final HashMap<String, CorePlayer> players = new HashMap<>();
    private static final QueryManager query = Marriage.getQuery();
    private static final HashMap<String, String> requests = new HashMap<>();

    public static CorePlayer getPlayerManager(String pName){
        if (players.containsKey(pName)){
            return players.get(pName);
        }
        query.loadPlayerSync(pName);
        return getPlayerManager(pName);
    }

    public static void setPlayerManager(String pName, CorePlayer corePlayer){
        players.put(pName, corePlayer);
    }

    public static void savePlayer(String pName){
        if (!players.containsKey(pName)) return;
        query.savePlayer(pName);
    }

    public static HashMap<String, String> getRequests() {
        return requests;
    }
}
