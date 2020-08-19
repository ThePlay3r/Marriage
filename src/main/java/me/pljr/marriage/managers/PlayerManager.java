package me.pljr.marriage.managers;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.CorePlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final HashMap<UUID, CorePlayer> players = new HashMap<>();
    private final QueryManager query = Marriage.getQuery();
    private final HashMap<UUID, UUID> requests = new HashMap<>();

    public CorePlayer getPlayerManager(UUID uuid){
        if (players.containsKey(uuid)){
            return players.get(uuid);
        }
        query.loadPlayerSync(uuid);
        return getPlayerManager(uuid);
    }

    public void setPlayerManager(UUID uuid, CorePlayer corePlayer){
        players.put(uuid, corePlayer);
    }

    public void savePlayer(UUID uuid){
        if (!players.containsKey(uuid)) return;
        query.savePlayer(uuid);
    }

    public HashMap<UUID, UUID> getRequests() {
        return requests;
    }
}
