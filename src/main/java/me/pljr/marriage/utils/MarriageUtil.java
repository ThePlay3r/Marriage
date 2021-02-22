package me.pljr.marriage.utils;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.config.Lang;
import me.pljr.marriage.exceptions.HasPartnerException;
import me.pljr.marriage.exceptions.NoHomeException;
import me.pljr.marriage.exceptions.NoPartnerException;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.exceptions.NoHeldItemException;
import me.pljr.pljrapispigot.exceptions.PlayerOfflineException;
import me.pljr.pljrapispigot.utils.BungeeUtil;
import me.pljr.pljrapispigot.utils.ChatUtil;
import me.pljr.pljrapispigot.utils.FormatUtil;
import me.pljr.pljrapispigot.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MarriageUtil {
    private final static PlayerManager playerManager = Marriage.getPlayerManager();

    /**
     * Tries to marry two players.
     *
     * @param marriagePlayer First player.
     * @param marriagePartner Second player.
     *
     * @throws HasPartnerException If one of the players already has a partner.
     */
    public static void marry(MarriagePlayer marriagePlayer, MarriagePlayer marriagePartner) throws HasPartnerException {
        UUID playerId = marriagePlayer.getUniqueId();
        UUID partnerId = marriagePartner.getUniqueId();
        if (marriagePlayer.getPartnerID() != null) throw new HasPartnerException(playerId);
        if (marriagePartner.getPartnerID() != null) throw new HasPartnerException(partnerId);

        marriagePartner.clearRequests();
        marriagePlayer.setPartnerID(partnerId);
        marriagePartner.clearRequests();
        marriagePartner.setPartnerID(playerId);
        playerManager.setPlayer(playerId, marriagePlayer);
        playerManager.setPlayer(partnerId, marriagePartner);
    }

    /**
     * Tries to divorce player.
     *
     * @param marriagePlayer Player who will get divorced.
     *
     * @throws NoPartnerException If player does not have a partner.
     */
    public static void divorce(MarriagePlayer marriagePlayer) throws NoPartnerException {
        UUID playerId = marriagePlayer.getUniqueId();
        UUID partnerId = marriagePlayer.getPartnerID();
        if (partnerId == null) throw new NoPartnerException(playerId);

        MarriagePlayer marriagePartner = playerManager.getPlayer(partnerId);

        marriagePlayer.setPartnerID(null);
        marriagePartner.setPartnerID(null);
        playerManager.setPlayer(playerId, marriagePlayer);
        playerManager.setPlayer(partnerId, marriagePartner);
    }

    /**
     * Tries to add a marrige request from one to another player.
     *
     * @param marriagePlayer The sender of the request.
     * @param marriageRequest The receiver of the request.
     *
     * @throws HasPartnerException If one of the players already has a partner.
     */
    public static void request(MarriagePlayer marriagePlayer, MarriagePlayer marriageRequest) throws HasPartnerException {
        UUID playerId = marriagePlayer.getUniqueId();
        UUID requestId = marriageRequest.getUniqueId();
        if (marriagePlayer.getPartnerID() != null) throw new HasPartnerException(playerId);
        if (marriageRequest.getPartnerID() != null) throw new HasPartnerException(requestId);
        marriageRequest.addRequest(playerId);
        playerManager.setPlayer(requestId, marriageRequest);
    }

    /**
     * Tries to send a message to player's partner. If partner or player is offline this will fail
     * silently.
     *
     * @param marriagePlayer Player, whos partner will receive the message.
     * @param message Message which will be send to both partner and player.
     * @param format Format of the message, available placeholders are {name} and {message}.
     *
     * @throws NoPartnerException If player does not have a partner.
     */
    public static void chat(MarriagePlayer marriagePlayer, String message, String format) throws NoPartnerException {
        if (marriagePlayer.getPartnerID() == null) throw new NoPartnerException(marriagePlayer.getUniqueId());

        String playerName = PlayerUtil.getName(marriagePlayer.getUniqueId());
        format = format
                .replace("{name}", playerName)
                .replace("{message}", message);
        if (CfgSettings.BUNGEE){
            String partnerName = PlayerUtil.getName(marriagePlayer.getPartnerID());
            BungeeUtil.message(playerName, format);
            BungeeUtil.message(partnerName, format);
        }else{
            Player player = Bukkit.getPlayer(marriagePlayer.getUniqueId());
            Player partner = Bukkit.getPlayer(marriagePlayer.getPartnerID());
            if (player != null) ChatUtil.sendMessage(player, format);
            if (partner != null) ChatUtil.sendMessage(partner, format);
        }
        Bukkit.getScheduler().runTaskAsynchronously(Marriage.getInstance(), ()->{
            for (Player player : Bukkit.getOnlinePlayers()){
                if (!player.hasPermission("marriage.admin.spy")) continue;
                MarriagePlayer marriageAdmin = playerManager.getPlayer(player);
                if (!marriageAdmin.isSpy()) continue;
                ChatUtil.sendMessage(player, Lang.CHAT_SPY.get()
                        .replace("{message}", message)
                        .replace("{name}", playerName));
            }
        });
    }

    /**
     * Tries to send an ItemStack from players hand to partners inventory. Fails silently,
     * if player is offline.
     *
     * @param marriagePlayer Player whos partner will receive the item.
     *
     * @throws NoPartnerException If player has no partner.
     * @throws PlayerOfflineException If partner is offline.
     * @throws NoHeldItemException If player is not holding any item.
     */
    public static void gift(MarriagePlayer marriagePlayer) throws NoPartnerException, PlayerOfflineException, NoHeldItemException {
        if (marriagePlayer.getPartnerID() == null) throw new NoPartnerException(marriagePlayer.getUniqueId());

        UUID partnerId = marriagePlayer.getPartnerID();
        if (!PlayerUtil.isPlayer(partnerId)) throw new PlayerOfflineException(partnerId);

        UUID playerId = marriagePlayer.getUniqueId();
        if (!PlayerUtil.isPlayer(playerId)) return;

        Player player = Bukkit.getPlayer(playerId);
        ItemStack giftItem = player.getItemInHand();
        if (giftItem == null) throw new NoHeldItemException(player);

        Player partner = Bukkit.getPlayer(partnerId);
        player.getInventory().remove(giftItem);
        player.updateInventory();
        PlayerUtil.give(partner, giftItem);
    }

    /**
     * Tries to teleport player to another player's marriage home.
     *
     * @param player Player who should be teleported.
     * @param marriagePlayer Owner of the home.
     *
     * @throws NoHomeException If the owner does not have set up a home.
     */
    public static void home(Player player, MarriagePlayer marriagePlayer) throws NoHomeException {
        if (marriagePlayer.getHome() == null) throw new NoHomeException(marriagePlayer.getUniqueId());
        PlayerUtil.teleport(player, marriagePlayer.getHome());
    }

    /**
     * Tries to set a new home location for marriage player.
     *
     * @param player Player whos location will be used for the new home.
     * @param marriagePlayer Marriage player who will have the new home set.
     *
     * @throws NoPartnerException If marriage player does not have a partner.
     */
    public static void setHome(Player player, MarriagePlayer marriagePlayer) throws NoPartnerException {
        UUID playerId = marriagePlayer.getUniqueId();
        UUID partnerId = marriagePlayer.getPartnerID();
        if (partnerId == null) throw new NoPartnerException(playerId);

        MarriagePlayer marriagePartner = playerManager.getPlayer(partnerId);

        Location homeLocation = player.getLocation();
        marriagePlayer.setHome(homeLocation);
        marriagePartner.setHome(homeLocation);

        playerManager.setPlayer(playerId, marriagePlayer);
        playerManager.setPlayer(partnerId, marriagePartner);
    }

    /**
     * Tries to get the last time, player's partner was online.
     *
     * @param marriagePlayer Marriage player whos partner's time will be sent.
     * @return The last tiime player's partner was on the server.
     *
     * @throws NoPartnerException If player does not have a partner.
     */
    public static long lastSeen(MarriagePlayer marriagePlayer) throws NoPartnerException {
        UUID partnerId = marriagePlayer.getPartnerID();
        if (partnerId == null) throw new NoPartnerException(marriagePlayer.getUniqueId());

        MarriagePlayer marriagePartner = playerManager.getPlayer(marriagePlayer.getPartnerID());

        return marriagePartner.getLastSeen();
    }

    /**
     * Tries to teleport a player to his partner. Fails silently if player is offline.
     *
     * @param marriagePlayer Player that should be teleported.
     * @throws NoPartnerException If the player does not have a partner.
     * @throws PlayerOfflineException If the partner is offline.
     */
    public static void teleport(MarriagePlayer marriagePlayer) throws NoPartnerException, PlayerOfflineException {
        UUID playerId = marriagePlayer.getUniqueId();
        UUID partnerId = marriagePlayer.getPartnerID();
        if (partnerId == null) throw new NoPartnerException(playerId);
        if (!PlayerUtil.isPlayer(partnerId)) throw new PlayerOfflineException(partnerId);
        if (!PlayerUtil.isPlayer(playerId)) return;

        Player player = Bukkit.getPlayer(playerId);
        Player partner = Bukkit.getPlayer(partnerId);

        PlayerUtil.teleport(player, partner);
    }
}
