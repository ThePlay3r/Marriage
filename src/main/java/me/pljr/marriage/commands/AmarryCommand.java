package me.pljr.marriage.commands;

import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.marriage.utils.MarryUtil;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.pljrapi.utils.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmarryCommand extends CommandUtil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /amarry help
        if (!(args.length > 0) || args[0].equalsIgnoreCase("help")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.help")) return false;
            }
            CfgLang.adminHelp.forEach(sender::sendMessage);
            return true;
        }

        // /amarry spy
        if (args[0].equalsIgnoreCase("spy")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!checkPerm(sender, "marriage.admin.spy")) return false;
            CorePlayer corePlayer = PlayerManager.getPlayerManager(playerName);
            if (corePlayer.isSpy()){
                player.sendMessage(CfgLang.lang.get(Lang.SPY_UNACTIVE));
                corePlayer.setSpy(false);
            }else{
                player.sendMessage(CfgLang.lang.get(Lang.SPY_ACTIVE));
                corePlayer.setSpy(true);
            }
            PlayerManager.setPlayerManager(playerName, corePlayer);
            return true;
        }

        // /amarry marry <playerName> <playerName>
        if (args[0].equalsIgnoreCase("marry")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.marry")) return false;
            }
            if (!(args.length == 3)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(sender, args[1]) || !checkPlayer(sender, args[2])) return false;
            if (MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgLang.lang.get(Lang.HAVE_PARTNER_PLAYER).replace("%name", args[1]));
                return false;
            }
            if (MarryUtil.isMarried(args[2])){
                sender.sendMessage(CfgLang.lang.get(Lang.HAVE_PARTNER_PLAYER).replace("%name", args[2]));
                return false;
            }
            MarryUtil.marry(args[1], args[2]);
            return true;
        }

        // /amarry unmarry <playerName>
        if (args[0].equalsIgnoreCase("unmarry")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.unmarry")) return false;
            }
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.divorce(args[1]);
            return true;
        }

        // /amarry sethome <playerName>
        if (args[0].equalsIgnoreCase("sethome")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!checkPerm(sender, "marriage.admin.sethome")) return false;
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(player, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                player.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.setHome(args[1], player.getLocation(), false);
            return true;
        }

        // /amarry home <playerName>
        if (args[0].equalsIgnoreCase("home")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!checkPerm(sender, "marriage.admin.home")) return false;
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(player, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                player.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = PlayerManager.getPlayerManager(args[1]);
            player.teleport(requestManager.getHome());
            return true;
        }

        // /amarry pvp <playerName>
        if (args[0].equalsIgnoreCase("pvp")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.pvp")) return false;
            }
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = PlayerManager.getPlayerManager(args[1]);
            if (requestManager.isPvp()){
                requestManager.setPvp(false);
                sender.sendMessage(CfgLang.lang.get(Lang.PVP_OFF_OTHERS));
            }else{
                requestManager.setPvp(true);
                sender.sendMessage(CfgLang.lang.get(Lang.PVP_ON_OTHERS));
            }
            PlayerManager.setPlayerManager(args[1], requestManager);
            return true;
        }

        // /amarry food <playerName>
        if (args[0].equalsIgnoreCase("food")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.food")) return false;
            }
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = PlayerManager.getPlayerManager(args[1]);
            if (requestManager.isFood()){
                requestManager.setFood(false);
                sender.sendMessage(CfgLang.lang.get(Lang.FOOD_OFF_OTHERS));
            }else{
                requestManager.setFood(true);
                sender.sendMessage(CfgLang.lang.get(Lang.FOOD_ON_OTHERS));
            }
            PlayerManager.setPlayerManager(args[1], requestManager);
            return true;
        }

        // /amarry xp <playerName>
        if (args[0].equalsIgnoreCase("xp")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.xp")) return false;
            }
            if (!(args.length == 2)){
                CfgLang.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = PlayerManager.getPlayerManager(args[1]);
            if (requestManager.isXp()){
                requestManager.setXp(false);
                sender.sendMessage(CfgLang.lang.get(Lang.XP_OFF_OTHERS));
            }else{
                requestManager.setXp(true);
                sender.sendMessage(CfgLang.lang.get(Lang.XP_ON_OTHERS));
            }
            PlayerManager.setPlayerManager(args[1], requestManager);
            return true;
        }

        if (sender instanceof Player){
            if (!checkPerm(sender, "marriage.admin.help")) return false;
            sendHelp(sender, CfgLang.adminHelp);
            return true;
        }
        sendHelp(sender, CfgLang.adminHelp);
        return true;
    }
}
