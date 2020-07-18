package me.pljr.marriage.utils;

import com.sun.org.apache.bcel.internal.generic.DCMPG;
import me.pljr.marriage.Marriage;
import me.pljr.marriage.database.QueryManager;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.managers.ConfigManager;
import me.pljr.marriage.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MarryUtil {
    public static QueryManager query = Marriage.getQuery();
    public static ConfigManager config = Marriage.getConfigManager();

    public static void chat(Player player, String message){
        String playerName = player.getName();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
        String partnerName = playerManager.getPartner();
        Player partner = Bukkit.getPlayer(partnerName);
        if (partner != null && partner.isOnline()){
            partner.sendMessage("§4§l❤§c§l-Chat §a" + playerName + " §8» §e" + message);
        }
        player.sendMessage("§4§l❤§c§l-Chat §a" + playerName + " §8» §e" + message);
    }

    public static void marry(String player1, String player2){
        PlayerManager playerManager1 = PlayerUtil.getPlayerManager(player1);
        PlayerManager playerManager2 = PlayerUtil.getPlayerManager(player2);
        playerManager1.setPartner(player2);
        playerManager2.setPartner(player1);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§c§l❤ §bSvadba §8» §a" + player1 + " §fa §a" + player2 + " §fboli oddaní!");
        Bukkit.broadcastMessage("");
        PlayerUtil.setPlayerManager(player1, playerManager1);
        PlayerUtil.setPlayerManager(player2, playerManager2);
        PlayerUtil.savePlayer(player1);
        PlayerUtil.savePlayer(player2);
    }

    public static void setHome(String playerName, Location location){
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
        String partnerName = playerManager.getPartner();
        Player partner = Bukkit.getPlayer(partnerName);
        if (partner == null || !partner.isOnline()){
            PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
            partnerManager.setHome(location);
            PlayerUtil.setPlayerManager(partnerName, partnerManager);
            PlayerUtil.savePlayer(partnerName);
        }else{
            partner.sendMessage("§c§l❤ §aSvadba §8» §fTvoj/a partner/ka nastavil/a §bzdieľaný §fdomov.");
            PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
            partnerManager.setHome(location);
            PlayerUtil.setPlayerManager(partnerName, partnerManager);
        }
        playerManager.setHome(location);
        PlayerUtil.setPlayerManager(playerName, playerManager);
        PlayerUtil.savePlayer(playerName);
        PlayerUtil.savePlayer(partnerName);
    }

    public static void divorce(String player){
        PlayerManager playerManager = PlayerUtil.getPlayerManager(player);
        String partnerName = playerManager.getPartner();
        Player partner = Bukkit.getPlayer(partnerName);
        if (partner == null || !partner.isOnline()){
            PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
            partnerManager.setPartner(null);
            PlayerUtil.setPlayerManager(partnerName, partnerManager);
            PlayerUtil.savePlayer(partnerName);
        }else{
            partner.sendTitle("§cSvadba", "§c✖ Tvoj/a partner/ka §bzrušil/a §fmanželstvo!.", 10, 20*3, 10);
            partner.playSound(partner.getLocation(), Sound.ITEM_SHIELD_BREAK, 10, 1);
            PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
            partnerManager.setPartner(null);
            PlayerUtil.setPlayerManager(partnerName, partnerManager);
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§c§l❤ §cSvadba §8» §a" + player + " §fa §a" + partnerName + " §fboli rovedení!");
        Bukkit.broadcastMessage("");
        playerManager.setPartner(null);
        PlayerUtil.setPlayerManager(player, playerManager);
        PlayerUtil.savePlayer(player);
        PlayerUtil.savePlayer(partnerName);
    }

    public static void sendMarryList(Player player, int page){
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), () ->{
            LinkedHashMap<String, String> marryList = query.getMarryListSync();

            player.sendMessage("");
            player.sendMessage("§c§l❤ §aSvadba §8» §fZoznam párov:");
            player.sendMessage("");

            int start = page*7;
            int stop = start+7;
            int loop = 1;
            for (Map.Entry<String, String> entry : marryList.entrySet()){
            if (loop == stop) break;
            if (loop<start){
                loop++;
                continue;
            }
                PlayerManager player1mngr = PlayerUtil.getPlayerManager(entry.getKey());
                Gender gender1 = player1mngr.getGender();
                String player1name = "§7" + entry.getKey();
                switch (gender1){
                    case FEMALE:
                        player1name = "§d" + entry.getKey();
                        break;
                    case MALE:
                        player1name = "§b" + entry.getKey();
                }
                PlayerManager player2mngr = PlayerUtil.getPlayerManager(entry.getValue());
                Gender gender2 = player2mngr.getGender();
                String player2name = "§7" + entry.getValue();
                switch (gender2){
                    case FEMALE:
                        player2name = "§d" + entry.getValue();
                        break;
                    case MALE:
                        player2name = "§b" + entry.getValue();
                }
                player.sendMessage(player1name + " §7+ " + player2name);
            }
        });
    }

    public static void kiss(Player player1, Player player2){
        World world = player1.getWorld();
        if (config.getBoolean("particles")){
            world.spawnParticle(Particle.HEART, player1.getLocation().clone().add(0,1,0), 4, 0.3, 0.3, 0.3);
            world.spawnParticle(Particle.HEART, player2.getLocation().clone().add(0,1,0), 4, 0.3, 0.3, 0.3);
        }
        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l❤ §aSvadba §8» §b" + player2.getName() + " §fťa pobozkal/a."));
        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l❤ §aSvadba §8» §b" + player1.getName() + " §fťa pobozkal/a."));
    }
}
