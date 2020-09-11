package me.pljr.marriage.commands;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.marriage.utils.MarryUtil;
import me.pljr.pljrapi.utils.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AmarryCommand extends CommandUtil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /amarry help
        if (!(args.length > 0) || args[0].equalsIgnoreCase("help")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.help")) return false;
            }
            sendHelp(sender, CfgLang.adminHelp);
            return true;
        }

        // /amarry spy
        if (args[0].equalsIgnoreCase("spy")){
            if (!(sender instanceof Player)){
                sendMessage(sender, CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            if (!checkPerm(sender, "marriage.admin.spy")) return false;
            CorePlayer corePlayer = Marriage.getPlayerManager().getPlayerManager(playerId);
            if (corePlayer.isSpy()){
                sendMessage(player, CfgLang.lang.get(Lang.SPY_UNACTIVE));
                corePlayer.setSpy(false);
            }else{
                sendMessage(player, CfgLang.lang.get(Lang.SPY_ACTIVE));
                corePlayer.setSpy(true);
            }
            Marriage.getPlayerManager().setPlayerManager(playerId, corePlayer);
            return true;
        }

        // /amarry marry <playerName> <playerName>
        if (args[0].equalsIgnoreCase("marry")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.marry")) return false;
            }
            if (!(args.length == 3)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(sender, args[1]) || !checkPlayer(sender, args[2])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            UUID player2Id = Bukkit.getPlayer(args[2]).getUniqueId();
            if (MarryUtil.isMarried(player1Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.HAVE_PARTNER_PLAYER).replace("%name", args[1]));
                return false;
            }
            if (MarryUtil.isMarried(player2Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.HAVE_PARTNER_PLAYER).replace("%name", args[2]));
                return false;
            }
            MarryUtil.marry(player1Id, player2Id);
            return true;
        }

        // /amarry unmarry <playerName>
        if (args[0].equalsIgnoreCase("unmarry")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.unmarry")) return false;
            }
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.divorce(player1Id);
            return true;
        }

        // /amarry sethome <playerName>
        if (args[0].equalsIgnoreCase("sethome")){
            if (!(sender instanceof Player)){
                sendMessage(sender, CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!checkPerm(sender, "marriage.admin.sethome")) return false;
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(player, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(player, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            MarryUtil.setHome(player1Id, player.getLocation(), false);
            return true;
        }

        // /amarry home <playerName>
        if (args[0].equalsIgnoreCase("home")){
            if (!(sender instanceof Player)){
                sendMessage(sender, CfgLang.lang.get(Lang.NO_CONSOLE));
                return false;
            }
            Player player = (Player) sender;
            if (!checkPerm(sender, "marriage.admin.home")) return false;
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(player, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(player, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = Marriage.getPlayerManager().getPlayerManager(player1Id);
            player.teleport(requestManager.getHome());
            return true;
        }

        // /amarry pvp <playerName>
        if (args[0].equalsIgnoreCase("pvp")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.pvp")) return false;
            }
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = Marriage.getPlayerManager().getPlayerManager(player1Id);
            if (requestManager.isPvp()){
                requestManager.setPvp(false);
                sendMessage(sender, CfgLang.lang.get(Lang.PVP_OFF_OTHERS));
            }else{
                requestManager.setPvp(true);
                sendMessage(sender, CfgLang.lang.get(Lang.PVP_ON_OTHERS));
            }
            Marriage.getPlayerManager().setPlayerManager(player1Id, requestManager);
            return true;
        }

        // /amarry food <playerName>
        if (args[0].equalsIgnoreCase("food")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.food")) return false;
            }
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = Marriage.getPlayerManager().getPlayerManager(player1Id);
            if (requestManager.isFood()){
                requestManager.setFood(false);
                sendMessage(sender, CfgLang.lang.get(Lang.FOOD_OFF_OTHERS));
            }else{
                requestManager.setFood(true);
                sendMessage(sender, CfgLang.lang.get(Lang.FOOD_ON_OTHERS));
            }
            Marriage.getPlayerManager().setPlayerManager(player1Id, requestManager);
            return true;
        }

        // /amarry xp <playerName>
        if (args[0].equalsIgnoreCase("xp")){
            if (sender instanceof Player){
                if (!checkPerm(sender, "marriage.admin.xp")) return false;
            }
            if (!(args.length == 2)){
                sendHelp(sender, CfgLang.adminHelp);
                return false;
            }
            if (!checkPlayer(sender, args[1])) return false;
            UUID player1Id = Bukkit.getPlayer(args[1]).getUniqueId();
            if (!MarryUtil.isMarried(player1Id)){
                sendMessage(sender, CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", args[1]));
                return false;
            }
            CorePlayer requestManager = Marriage.getPlayerManager().getPlayerManager(player1Id);
            if (requestManager.isXp()){
                requestManager.setXp(false);
                sendMessage(sender, CfgLang.lang.get(Lang.XP_OFF_OTHERS));
            }else{
                requestManager.setXp(true);
                sendMessage(sender, CfgLang.lang.get(Lang.XP_ON_OTHERS));
            }
            Marriage.getPlayerManager().setPlayerManager(player1Id, requestManager);
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
