package me.pljr.marriage.commands;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.config.CfgSettings;
import me.pljr.marriage.config.CfgSounds;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.enums.Sounds;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.menus.MarryMenu;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.marriage.utils.*;
import me.pljr.pljrapi.PLJRApi;
import me.pljr.pljrapi.managers.TitleManager;
import me.pljr.pljrapi.objects.PLJRTitle;
import me.pljr.pljrapi.utils.CommandUtil;
import me.pljr.pljrapi.utils.FormatUtil;
import me.pljr.pljrapi.utils.NumberUtil;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarryCommand implements CommandExecutor {
    private final Economy economy = PLJRApi.getVaultEcon();

    private final boolean sounds = CfgSettings.sounds;
    private final int costMarry = CfgSettings.costMarry;
    private final int costDivorce = CfgSettings.costDivorce;
    private final int cooldown = CfgSettings.cooldown;
    private final boolean togglepvp = CfgSettings.togglepvp;
    private final boolean togglefood = CfgSettings.togglefood;
    private final boolean togglexp = CfgSettings.togglexp;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(CfgLang.lang.get(Lang.NO_CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (!CommandUtil.checkPerm(player, "marriage.use")) return false;
        String playerName = player.getName();
        Location playerLoc = player.getLocation();
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerName);
        if (args.length > 1){

            // /marry c <string>
            if (args[0].equalsIgnoreCase("c")){
                if (!CommandUtil.checkPerm(player, "marriage.chat")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                MarryUtil.chat(player, FormatUtil.colorString(StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ")));
                return true;
            }

        }
        if (args.length == 1){

            // /marry food
            if (args[0].equalsIgnoreCase("food")){
                if (!togglefood){
                    CommandUtil.sendHelp(sender, CfgLang.help);
                    return false;
                }
                if (!CommandUtil.checkPerm(player, "marriage.food")) return false;
                if (corePlayer.isFood()){
                    player.sendMessage(CfgLang.lang.get(Lang.FOOD_OFF));
                    corePlayer.setFood(false);
                }else{
                    player.sendMessage(CfgLang.lang.get(Lang.FOOD_ON));
                    corePlayer.setFood(true);
                }
                PlayerManager.setPlayerManager(playerName, corePlayer);
                return true;
            }

            // /marry xp
            if (args[0].equalsIgnoreCase("xp")){
                if (!togglexp){
                    CommandUtil.sendHelp(sender, CfgLang.help);
                    return false;
                }
                if (!CommandUtil.checkPerm(player, "marriage.xp")) return false;
                if (corePlayer.isXp()){
                    player.sendMessage(CfgLang.lang.get(Lang.XP_OFF));
                    corePlayer.setXp(false);
                }else{
                    player.sendMessage(CfgLang.lang.get(Lang.XP_ON));
                    corePlayer.setXp(true);
                }
                PlayerManager.setPlayerManager(playerName, corePlayer);
                return true;
            }

            // /marry help
            if (args[0].equalsIgnoreCase("help")){
                if (!CommandUtil.checkPerm(player, "marriage.help")) return false;
                CommandUtil.sendHelp(sender, CfgLang.help);
                return true;
            }

            // /marry give
            if (args[0].equalsIgnoreCase("give")){
                if (!CommandUtil.checkPerm(player, "marriage.give")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                String partnerName = corePlayer.getPartner();
                if (!CommandUtil.checkPlayer(player, partnerName)) return false;
                Player partner = Bukkit.getPlayer(partnerName);
                if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType() == Material.AIR){
                    player.sendMessage(CfgLang.lang.get(Lang.ITEM_IN_HAND));
                    CommandUtil.fail(player);
                    return false;
                }
                player.sendMessage(CfgLang.lang.get(Lang.GIFT_SEND).replace("%name", partnerName));
                partner.sendMessage(CfgLang.lang.get(Lang.GIFT_RECEIVE).replace("%name", playerName));
                ItemStack giveItem = player.getInventory().getItemInHand();
                player.getInventory().setItemInHand(null);
                player.updateInventory();
                if (partner.getInventory().firstEmpty() == -1){
                    partner.getWorld().dropItem(partner.getLocation(), giveItem);
                }else{
                    partner.getInventory().addItem(giveItem);
                    partner.updateInventory();
                }
                return true;
            }

            // /marry home
            if (args[0].equalsIgnoreCase("home")){
                if (!CommandUtil.checkPerm(player, "marriage.home")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                player.teleport(corePlayer.getHome());
                player.sendMessage(CfgLang.lang.get(Lang.TPHOME));
                return true;
            }

            // /marry sethome
            if (args[0].equalsIgnoreCase("sethome")){
                if (!CommandUtil.checkPerm(player, "marriage.sethome")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                MarryUtil.setHome(playerName, playerLoc, true);
                player.sendMessage(CfgLang.lang.get(Lang.SETHOME_PLAYER));
                return true;
            }

            // /marry seen
            if (args[0].equalsIgnoreCase("seen")){
                if (!CommandUtil.checkPerm(player, "marriage.seen")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                String partnerName = corePlayer.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner != null && partner.isOnline()){
                    player.sendMessage(CfgLang.lang.get(Lang.ONLINE).replace("%name", partnerName));
                    return true;
                }
                CorePlayer partnerManager = PlayerManager.getPlayerManager(partnerName);
                String lastseen = FormatUtil.formatTime((System.currentTimeMillis() - partnerManager.getLastseen()) / 1000);
                player.sendMessage(CfgLang.lang.get(Lang.LASTSEEN).replace("%name", partnerName).replace("%time", lastseen));
                return true;
            }

            // /marry list
            if (args[0].equalsIgnoreCase("list")){
                if (!CommandUtil.checkPerm(player, "marriage.list")) return false;
                MarryUtil.sendMarryList(player, 0);
                return true;
            }

            // /marry pvp
            if (args[0].equalsIgnoreCase("pvp")){
                if (!togglepvp){
                    CommandUtil.sendHelp(sender, CfgLang.help);
                    return false;
                }
                if (!CommandUtil.checkPerm(player, "marriage.pvp")) return false;
                boolean pvp = corePlayer.isPvp();
                if (pvp){
                    pvp = false;
                    player.sendMessage(CfgLang.lang.get(Lang.PVP_OFF));
                }else{
                    pvp = true;
                    player.sendMessage(CfgLang.lang.get(Lang.PVP_ON));
                }
                corePlayer.setPvp(pvp);
                PlayerManager.setPlayerManager(playerName, corePlayer);
                return true;
            }

            // /marry tp
            if (args[0].equalsIgnoreCase("tp")){
                if (!CommandUtil.checkPerm(player, "marriage.tp")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                String partnerName = corePlayer.getPartner();
                if (!CommandUtil.checkPlayer(player, partnerName)) return false;
                Player partner = Bukkit.getPlayer(partnerName);
                player.teleport(partner);
                player.sendMessage(CfgLang.lang.get(Lang.TELEPORT_PLAYER).replace("%name", partnerName));
                partner.sendMessage(CfgLang.lang.get(Lang.TELEPORT_PARTNER).replace("%name", playerName));
                return true;
            }

            // /marry divorce
            if (args[0].equalsIgnoreCase("divorce")){
                if (!CommandUtil.checkPerm(player, "marriage.divorce")) return false;
                if (corePlayer.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_PARTNER));
                    CommandUtil.fail(player);
                    return false;
                }
                if (economy.getBalance(player) < costDivorce){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_MONEY).replace("%cost", costDivorce+""));
                    return false;
                }
                economy.withdrawPlayer(player, costDivorce);
                MarryUtil.divorce(playerName);
                TitleManager.send(player, new PLJRTitle(CfgLang.lang.get(Lang.DIVORCE_PLAYER_TITLE), CfgLang.lang.get(Lang.DIVORCE_PLAYER_SUBTITLE), 10, 20*3, 10));
                if (sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.DIVORCE), 10, 1);
                return true;
            }

            /*

            Marrying

             */
            String requestName = args[0];
            if (requestName.equalsIgnoreCase(playerName)){
                if (CfgSettings.menu){
                    player.openInventory(MarryMenu.getMenu(playerName));
                }else{
                    CommandUtil.sendHelp(sender, CfgLang.help);
                }
                return false;
            }
            if (!CommandUtil.checkPlayer(player, requestName)) return false;
            Player request = Bukkit.getPlayer(requestName);
            CorePlayer requestManager = PlayerManager.getPlayerManager(requestName);
            if (corePlayer.getPartner() != null){
                player.sendMessage(CfgLang.lang.get(Lang.HAVE_PARTNER));
                return true;
            }
            if (requestManager.getPartner() != null){
                player.sendMessage(CfgLang.lang.get(Lang.HAVE_PARTNER_PLAYER).replace("%name", requestName));
                CommandUtil.fail(player);
                return false;
            }
            if (PlayerManager.getRequests().containsKey(playerName)){
                player.sendMessage(CfgLang.lang.get(Lang.REQUEST_PENDING));
                return true;
            }
            if (economy.getBalance(player) < costMarry){
                player.sendMessage(CfgLang.lang.get(Lang.NO_MONEY).replace("%cost", costMarry+""));
                return false;
            }
            if (PlayerManager.getRequests().containsKey(requestName)){
                if (!PlayerManager.getRequests().get(requestName).equals(playerName)){
                    player.sendMessage(CfgLang.lang.get(Lang.REQUEST_PENDING_PLAYER).replace("%name", requestName));
                    CommandUtil.fail(player);
                    return false;
                }

                // Successful marry >
                MarryUtil.marry(playerName, requestName);
                TitleManager.send(player, new PLJRTitle(CfgLang.lang.get(Lang.MARRY_ACCEPT_PLAYER1_TITLE),
                        CfgLang.lang.get(Lang.MARRY_ACCEPT_PLAYER1_SUBTITLE).replace("%name", requestName),
                        10, 20*3, 10));
                TitleManager.send(player, new PLJRTitle(CfgLang.lang.get(Lang.MARRY_ACCEPT_PLAYER2_TITLE),
                        CfgLang.lang.get(Lang.MARRY_ACCEPT_PLAYER2_SUBTITLE).replace("%name", playerName),
                        10, 20*3, 10));
                if (sounds) player.playSound(playerLoc, CfgSounds.sounds.get(Sounds.MARRY_ACCEPT), 10, 1);
                if (sounds) request.playSound(playerLoc, CfgSounds.sounds.get(Sounds.MARRY_ACCEPT), 10, 1);
                economy.withdrawPlayer(player, costMarry);
                economy.withdrawPlayer(request, costMarry);
                PlayerManager.getRequests().remove(requestName);
                PlayerManager.getRequests().remove(playerName);
                return true;
            }
            player.sendMessage(CfgLang.lang.get(Lang.REQUEST_SEND).replace("%name", requestName));
            request.sendMessage(CfgLang.lang.get(Lang.REQUEST_RECEIVED).replace("%name", playerName));
            if (sounds) request.playSound(request.getLocation(), CfgSounds.sounds.get(Sounds.NOTIFY), 10, 1);
            PlayerManager.getRequests().put(playerName, requestName);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Marriage.getInstance(), () ->{
                if (PlayerManager.getRequests().containsKey(playerName)){
                    if (player.isOnline()){
                        player.sendMessage(CfgLang.lang.get(Lang.REQUEST_EXPIRED_SENDER).replace("%name", requestName));
                    }
                    if (request.isOnline()){
                        request.sendMessage(CfgLang.lang.get(Lang.REQUEST_EXPIRED_RECEIVER).replace("%name", playerName));
                    }
                    PlayerManager.getRequests().remove(playerName);
                }
            }, cooldown);
            return true;
        }
        if (args.length == 2){

            // /marry list <int>
            if (args[0].equalsIgnoreCase("list")){
                if (!CommandUtil.checkPerm(player, "marriage.list")) return false;
                if (!NumberUtil.isInt(args[1])){
                    player.sendMessage(CfgLang.lang.get(Lang.NO_NUMBER).replace("%arg", args[1]));
                    CommandUtil.fail(player);
                    return false;
                }
                MarryUtil.sendMarryList(player, Integer.parseInt(args[1]));
                return true;
            }

            // /marry gender <gender>
            if (args[0].equalsIgnoreCase("gender")){
                if (!CommandUtil.checkPerm(player, "marriage.gender")) return false;
                String genderType = args[1].toUpperCase();
                for (Gender gender : Gender.values()){
                    if (gender.toString().equals(genderType)){
                        switch (gender){
                            case FEMALE:
                                player.sendMessage(CfgLang.lang.get(Lang.GENDER_SET).replace("%gender", CfgLang.lang.get(Lang.GENDER_FEMALE)));
                                break;
                            case NONE:
                                player.sendMessage(CfgLang.lang.get(Lang.GENDER_SET).replace("%gender", CfgLang.lang.get(Lang.GENDER_NONE)));
                                break;
                            case MALE:
                                player.sendMessage(CfgLang.lang.get(Lang.GENDER_SET).replace("%gender", CfgLang.lang.get(Lang.GENDER_MALE)));
                                break;
                        }
                        corePlayer.setGender(gender);
                        PlayerManager.setPlayerManager(playerName, corePlayer);
                        return true;
                    }
                }
                player.sendMessage(CfgLang.lang.get(Lang.NO_GENDER).replace("%gender", genderType));
                CommandUtil.fail(player);
                return false;
            }

            // /marry partner <playerName>
            if (args[0].equalsIgnoreCase("partner")){
                if (!CommandUtil.checkPerm(player, "marriage.partner")) return false;
                String requestedInfoName = args[1];
                CorePlayer requestedInfoManager = PlayerManager.getPlayerManager(requestedInfoName);
                if (requestedInfoManager.getPartner() == null){
                    player.sendMessage(CfgLang.lang.get(Lang.PARTNER_NO_PARTNER).replace("%name", requestedInfoName));
                }else{
                    player.sendMessage(CfgLang.lang.get(Lang.PARTNER).replace("%name", requestedInfoName).replace("%partner", requestedInfoManager.getPartner()));
                }
                return true;
            }

        }
        if (CfgSettings.menu){
            player.openInventory(MarryMenu.getMenu(playerName));
        }else{
            CommandUtil.sendHelp(sender, CfgLang.help);
        }
        return true;
    }
}
