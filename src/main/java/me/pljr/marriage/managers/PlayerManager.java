package me.pljr.marriage.managers;

import me.pljr.marriage.objects.MarriagePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final HashMap<UUID, MarriagePlayer> players;
    private final QueryManager query;

    public PlayerManager(QueryManager query){
        this.players = new HashMap<>();
        this.query = query;
    }

    public MarriagePlayer getPlayer(Player player){
        return getPlayer(player.getUniqueId());
    }

    public MarriagePlayer getPlayer(UUID uuid){
        if (!players.containsKey(uuid)){
            setPlayer(uuid, query.loadPlayer(uuid));
        }
        return players.get(uuid);
    }

    public void setPlayer(Player player, MarriagePlayer marriagePlayer){
        setPlayer(player.getUniqueId(), marriagePlayer);
    }

    public void setPlayer(UUID uuid, MarriagePlayer marriagePlayer){
        this.players.put(uuid, marriagePlayer);
        savePlayer(uuid);
    }

    public void savePlayer(UUID uuid){
        query.savePlayerAsync(getPlayer(uuid));
    }
}
