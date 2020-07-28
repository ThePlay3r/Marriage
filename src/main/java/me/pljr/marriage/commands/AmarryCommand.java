package me.pljr.marriage.commands;

import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.CommandUtil;
import me.pljr.marriage.utils.MarryUtil;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmarryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(args.length > 0) || args[0].equalsIgnoreCase("help")){
            if (sender instanceof Player){
                if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.help")) return false;
            }
            CfgMessages.adminHelp.forEach(sender::sendMessage);
            return true;
        }
        if (args[0].equalsIgnoreCase("spy")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgMessages.messages.get(Message.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.spy")) return false;
            PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
            if (playerManager.isSpy()){
                player.sendMessage(CfgMessages.messages.get(Message.SPY_UNACTIVE));
                playerManager.setSpy(false);
            }else{
                player.sendMessage(CfgMessages.messages.get(Message.SPY_ACTIVE));
                playerManager.setSpy(true);
            }
            PlayerUtil.setPlayerManager(playerName, playerManager);
            return true;
        }
        if (args[0].equalsIgnoreCase("marry")){
            if (sender instanceof Player){
                if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.marry")) return false;
            }
            if (!(args.length == 3)){
                CfgMessages.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!CommandUtil.checkPlayer(sender, args[1]) || !CommandUtil.checkPlayer(sender, args[2])) return false;
            if (MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgMessages.messages.get(Message.HAVE_PARTNER_PLAYER).replace("%name", args[1]));
                return false;
            }
            if (MarryUtil.isMarried(args[2])){
                sender.sendMessage(CfgMessages.messages.get(Message.HAVE_PARTNER_PLAYER).replace("%name", args[2]));
                return false;
            }
            MarryUtil.marry(args[1], args[2]);
            return true;
        }
        if (args[0].equalsIgnoreCase("unmarry")){
            if (sender instanceof Player){
                if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.unmarry")) return false;
            }
            if (!(args.length == 2)){
                CfgMessages.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!CommandUtil.checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgMessages.messages.get(Message.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.divorce(args[1]);
            return true;
        }
        if (args[0].equalsIgnoreCase("sethome")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgMessages.messages.get(Message.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.sethome")) return false;
            if (!(args.length == 2)){
                CfgMessages.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!CommandUtil.checkPlayer(player, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                player.sendMessage(CfgMessages.messages.get(Message.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.setHome(args[1], player.getLocation());
            return true;
        }
        if (args[0].equalsIgnoreCase("home")){
            if (!(sender instanceof Player)){
                sender.sendMessage(CfgMessages.messages.get(Message.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.home")) return false;
            if (!(args.length == 2)){
                CfgMessages.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!CommandUtil.checkPlayer(player, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                player.sendMessage(CfgMessages.messages.get(Message.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            PlayerManager requestManager = PlayerUtil.getPlayerManager(args[1]);
            player.teleport(requestManager.getHome());
            return true;
        }
        if (args[0].equalsIgnoreCase("pvp")){
            if (sender instanceof Player){
                if (!CommandUtil.checkPerm((Player) sender, "marriage.admin.pvp")) return false;
            }
            if (!(args.length == 2)){
                CfgMessages.adminHelp.forEach(sender::sendMessage);
                return false;
            }
            if (!CommandUtil.checkPlayer(sender, args[1])) return false;
            if (!MarryUtil.isMarried(args[1])){
                sender.sendMessage(CfgMessages.messages.get(Message.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            PlayerManager requestManager = PlayerUtil.getPlayerManager(args[1]);
            if (requestManager.isPvp()){
                requestManager.setPvp(false);
                sender.sendMessage(CfgMessages.messages.get(Message.PVP_OFF_OTHERS));
            }else{
                requestManager.setPvp(true);
                sender.sendMessage(CfgMessages.messages.get(Message.PVP_ON_OTHERS));
            }
            PlayerUtil.setPlayerManager(args[1], requestManager);
            return true;
        }
        CfgMessages.adminHelp.forEach(sender::sendMessage);
        return false;
    }
}
