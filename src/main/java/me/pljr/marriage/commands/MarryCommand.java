package me.pljr.marriage.commands;

import me.pljr.marriage.config.*;
import me.pljr.marriage.exceptions.HasPartnerException;
import me.pljr.marriage.exceptions.NoHomeException;
import me.pljr.marriage.exceptions.NoPartnerException;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.menus.MarryMenu;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.marriage.utils.MarriageUtil;
import me.pljr.pljrapispigot.builders.TitleBuilder;
import me.pljr.pljrapispigot.exceptions.NoHeldItemException;
import me.pljr.pljrapispigot.exceptions.PlayerOfflineException;
import me.pljr.pljrapispigot.utils.ChatUtil;
import me.pljr.pljrapispigot.utils.CommandUtil;
import me.pljr.pljrapispigot.utils.FormatUtil;
import me.pljr.pljrapispigot.utils.PlayerUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MarryCommand extends CommandUtil {
    private final Plugin plugin;
    private final PlayerManager playerManager;

    public MarryCommand(Plugin plugin, PlayerManager playerManager) {
        super("marry", "marriage.use");
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        // /marry
        if (args.length == 0){
            if (CfgSettings.MENU){
                new MarryMenu(player);
            }else{
                sendMessage(player, Lang.HELP.get());
            }
            return;
        }

        MarriagePlayer marriagePlayer = playerManager.getPlayer(player);
        String arg = args[0].toUpperCase();

        if (args.length == 1){
            switch (arg) {
                case "LIST":
                    if (!checkPerm(player, "marriage.list")) return;
                    sendMessage(player, Lang.LIST_HEADER.get());
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
                       for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
                           MarriagePlayer marriageOnlinePlayer = playerManager.getPlayer(onlinePlayer);
                           UUID partnerId = marriageOnlinePlayer.getPartnerID();
                           if (partnerId == null) continue;
                           String onlinePlayerName = onlinePlayer.getName();
                           String onlinePlayerPartnerName = PlayerUtil.getName(partnerId);
                           MarriagePlayer marriageOnlinePlayerPartner = playerManager.getPlayer(partnerId);
                           sendMessage(player, Lang.LIST_FORMAT.get()
                                   .replace("{partner}", marriageOnlinePlayerPartner.getGender().getColor() + onlinePlayerPartnerName)
                                   .replace("{player}", marriageOnlinePlayer.getGender().getColor() + onlinePlayerName));
                       }
                    });
                    break;
                case "HELP":
                    if (!checkPerm(player, "marriage.help")) return;
                    sendMessage(player, Lang.HELP.get());
                    break;
                case "GIVE":
                    if (!checkPerm(player, "marriage.give")) return;
                    try {
                        MarriageUtil.gift(marriagePlayer);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER.get());
                        return;
                    } catch (NoHeldItemException e) {
                        sendMessage(player, Lang.NO_HELD_ITEM.get());
                        return;
                    } catch (PlayerOfflineException e) {
                        sendMessage(player, Lang.PARTNER_OFFLINE.get());
                        return;
                    }
                    Player givePartner = Bukkit.getPlayer(marriagePlayer.getPartnerID());
                    sendMessage(player, Lang.GIFT.get());
                    sendMessage(givePartner, Lang.GIFT_PARTNER.get());
                    break;
                case "HOME":
                    if (!checkPerm(player, "marriage.home")) return;
                    try {
                        MarriageUtil.home(player, marriagePlayer);
                    } catch (NoHomeException e) {
                        sendMessage(player, Lang.NO_HOME.get());
                        return;
                    }
                    sendMessage(player, Lang.TP_HOME.get());
                    break;
                case "SETHOME":
                    if (!checkPerm(player, "marriage.sethome")) return;
                    try {
                        MarriageUtil.setHome(player, marriagePlayer);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER.get());
                        return;
                    }
                    UUID homePartnerId = marriagePlayer.getPartnerID();
                    if (PlayerUtil.isPlayer(homePartnerId)) {
                        Player homePartner = Bukkit.getPlayer(homePartnerId);
                        sendMessage(homePartner, Lang.SET_HOME_PARTNER.get());
                    }
                    sendMessage(player, Lang.SET_HOME.get());
                    break;
                case "SEEN":
                    if (!checkPerm(player, "marriage.seen")) return;
                    try {
                        sendMessage(player, Lang.LAST_SEEN.get()
                                .replace("{time}", FormatUtil.formatTime(MarriageUtil.lastSeen(marriagePlayer))));
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER.get());
                    }
                    break;
                case "PVP":
                    if (!checkPerm(player, "marriage.pvp")) return;
                    marriagePlayer.setPvp(!marriagePlayer.isPvp());
                    if (marriagePlayer.isPvp()) {
                        sendMessage(player, Lang.PVP_TOGGLE.get().replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.PVP_TOGGLE.get().replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                case "TP":
                    if (!checkPerm(player, "marriage.tp")) return;
                    try {
                        MarriageUtil.teleport(marriagePlayer);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER.get());
                        return;
                    } catch (PlayerOfflineException e) {
                        sendMessage(player, Lang.PARTNER_OFFLINE.get());
                        return;
                    }
                    Player tpPartner = Bukkit.getPlayer(marriagePlayer.getUniqueId());
                    sendMessage(player, Lang.TP.get().replace("{name}", tpPartner.getName()));
                    sendMessage(tpPartner, Lang.TP_PARTNER.get().replace("{name}", player.getName()));
                    break;
                case "DIVORCE":
                    if (!checkPerm(player, "marriage.divorce")) return;
                    if (!checkBalance(player, CfgSettings.COST_DIVORCE)) return;
                    UUID divorcePartnerId = marriagePlayer.getPartnerID();
                    try {
                        MarriageUtil.divorce(marriagePlayer);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER.get());
                        return;
                    }
                    Player divorcePartner = Bukkit.getPlayer(divorcePartnerId);
                    ChatUtil.broadcast(Lang.UNMARRY_BROADCAST.get()
                            .replace("{partner}", divorcePartner.getName())
                            .replace("{player}", player.getName()), "", CfgSettings.BUNGEE);
                    if (divorcePartnerId != null) {
                        SoundType.DIVORCE.get().play(divorcePartner);
                        TitleType.DIVORCE_PARTNER.get().send(divorcePartner);
                    }
                    SoundType.DIVORCE.get().play(player);
                    TitleType.DIVORCE_PLAYER.get().send(player);
                    break;
                case "FOOD":
                    if (!checkPerm(player, "marriage.food")) return;
                    if (!CfgSettings.TOGGLE_FOOD){
                        sendMessage(player, Lang.DISABLED.get());
                        return;
                    }
                    marriagePlayer.setSharedFood(!marriagePlayer.isSharedFood());
                    if (marriagePlayer.isSharedFood()) {
                        sendMessage(player, Lang.FOOD_TOGGLE.get().replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.FOOD_TOGGLE.get().replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                case "XP":
                    if (!checkPerm(player, "marriage.xp")) return;
                    if (!CfgSettings.TOGGLE_XP){
                        sendMessage(player, Lang.DISABLED.get());
                        return;
                    }
                    marriagePlayer.setSharedXP(!marriagePlayer.isSharedXP());
                    if (marriagePlayer.isSharedXP()) {
                        sendMessage(player, Lang.XP_TOGGLE.get().replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.XP_TOGGLE.get().replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                default:
                    if (!checkPlayer(player, args[0])) return;
                    if (!checkBalance(player, CfgSettings.COST_MARRY)) return;
                    String playerName = player.getName();

                    Player marryPartner = Bukkit.getPlayer(args[0]);
                    String marryPartnerName = marryPartner.getName();
                    UUID marryPartnerId = marryPartner.getUniqueId();
                    MarriagePlayer marriagePartner = playerManager.getPlayer(marryPartnerId);
                    if (marriagePlayer.getRequests().contains(marryPartnerId)) {
                        // Accepting a request
                        try {
                            MarriageUtil.marry(marriagePlayer, marriagePartner);
                        } catch (HasPartnerException e) {
                            sendMessage(player, Lang.HAS_PARTNER.get().replace("{name}", marryPartnerName));
                            return;
                        }
                        new TitleBuilder(TitleType.MARRY_PLAYER.get())
                                .replaceSubtitle("{name}", marryPartnerName)
                                .create().send(player);
                        new TitleBuilder(TitleType.MARRY_PARTNER.get())
                                .replaceSubtitle("{name}", playerName)
                                .create().send(marryPartner);
                        SoundType.MARRY_ACCEPT.get().play(player);
                        SoundType.MARRY_ACCEPT.get().play(marryPartner);
                        ChatUtil.broadcast(Lang.MARRY_BROADCAST.get()
                                        .replace("{partner}", marryPartnerName)
                                        .replace("{player}", playerName),
                                "", CfgSettings.BUNGEE);
                    } else {
                        // Sending a request
                        try {
                            MarriageUtil.request(marriagePlayer, marriagePartner);
                        } catch (HasPartnerException e) {
                            if (player.getUniqueId().equals(e.getSource())) {
                                sendMessage(player, Lang.HAVE_PARTNER.get());
                                return;
                            } else if (marryPartnerId.equals(e.getSource())) {
                                sendMessage(player, Lang.HAS_PARTNER.get().replace("{name}", marryPartnerName));
                                return;
                            }
                        }
                        sendMessage(player, Lang.MARRY_REQUEST.get().replace("{name}", marryPartnerName));
                        sendMessage(marryPartner, Lang.MARRY_REQUEST_PARTNER.get().replace("{name}", playerName));
                        SoundType.NOTIFY.get().play(marryPartner);
                    }
                    break;
            }
        }

        else if (args.length == 2){
            if (arg.equals("C")) {
                if (!checkPerm(player, "marriage.chat")) return;
                try {
                    MarriageUtil.chat(marriagePlayer,
                            StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " "),
                            Lang.CHAT_FORMAT.get());
                } catch (NoPartnerException e) {
                    sendMessage(player, Lang.NO_PARTNER.get());
                }
            } else if (arg.equals("GENDER")){
                if (!checkPerm(player, "marriage.gender")) return;
                for (Gender gender : Gender.values()){
                    if (args[1].equalsIgnoreCase(gender.getName())){
                        marriagePlayer.setGender(gender);
                        sendMessage(player, Lang.GENDER_CHANGE.get().replace("{gender}", gender.getColor() + gender.getName()));
                        playerManager.setPlayer(player, marriagePlayer);
                        return;
                    }
                }
                sendMessage(player, Lang.GENDER_CHANGE_ERROR.get().replace("{gender}", args[1]));
            }
        }

        else sendMessage(player, Lang.HELP.get());
    }
}
