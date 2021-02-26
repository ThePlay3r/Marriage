package me.pljr.marriage.commands;

import lombok.AllArgsConstructor;
import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.config.Lang;
import me.pljr.marriage.config.SoundType;
import me.pljr.marriage.config.TitleType;
import me.pljr.marriage.exceptions.HasPartnerException;
import me.pljr.marriage.exceptions.NoHomeException;
import me.pljr.marriage.exceptions.NoPartnerException;
import me.pljr.marriage.managers.MarriageManager;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.builders.TitleBuilder;
import me.pljr.pljrapispigot.utils.ChatUtil;
import me.pljr.pljrapispigot.utils.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AMarryCommand extends CommandUtil {

    private final Marriage marriage;
    private final PlayerManager playerManager;
    private final MarriageManager manager;
    private final CfgSettings settings;

    public AMarryCommand(Marriage marriage, PlayerManager playerManager, MarriageManager manager, CfgSettings settings) {
        super("amarry", "marriage.admin");
        this.marriage = marriage;
        this.playerManager = playerManager;
        this.manager = manager;
        this.settings = settings;
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        MarriagePlayer marriagePlayer = playerManager.getPlayer(player);
        if (args.length < 1) {
            sendMessage(player, Lang.ADMIN_HELP.get());
            return;
        }
        final String arg = args[0].toUpperCase();

        if (args.length == 1){
            switch (arg) {
                case "HELP":
                    if (!checkPerm(player, "marriage.admin.help")) return;
                    sendMessage(player, Lang.ADMIN_HELP.get());
                    break;
                case "SPY":
                    if (!checkPerm(player, "marriage.admin.spy")) return;
                    marriagePlayer.setSpy(!marriagePlayer.isSpy());
                    if (marriagePlayer.isSpy()) {
                        sendMessage(player, Lang.SPY_TOGGLE.get().replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.SPY_TOGGLE.get().replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                case "RELOAD":
                    if (!checkPerm(player, "marriage.admin.reload")) return;
                    marriage.setupConfig();
                    sendMessage(player, Lang.RELOAD.get());
                    break;
            }
        }

        else if (args.length == 2){
            switch (arg) {
                case "UNMARRY":
                    if (!checkPerm(player, "marriage.admin.unmarry")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player unmarryTarget = Bukkit.getPlayer(args[1]);
                    MarriagePlayer marriageUnmarryTarget = playerManager.getPlayer(unmarryTarget);
                    UUID divorcePartnerId = marriageUnmarryTarget.getPartnerID();
                    try {
                        manager.divorce(marriageUnmarryTarget);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER_PLAYER.get().replace("{name}", unmarryTarget.getName()));
                        return;
                    }
                    Player divorcePartner = Bukkit.getPlayer(divorcePartnerId);
                    ChatUtil.broadcast(Lang.UNMARRY_BROADCAST.get()
                            .replace("{partner}", divorcePartner.getName())
                            .replace("{player}", unmarryTarget.getName()), "", settings.isBungee());
                    if (divorcePartnerId != null) {
                        SoundType.DIVORCE.get().play(divorcePartner);
                        TitleType.DIVORCE_PARTNER.get().send(divorcePartner);
                    }
                    SoundType.DIVORCE.get().play(unmarryTarget);
                    TitleType.DIVORCE_PLAYER.get().send(unmarryTarget);
                    break;
                case "SETHOME": {
                    if (!checkPerm(player, "marriage.admin.sethome")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player target = Bukkit.getPlayer(args[1]);
                    String targetName = target.getName();
                    MarriagePlayer marriageTarget = playerManager.getPlayer(target);
                    try {
                        manager.setHome(player, marriageTarget);
                    } catch (NoPartnerException e) {
                        sendMessage(player, Lang.NO_PARTNER_PLAYER.get().replace("{name}", targetName));
                        return;
                    }
                    sendMessage(player, Lang.SET_HOME_ADMIN.get().replace("{name}", targetName));
                    break;
                }
                case "HOME": {
                    if (!checkPerm(player, "marriage.admin.home")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player target = Bukkit.getPlayer(args[1]);
                    String targetName = target.getName();
                    MarriagePlayer marriageTarget = playerManager.getPlayer(target);
                    try {
                        manager.home(player, marriageTarget);
                    } catch (NoHomeException e) {
                        sendMessage(player, Lang.NO_HOME_PLAYER.get().replace("{name}", targetName));
                        return;
                    }
                    sendMessage(player, Lang.TP_HOME_ADMIN.get().replace("{name}", targetName));
                    break;
                }
                case "XP": {
                    if (!checkPerm(player, "marriage.admin.pvp")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player target = Bukkit.getPlayer(args[1]);
                    String targetName = target.getName();
                    MarriagePlayer marriageTarget = playerManager.getPlayer(target);
                    marriageTarget.setSharedXP(!marriageTarget.isSharedXP());
                    if (marriageTarget.isSharedXP()) {
                        sendMessage(player, Lang.XP_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.XP_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                }
                case "FOOD": {
                    if (!checkPerm(player, "marriage.admin.food")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player target = Bukkit.getPlayer(args[1]);
                    String targetName = target.getName();
                    MarriagePlayer marriageTarget = playerManager.getPlayer(target);
                    marriageTarget.setSharedFood(!marriageTarget.isSharedFood());
                    if (marriageTarget.isSharedFood()) {
                        sendMessage(player, Lang.FOOD_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.FOOD_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                }
                case "PVP": {
                    if (!checkPerm(player, "marriage.admin.xp")) return;
                    if (!checkPlayer(player, args[1])) return;
                    Player target = Bukkit.getPlayer(args[1]);
                    String targetName = target.getName();
                    MarriagePlayer marriageTarget = playerManager.getPlayer(target);
                    marriageTarget.setPvp(!marriageTarget.isPvp());
                    if (marriageTarget.isPvp()) {
                        sendMessage(player, Lang.PVP_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.ACTIVE.get()));
                    } else {
                        sendMessage(player, Lang.PVP_TOGGLE_ADMIN.get()
                                .replace("{name}", targetName)
                                .replace("{state}", Lang.INACTIVE.get()));
                    }
                    playerManager.setPlayer(player, marriagePlayer);
                    break;
                }
            }
        }

        else if (args.length == 3){
            if (arg.equals("MARRY")) {
                if (!checkPerm(player, "marriage.admin.marry")) return;
                if (!checkPlayer(player, args[1])) return;
                if (!checkPlayer(player, args[2])) return;
                Player marryTargetOne = Bukkit.getPlayer(args[1]);
                Player marryTargetTwo = Bukkit.getPlayer(args[2]);
                UUID marryTargetOneId = marryTargetOne.getUniqueId();
                UUID marryTargetTwoId = marryTargetTwo.getUniqueId();
                MarriagePlayer marriageTargetOne = playerManager.getPlayer(marryTargetOneId);
                MarriagePlayer marriageTargetTwo = playerManager.getPlayer(marryTargetTwoId);
                try {
                    manager.marry(marriageTargetOne, marriageTargetTwo);
                } catch (HasPartnerException e) {
                    if (e.getSource() == marryTargetOneId) {
                        sendMessage(player, Lang.HAS_PARTNER.get().replace("{name}", marryTargetOne.getName()));
                    } else {
                        sendMessage(player, Lang.HAS_PARTNER.get().replace("{name}", marryTargetTwo.getName()));
                    }
                    return;
                }
                String marryTargetOneName = marryTargetOne.getName();
                String marryTargetTwoName = marryTargetTwo.getName();
                new TitleBuilder(TitleType.MARRY_PLAYER.get())
                        .replaceSubtitle("{name}", marryTargetTwoName)
                        .create().send(marryTargetOne);
                new TitleBuilder(TitleType.MARRY_PARTNER.get())
                        .replaceSubtitle("{name}", marryTargetOneName)
                        .create().send(marryTargetTwo);
                SoundType.MARRY_ACCEPT.get().play(marryTargetOne);
                SoundType.MARRY_ACCEPT.get().play(marryTargetTwo);
                ChatUtil.broadcast(Lang.MARRY_BROADCAST.get()
                                .replace("{partner}", marryTargetTwoName)
                                .replace("{player}", marryTargetOneName),
                        "", settings.isBungee());
            }
        }
    }
}
