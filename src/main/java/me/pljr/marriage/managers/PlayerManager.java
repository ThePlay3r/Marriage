package me.pljr.marriage.managers;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private static final HashMap<UUID, CorePlayer> players = new HashMap<>();
    private static final QueryManager query = Marriage.getQuery();
    private static final HashMap<UUID, UUID> requests = new HashMap<>();

    public static CorePlayer getPlayerManager(UUID uuid){
        if (players.containsKey(uuid)){
            return players.get(uuid);
        }
        query.loadPlayerSync(uuid);
        return getPlayerManager(uuid);
    }

    public static void setPlayerManager(UUID uuid, CorePlayer corePlayer){
        players.put(uuid, corePlayer);
    }

    public static void savePlayer(UUID uuid){
        if (!players.containsKey(uuid)) return;
        query.savePlayer(uuid);
    }

    public static HashMap<UUID, UUID> getRequests() {
        return requests;
    }
}
