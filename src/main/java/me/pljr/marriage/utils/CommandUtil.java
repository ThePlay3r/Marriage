package me.pljr.marriage.utils;

import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.config.CfgOptions;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.enums.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static void fail(Player player){
        Location playerLoc = player.getLocation();
        if (CfgOptions.sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.FAIL), 10, 1);
    }

    public static boolean checkPerm(Player player, String perm){
        if (player.hasPermission(perm)) return true;
        player.sendMessage(CfgMessages.messages.get(Message.NO_PERM));
        fail(player);
        return false;
    }

    public static boolean checkPlayer(CommandSender sender, String requestName){
        Player request = Bukkit.getPlayer(requestName);
        if (request == null || !request.isOnline()){
            sender.sendMessage(CfgMessages.messages.get(Message.OFFLINE).replace("%name", requestName));
            if (sender instanceof Player){
                CommandUtil.fail((Player) sender);
            }
            return false;
        }
        return true;
    }
}
