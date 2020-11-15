package me.pljr.marriage.utils;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.QueryManager;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.managers.ActionBarManager;
import me.pljr.pljrapi.managers.ConfigManager;
import me.pljr.pljrapi.managers.TitleManager;
import me.pljr.pljrapi.objects.PLJRActionBar;
import me.pljr.pljrapi.objects.PLJRTitle;
import me.pljr.pljrapi.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class MarryUtil {
    public static QueryManager query = Marriage.getQuery();
    public static ConfigManager config = Marriage.getConfigManager();

    public static void chat(Player player, String message){
        final String msg = MiniMessageUtil.strip(message);
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();
        CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(playerId);
        OfflinePlayer partner = Bukkit.getOfflinePlayer(corePlayer.getPartner());
        String format = CfgLang.lang.get(Lang.CHAT_FORMAT).replace("%name", playerName).replace("%message", msg);
        if (CfgSettings.bungee){
            BungeeUtil.message(partner.getName(), format);
        }else{
            ChatUtil.sendMessage(partner, format);
        }
        ChatUtil.sendMessage(player, format);
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), ()->{
            for (Player p : Bukkit.getOnlinePlayers()){
                if (p.hasPermission("marriage.admin.spy")){
                    CorePlayer pManager = Marriage.getPlayerManager().getCorePlayer(p.getUniqueId());
                    if (pManager.isSpy()){
                        ChatUtil.sendMessage(p, CfgLang.lang.get(Lang.CHAT_FORMAT_SPY).replace("%name", playerName).replace("%message", msg));
                    }
                }
            }
        });
    }

    public static void marry(UUID player1, UUID player2){
        CorePlayer corePlayer1 = Marriage.getPlayerManager().getCorePlayer(player1);
        CorePlayer corePlayer2 = Marriage.getPlayerManager().getCorePlayer(player2);
        corePlayer1.setPartner(player2);
        corePlayer2.setPartner(player1);
        String player1Name = PlayerUtil.getName(Bukkit.getOfflinePlayer(player1));
        String player2Name = PlayerUtil.getName(Bukkit.getOfflinePlayer(player2));
        ChatUtil.broadcastClean("", "", CfgSettings.bungee);
        ChatUtil.broadcast(CfgLang.lang.get(Lang.MARRY_ACCEPT_BROADCAST).replace("%name1", player1Name).replace("%name2", player2Name), "", CfgSettings.bungee);
        ChatUtil.broadcastClean("", "", CfgSettings.bungee);
        Marriage.getPlayerManager().setCorePlayer(player1, corePlayer1);
        Marriage.getPlayerManager().setCorePlayer(player2, corePlayer2);
    }

    public static void setHome(UUID playerId, Location location, boolean notify){
        CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(playerId);
        UUID partnerId = corePlayer.getPartner();
        CorePlayer partnerManager = Marriage.getPlayerManager().getCorePlayer(partnerId);
        corePlayer.setHome(location);
        partnerManager.setHome(location);
        Marriage.getPlayerManager().setCorePlayer(playerId, corePlayer);
        Marriage.getPlayerManager().setCorePlayer(partnerId, partnerManager);
        OfflinePlayer partner = Bukkit.getOfflinePlayer(partnerId);
        if (partner.isOnline()) {
            Player onlinePartner = (Player) partner;
            if (notify) ChatUtil.sendMessage(onlinePartner, CfgLang.lang.get(Lang.SETHOME_PARTNER));
        }

    }

    public static void divorce(UUID playerId){
        CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(playerId);
        UUID partnerId = corePlayer.getPartner();
        OfflinePlayer partner = Bukkit.getOfflinePlayer(partnerId);
        String playerName = Bukkit.getOfflinePlayer(playerId).getName();
        String partnerName = PlayerUtil.getName(partner);
        CorePlayer partnerManager = Marriage.getPlayerManager().getCorePlayer(partnerId);
        corePlayer.setPartner(null);
        partnerManager.setPartner(null);
        Marriage.getPlayerManager().setCorePlayer(partnerId, corePlayer);
        Marriage.getPlayerManager().setCorePlayer(partnerId, partnerManager);
        if (partner.isOnline()){
            Player onlinePartner = (Player) partner;
            TitleManager.send(onlinePartner, new PLJRTitle(CfgLang.lang.get(Lang.DIVORCE_PARTNER_TITLE), CfgLang.lang.get(Lang.DIVORCE_PARTNER_SUBTITLE), 10, 20*3, 10));
            if (CfgSettings.particles)onlinePartner.playSound(onlinePartner.getLocation(), CfgSounds.sounds.get(Sounds.DIVORCE), 10, 1);
        }
        ChatUtil.broadcastClean("", "", CfgSettings.bungee);
        ChatUtil.broadcast(CfgLang.lang.get(Lang.DIVORCE_BROADCAST).replace("%name1", playerName).replace("%name2", partnerName), "", CfgSettings.bungee);
        ChatUtil.broadcastClean("", "", CfgSettings.bungee);
    }

    public static void sendMarryList(Player player, int page){
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), () ->{
            LinkedHashMap<UUID, UUID> marryList = query.getMarryListSync();

            ChatUtil.sendMessageClean(player, "");
            ChatUtil.sendMessage(player, CfgLang.lang.get(Lang.LIST_TITLE));
            ChatUtil.sendMessageClean(player, "");

            int start = page*7;
            int stop = start+7;
            int loop = 1;
            for (Map.Entry<UUID, UUID> entry : marryList.entrySet()){
                if (loop == stop) break;
                if (loop<start){
                    loop++;
                    continue;
                }
                CorePlayer player1mngr = Marriage.getPlayerManager().getCorePlayer(entry.getKey());
                Gender gender1 = player1mngr.getGender();
                String player1name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
                if (player1name == null){
                    player1name = "?";
                }
                switch (gender1){
                    case FEMALE:
                        player1name = CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR) + player1name;
                        break;
                    case MALE:
                        player1name = CfgLang.lang.get(Lang.GENDER_MALE_COLOR) + player1name;
                        break;
                    case NONE:
                        player1name = CfgLang.lang.get(Lang.GENDER_NONE_COLOR) + player1name;
                }
                CorePlayer player2mngr = Marriage.getPlayerManager().getCorePlayer(entry.getValue());
                Gender gender2 = player2mngr.getGender();
                String player2name = Bukkit.getOfflinePlayer(entry.getValue()).getName();
                if (player2name == null){
                    player2name = "?";
                }
                switch (gender2){
                    case FEMALE:
                        player2name = CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR) + player2name;
                        break;
                    case MALE:
                        player2name = CfgLang.lang.get(Lang.GENDER_MALE_COLOR) + player2name;
                        break;
                    case NONE:
                        player2name = CfgLang.lang.get(Lang.GENDER_NONE_COLOR) + player2name;
                }
                ChatUtil.sendMessageClean(player, player1name + " §7+ " + player2name);
            }
        });
    }

    public static void kiss(Player player1, Player player2){
        if (CfgSettings.particles){
            ParticleUtil.spawn("HEART", player1.getLocation().clone().add(0,1,0), 4, 0.3f, 0.3f, 0.3f);
            ParticleUtil.spawn("HEART", player2.getLocation().clone().add(0,1,0), 4, 0.3f, 0.3f, 0.3f);
        }
        ActionBarManager.send(player1, new PLJRActionBar(CfgLang.lang.get(Lang.KISS_PLAYER).replace("%name", player2.getName()), 20));
        ActionBarManager.send(player2, new PLJRActionBar(CfgLang.lang.get(Lang.KISS_PLAYER).replace("%name", player1.getName()), 20));
    }

    public static boolean isMarried(UUID uuid){
        CorePlayer corePlayer = Marriage.getPlayerManager().getCorePlayer(uuid);
        return (!(corePlayer.getPartner() == null));
    }
}
