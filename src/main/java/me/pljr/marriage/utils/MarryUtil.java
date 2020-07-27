package me.pljr.marriage.utils;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.config.CfgOptions;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.database.QueryManager;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.ConfigManager;
import me.pljr.marriage.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
            partner.sendMessage(CfgMessages.messages.get(Message.CHAT_FORMAT).replace("%name", playerName).replace("%message", message));
        }
        player.sendMessage(CfgMessages.messages.get(Message.CHAT_FORMAT).replace("%name", playerName).replace("%message", message));
    }

    public static void marry(String player1, String player2){
        PlayerManager playerManager1 = PlayerUtil.getPlayerManager(player1);
        PlayerManager playerManager2 = PlayerUtil.getPlayerManager(player2);
        playerManager1.setPartner(player2);
        playerManager2.setPartner(player1);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CfgMessages.messages.get(Message.MARRY_ACCEPT_BROADCAST).replace("%name1", player1).replace("%name2", player2));
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
            partner.sendMessage(CfgMessages.messages.get(Message.SETHOME_PARTNER));
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
            partner.sendTitle(CfgMessages.messages.get(Message.DIVORCE_PARTNER_TITLE), CfgMessages.messages.get(Message.DIVORCE_PARTNER_SUBTITLE), 10, 20*3, 10);
            if (CfgOptions.particles)partner.playSound(partner.getLocation(), CfgSounds.sounds.get(Sounds.DIVORCE), 10, 1);
            PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
            partnerManager.setPartner(null);
            PlayerUtil.setPlayerManager(partnerName, partnerManager);
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CfgMessages.messages.get(Message.DIVORCE_BROADCAST).replace("%name1", player).replace("%name2", partnerName));
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
            player.sendMessage(CfgMessages.messages.get(Message.LIST_TITLE));
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
                String player1name = CfgMessages.messages.get(Message.GENDER_NONE_COLOR) + entry.getKey();
                switch (gender1){
                    case FEMALE:
                        player1name = CfgMessages.messages.get(Message.GENDER_FEMALE_COLOR) + entry.getKey();
                        break;
                    case MALE:
                        player1name = CfgMessages.messages.get(Message.GENDER_MALE_COLOR) + entry.getKey();
                }
                PlayerManager player2mngr = PlayerUtil.getPlayerManager(entry.getValue());
                Gender gender2 = player2mngr.getGender();
                String player2name = CfgMessages.messages.get(Message.GENDER_NONE_COLOR) + entry.getValue();
                switch (gender2){
                    case FEMALE:
                        player2name = CfgMessages.messages.get(Message.GENDER_FEMALE_COLOR) + entry.getValue();
                        break;
                    case MALE:
                        player2name = CfgMessages.messages.get(Message.GENDER_MALE_COLOR) + entry.getValue();
                }
                player.sendMessage(player1name + " §7+ " + player2name);
            }
        });
    }

    public static void kiss(Player player1, Player player2){
        World world = player1.getWorld();
        if (CfgOptions.particles){
            world.spawnParticle(Particle.HEART, player1.getLocation().clone().add(0,1,0), 4, 0.3, 0.3, 0.3);
            world.spawnParticle(Particle.HEART, player2.getLocation().clone().add(0,1,0), 4, 0.3, 0.3, 0.3);
        }
        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(CfgMessages.messages.get(Message.KISS_PLAYER).replace("%name", player2.getName())));
        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(CfgMessages.messages.get(Message.KISS_PARTNER).replace("%name", player1.getName())));
    }
}
