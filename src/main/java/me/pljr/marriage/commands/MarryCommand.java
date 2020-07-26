package me.pljr.marriage.commands;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.config.CfgOptions;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.menus.MarryMenu;
import me.pljr.marriage.utils.FormatUtil;
import me.pljr.marriage.utils.MarryUtil;
import me.pljr.marriage.utils.NumberUtil;
import me.pljr.marriage.utils.PlayerUtil;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarryCommand implements CommandExecutor {
    private final Economy economy = Marriage.getEconomy();

    private final boolean sounds = CfgOptions.sounds;
    private final int costMarry = CfgOptions.costMarry;
    private final int costDivorce = CfgOptions.costDivorce;
    private final int cooldown = CfgOptions.cooldown;
    private final boolean togglepvp = CfgOptions.togglepvp;

    private void fail(Player player){
        Location playerLoc = player.getLocation();
        if (sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.FAIL), 10, 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(CfgMessages.messages.get(Message.NO_CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("marriage.use")){
            player.sendMessage(CfgMessages.messages.get(Message.NO_PERM));
            fail(player);
            return false;
        }
        String playerName = player.getName();
        Location playerLoc = player.getLocation();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
        if (args.length > 1){
            if (args[0].equalsIgnoreCase("c")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                MarryUtil.chat(player, FormatUtil.colorString(StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ")));
                return true;
            }
        }
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("give")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner == null || !partner.isOnline()){
                    player.sendMessage(CfgMessages.messages.get(Message.OFFLINE).replace("%name", partnerName));
                    fail(player);
                    return false;
                }
                if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR){
                    player.sendMessage(CfgMessages.messages.get(Message.ITEM_IN_HAND));
                    fail(player);
                    return false;
                }
                player.sendMessage(CfgMessages.messages.get(Message.GIFT_SEND).replace("%name", partnerName));
                partner.sendMessage(CfgMessages.messages.get(Message.GIFT_RECEIVE).replace("%name", playerName));
                ItemStack giveItem = player.getInventory().getItemInMainHand();
                player.getInventory().setItemInMainHand(null);
                player.updateInventory();
                if (partner.getInventory().firstEmpty() == -1){
                    partner.getWorld().dropItem(partner.getLocation(), giveItem);
                }else{
                    partner.getInventory().addItem(giveItem);
                    partner.updateInventory();
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("home")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                player.teleport(playerManager.getHome());
                player.sendMessage(CfgMessages.messages.get(Message.TPHOME));
                return true;
            }

            if (args[0].equalsIgnoreCase("sethome")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                MarryUtil.setHome(playerName, playerLoc);
                player.sendMessage(CfgMessages.messages.get(Message.SETHOME_PLAYER));
                return true;
            }

            if (args[0].equalsIgnoreCase("seen")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner != null && partner.isOnline()){
                    player.sendMessage(CfgMessages.messages.get(Message.ONLINE).replace("%name", partnerName));
                    return true;
                }
                PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
                String lastseen = FormatUtil.formatTime((System.currentTimeMillis() - partnerManager.getLastseen()) / 1000);
                player.sendMessage(CfgMessages.messages.get(Message.LASTSEEN).replace("%name", partnerName).replace("%time", lastseen));
                return true;
            }

            if (args[0].equalsIgnoreCase("list")){
                MarryUtil.sendMarryList(player, 0);
                return true;
            }

            if (args[0].equalsIgnoreCase("pvp")){
                if (!togglepvp) return false;
                boolean pvp = playerManager.isPvp();
                if (pvp){
                    pvp = false;
                    player.sendMessage(CfgMessages.messages.get(Message.PVP_ON));
                }else{
                    pvp = true;
                    player.sendMessage(CfgMessages.messages.get(Message.PVP_OFF));
                }
                playerManager.setPvp(pvp);
                PlayerUtil.setPlayerManager(playerName, playerManager);
                return true;
            }

            if (args[0].equalsIgnoreCase("tp")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner == null || !partner.isOnline()){
                    player.sendMessage(CfgMessages.messages.get(Message.OFFLINE).replace("%name", partnerName));
                    fail(player);
                    return false;
                }
                player.teleport(partner);
                player.sendMessage(CfgMessages.messages.get(Message.TELEPORT_PLAYER).replace("%name", partnerName));
                partner.sendMessage(CfgMessages.messages.get(Message.TELEPORT_PARTNER).replace("%name", playerName));
                return true;
            }

            if (args[0].equalsIgnoreCase("divorce")){
                if (playerManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_PARTNER));
                    fail(player);
                    return false;
                }
                if (economy.getBalance(player) < costDivorce){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_MONEY).replace("%cost", costDivorce+""));
                    return false;
                }
                economy.withdrawPlayer(player, costDivorce);
                MarryUtil.divorce(playerName);
                player.sendTitle(CfgMessages.messages.get(Message.DIVORCE_PLAYER_TITLE), CfgMessages.messages.get(Message.DIVORCE_PLAYER_SUBTITLE), 10, 20*3, 10);
                if (sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.DIVORCE), 10, 1);
                return true;
            }

            /*

            Marrying

             */
            String requestName = args[0];
            if (requestName.equalsIgnoreCase(playerName)){
                player.openInventory(MarryMenu.getMenu(playerName));
                return false;
            }
            Player request = Bukkit.getPlayer(requestName);
            if (request == null || !request.isOnline()){
                player.sendMessage(CfgMessages.messages.get(Message.OFFLINE).replace("%name", requestName));
                fail(player);
                return false;
            }
            PlayerManager requestManager = PlayerUtil.getPlayerManager(requestName);
            if (playerManager.getPartner() != null){
                player.sendMessage(CfgMessages.messages.get(Message.HAVE_PARTNER));
                return true;
            }
            if (requestManager.getPartner() != null){
                player.sendMessage(CfgMessages.messages.get(Message.HAVE_PARTNER_PLAYER).replace("%name", requestName));
                fail(player);
                return false;
            }
            if (PlayerUtil.getRequests().containsKey(playerName)){
                player.sendMessage(CfgMessages.messages.get(Message.REQUEST_PENDING));
                return true;
            }
            if (economy.getBalance(player) < costMarry){
                player.sendMessage(CfgMessages.messages.get(Message.NO_MONEY).replace("%cost", costMarry+""));
                return false;
            }
            if (PlayerUtil.getRequests().containsKey(requestName)){
                if (!PlayerUtil.getRequests().get(requestName).equals(playerName)){
                    player.sendMessage(CfgMessages.messages.get(Message.REQUEST_PENDING_PLAYER).replace("%name", requestName));
                    fail(player);
                    return false;
                }
                if (sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.MARRY_ACCEPT), 10, 1);
                player.sendTitle(CfgMessages.messages.get(Message.MARRY_ACCEPT_PLAYER1_TITLE),
                        CfgMessages.messages.get(Message.MARRY_ACCEPT_PLAYER1_SUBTITLE).replace("%name", requestName),
                        10, 20*3, 10);
                if (sounds) request.playSound(playerLoc, CfgSounds.sounds.get(Sounds.MARRY_ACCEPT), 10, 1);
                request.sendTitle(CfgMessages.messages.get(Message.MARRY_ACCEPT_PLAYER2_TITLE),
                        CfgMessages.messages.get(Message.MARRY_ACCEPT_PLAYER2_SUBTITLE).replace("%name", playerName),
                        10, 20*3, 10);
                MarryUtil.marry(playerName, requestName);
                economy.withdrawPlayer(player, costMarry);
                economy.withdrawPlayer(request, costMarry);
                PlayerUtil.getRequests().remove(requestName);
                PlayerUtil.getRequests().remove(playerName);
                return true;
            }
            player.sendMessage(CfgMessages.messages.get(Message.REQUEST_SEND).replace("%name", requestName));
            request.sendMessage(CfgMessages.messages.get(Message.REQUEST_RECEIVED).replace("%name", playerName));
            if (sounds) request.playSound(request.getLocation(), CfgSounds.sounds.get(Sounds.NOTIFY), 10, 1);
            PlayerUtil.getRequests().put(playerName, requestName);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Marriage.getInstance(), () ->{
                if (PlayerUtil.getRequests().containsKey(playerName)){
                    if (player.isOnline()){
                        player.sendMessage(CfgMessages.messages.get(Message.REQUEST_EXPIRED_SENDER).replace("%name", requestName));
                    }
                    if (request.isOnline()){
                        request.sendMessage(CfgMessages.messages.get(Message.REQUEST_EXPIRED_RECEIVER).replace("%name", playerName));
                    }
                    PlayerUtil.getRequests().remove(playerName);
                }
            }, cooldown);
            return true;
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase("list")){
                if (!NumberUtil.isInt(args[1])){
                    player.sendMessage(CfgMessages.messages.get(Message.NO_NUMBER).replace("%arg", args[1]));
                    fail(player);
                    return false;
                }
                MarryUtil.sendMarryList(player, Integer.parseInt(args[1]));
                return true;
            }

            if (args[0].equalsIgnoreCase("gender")){
                String genderType = args[1].toUpperCase();
                for (Gender gender : Gender.values()){
                    if (gender.toString().equals(genderType)){
                        switch (gender){
                            case FEMALE:
                                player.sendMessage(CfgMessages.messages.get(Message.GENDER_SET).replace("%gender", CfgMessages.messages.get(Message.GENDER_FEMALE)));
                                break;
                            case NONE:
                                player.sendMessage(CfgMessages.messages.get(Message.GENDER_SET).replace("%gender", CfgMessages.messages.get(Message.GENDER_NONE)));
                                break;
                            case MALE:
                                player.sendMessage(CfgMessages.messages.get(Message.GENDER_SET).replace("%gender", CfgMessages.messages.get(Message.GENDER_MALE)));
                                break;
                        }
                        playerManager.setGender(gender);
                        PlayerUtil.setPlayerManager(playerName, playerManager);
                        return true;
                    }
                }
                player.sendMessage(CfgMessages.messages.get(Message.NO_GENDER).replace("%gender", genderType));
                fail(player);
                return false;
            }

            if (args[0].equalsIgnoreCase("partner")){
                String requestedInfoName = args[1];
                PlayerManager requestedInfoManager = PlayerUtil.getPlayerManager(requestedInfoName);
                if (requestedInfoManager.getPartner() == null){
                    player.sendMessage(CfgMessages.messages.get(Message.PARTNER_NO_PARTNER).replace("%name", requestedInfoName));
                }else{
                    player.sendMessage(CfgMessages.messages.get(Message.PARTNER).replace("%name", requestedInfoName).replace("%partner", requestedInfoManager.getPartner()));
                }
                return true;
            }
        }
        player.openInventory(MarryMenu.getMenu(playerName));
        return true;
    }
}
