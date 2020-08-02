package me.pljr.marriage.utils;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.managers.QueryManager;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.managers.ActionBarManager;
import me.pljr.pljrapi.managers.ConfigManager;
import me.pljr.pljrapi.managers.TitleManager;
import me.pljr.pljrapi.objects.PLJRActionBar;
import me.pljr.pljrapi.objects.PLJRTitle;
import me.pljr.pljrapi.utils.ChatUtil;
import me.pljr.pljrapi.utils.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class MarryUtil {
    public static QueryManager query = Marriage.getQuery();
    public static ConfigManager config = Marriage.getConfigManager();

    public static void chat(Player player, String message){
        String playerName = player.getName();
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerName);
        String partnerName = corePlayer.getPartner();
        String format = CfgLang.lang.get(Lang.CHAT_FORMAT).replace("%name", playerName).replace("%message", message);
        ChatUtil.message(partnerName, format);
        player.sendMessage(format);
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.hasPermission("marriage.admin.spy")){
                CorePlayer pManager = PlayerManager.getPlayerManager(p.getName());
                if (pManager.isSpy()){
                    player.sendMessage(CfgLang.lang.get(Lang.CHAT_FORMAT_SPY).replace("%name", playerName).replace("%message", message));
                }
            }
        }
    }

    public static void marry(String player1, String player2){
        CorePlayer corePlayer1 = PlayerManager.getPlayerManager(player1);
        CorePlayer corePlayer2 = PlayerManager.getPlayerManager(player2);
        corePlayer1.setPartner(player2);
        corePlayer2.setPartner(player1);
        ChatUtil.broadcast("");
        ChatUtil.broadcast(CfgLang.lang.get(Lang.MARRY_ACCEPT_BROADCAST).replace("%name1", player1).replace("%name2", player2));
        ChatUtil.broadcast("");
        PlayerManager.setPlayerManager(player1, corePlayer1);
        PlayerManager.setPlayerManager(player2, corePlayer2);
        PlayerManager.savePlayer(player1);
        PlayerManager.savePlayer(player2);
    }

    public static void setHome(String playerName, Location location, boolean notify){
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerName);
        String partnerName = corePlayer.getPartner();
        Player partner = Bukkit.getPlayer(partnerName);
        if (partner == null || !partner.isOnline()){
            CorePlayer partnerManager = PlayerManager.getPlayerManager(partnerName);
            partnerManager.setHome(location);
            PlayerManager.setPlayerManager(partnerName, partnerManager);
            PlayerManager.savePlayer(partnerName);
        }else{
            if (notify) partner.sendMessage(CfgLang.lang.get(Lang.SETHOME_PARTNER));
            CorePlayer partnerManager = PlayerManager.getPlayerManager(partnerName);
            partnerManager.setHome(location);
            PlayerManager.setPlayerManager(partnerName, partnerManager);
        }
        corePlayer.setHome(location);
        PlayerManager.setPlayerManager(playerName, corePlayer);
        PlayerManager.savePlayer(playerName);
        PlayerManager.savePlayer(partnerName);
    }

    public static void divorce(String player){
        CorePlayer corePlayer = PlayerManager.getPlayerManager(player);
        String partnerName = corePlayer.getPartner();
        Player partner = Bukkit.getPlayer(partnerName);
        if (partner == null || !partner.isOnline()){
            CorePlayer partnerManager = PlayerManager.getPlayerManager(partnerName);
            partnerManager.setPartner(null);
            PlayerManager.setPlayerManager(partnerName, partnerManager);
            PlayerManager.savePlayer(partnerName);
        }else{
            TitleManager.send(partner, new PLJRTitle(CfgLang.lang.get(Lang.DIVORCE_PARTNER_TITLE), CfgLang.lang.get(Lang.DIVORCE_PARTNER_SUBTITLE), 10, 20*3, 10));
            if (CfgSettings.particles)partner.playSound(partner.getLocation(), CfgSounds.sounds.get(Sounds.DIVORCE), 10, 1);
            CorePlayer partnerManager = PlayerManager.getPlayerManager(partnerName);
            partnerManager.setPartner(null);
            PlayerManager.setPlayerManager(partnerName, partnerManager);
        }
        ChatUtil.broadcast("");
        ChatUtil.broadcast(CfgLang.lang.get(Lang.DIVORCE_BROADCAST).replace("%name1", player).replace("%name2", partnerName));
        ChatUtil.broadcast("");
        corePlayer.setPartner(null);
        PlayerManager.setPlayerManager(player, corePlayer);
        PlayerManager.savePlayer(player);
        PlayerManager.savePlayer(partnerName);
    }

    public static void sendMarryList(Player player, int page){
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), () ->{
            LinkedHashMap<String, String> marryList = query.getMarryListSync();

            player.sendMessage("");
            player.sendMessage(CfgLang.lang.get(Lang.LIST_TITLE));
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
                CorePlayer player1mngr = PlayerManager.getPlayerManager(entry.getKey());
                Gender gender1 = player1mngr.getGender();
                String player1name = CfgLang.lang.get(Lang.GENDER_NONE_COLOR) + entry.getKey();
                switch (gender1){
                    case FEMALE:
                        player1name = CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR) + entry.getKey();
                        break;
                    case MALE:
                        player1name = CfgLang.lang.get(Lang.GENDER_MALE_COLOR) + entry.getKey();
                }
                CorePlayer player2mngr = PlayerManager.getPlayerManager(entry.getValue());
                Gender gender2 = player2mngr.getGender();
                String player2name = CfgLang.lang.get(Lang.GENDER_NONE_COLOR) + entry.getValue();
                switch (gender2){
                    case FEMALE:
                        player2name = CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR) + entry.getValue();
                        break;
                    case MALE:
                        player2name = CfgLang.lang.get(Lang.GENDER_MALE_COLOR) + entry.getValue();
                }
                player.sendMessage(player1name + " §7+ " + player2name);
            }
        });
    }

    public static void kiss(Player player1, Player player2){
        if (CfgSettings.particles){
            ParticleUtil.spawn(Particle.HEART, player1.getLocation().clone().add(0,1,0), 4, 0.3f, 0.3f, 0.3f);
            ParticleUtil.spawn(Particle.HEART, player2.getLocation().clone().add(0,1,0), 4, 0.3f, 0.3f, 0.3f);
        }
        ActionBarManager.send(player1, new PLJRActionBar(CfgLang.lang.get(Lang.KISS_PLAYER).replace("%name", player2.getName()), 20));
        ActionBarManager.send(player2, new PLJRActionBar(CfgLang.lang.get(Lang.KISS_PLAYER).replace("%name", player1.getName()), 20));
    }

    public static boolean isMarried(String player){
        CorePlayer corePlayer = PlayerManager.getPlayerManager(player);
        return (!(corePlayer.getPartner() == null));
    }
}
