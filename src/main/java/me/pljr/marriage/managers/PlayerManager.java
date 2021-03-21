package me.pljr.marriage.managers;

import lombok.AllArgsConstructor;
import me.pljr.marriage.Marriage;
import me.pljr.marriage.objects.MarriagePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@AllArgsConstructor
public class PlayerManager {
    private final static int AUTOSAVE = 12000;

    private final HashMap<UUID, MarriagePlayer> players = new HashMap<>();
    private final JavaPlugin plugin;
    private final QueryManager queryManager;
    private final boolean cachePlayers;

    public void getPlayer(UUID uuid, Consumer<MarriagePlayer> consumer){
        if (!players.containsKey(uuid)){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                MarriagePlayer player = queryManager.loadPlayer(uuid);
                setPlayer(uuid, player);
                consumer.accept(player);
            });
        }else{
            consumer.accept(players.get(uuid));
        }
    }

    public MarriagePlayer getPlayer(UUID uuid){
        if (!players.containsKey(uuid)){
            MarriagePlayer player = queryManager.loadPlayer(uuid);
            setPlayer(uuid, player);
            return player;
        }else{
            return players.get(uuid);
        }
    }

    public void setPlayer(UUID uuid, MarriagePlayer player){
        players.put(uuid, player);
    }

    public void savePlayer(UUID uuid){
        queryManager.savePlayer(players.get(uuid));
        if (!cachePlayers) players.remove(uuid);
    }

    public void initAutoSave(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Marriage.log.info("Saving players..");
            for (Map.Entry<UUID, MarriagePlayer> entry : players.entrySet()){
                savePlayer(entry.getKey());
            }
            Marriage.log.info("All players were saved.");
        }, AUTOSAVE, AUTOSAVE);
    }
}
