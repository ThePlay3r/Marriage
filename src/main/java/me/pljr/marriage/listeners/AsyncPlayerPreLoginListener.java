package me.pljr.marriage.listeners;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.database.QueryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {
    private final QueryManager query = Marriage.getQuery();

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event){
        query.loadPlayerSync(event.getName());
    }
}
