package me.pljr.marriage.commands;

import me.pljr.marriage.Marriage;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Message;
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

    private void fail(Player player){
        Location playerLoc = player.getLocation();
        player.playSound(playerLoc, Sound.ENTITY_VILLAGER_NO, 10, 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cTento príkaz je len pre hráčov!");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("marriage.use")){
            player.sendMessage("§cNemáte dostatočné pravomocie!");
            fail(player);
            return false;
        }
        String playerName = player.getName();
        Location playerLoc = player.getLocation();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);
        if (args.length > 1){
            if (args[0].equalsIgnoreCase("c")){
                if (playerManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
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
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner == null || !partner.isOnline()){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + partnerName + " §fnie je na serveri.");
                    fail(player);
                    return false;
                }
                if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fMusíš mať předmet v ruke!");
                    fail(player);
                    return false;
                }
                player.sendMessage("§c§l❤ §aSvadba §8» §fOdoslal/a si darček pre §b" + partnerName + "§f.");
                partner.sendMessage("§c§l❤ §aSvadba §8» §fObdržal/a si darček od §b" + playerName + "§f.");
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
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                player.teleport(playerManager.getHome());
                player.sendMessage("§c§l❤ §aSvadba §8» §bÚspešně §fsi sa teleportoval/a na Váš domov.");
                return true;
            }

            if (args[0].equalsIgnoreCase("sethome")){
                if (playerManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                MarryUtil.setHome(playerName, playerLoc);
                player.sendMessage("§c§l❤ §aSvadba §8» §bÚspešně §fsi nastavil/a zdieľaný domov.");
                return true;
            }

            if (args[0].equalsIgnoreCase("seen")){
                if (playerManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner != null && partner.isOnline()){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + partnerName + " §fje na serveri.");
                    return true;
                }
                PlayerManager partnerManager = PlayerUtil.getPlayerManager(partnerName);
                String lastseen = FormatUtil.formatTime((System.currentTimeMillis() - partnerManager.getLastseen()) / 1000);
                player.sendMessage("§c§l❤ §aSvadba §8» §b" + partnerName + " §fbol/a naposledy online pred §b" + lastseen);
                return true;
            }

            if (args[0].equalsIgnoreCase("list")){
                MarryUtil.sendMarryList(player, 0);
                return true;
            }

            if (args[0].equalsIgnoreCase("pvp")){
                boolean pvp = playerManager.isPvp();
                if (pvp){
                    pvp = false;
                    player.sendMessage("§c§l❤ §aSvadba §8» §bVypol/la §fsi si PvP.");
                }else{
                    pvp = true;
                    player.sendMessage("§c§l❤ §aSvadba §8» §bZapol/la §fsi si PvP.");
                }
                playerManager.setPvp(pvp);
                PlayerUtil.setPlayerManager(playerName, playerManager);
                return true;
            }

            if (args[0].equalsIgnoreCase("tp")){
                if (playerManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                String partnerName = playerManager.getPartner();
                Player partner = Bukkit.getPlayer(partnerName);
                if (partner == null || !partner.isOnline()){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + partnerName + " §fnie je na serveri.");
                    fail(player);
                    return false;
                }
                player.teleport(partner);
                player.sendMessage("§c§l❤ §aSvadba §8» §fTeleportoval/a si sa na §b" + partnerName + "§f.");
                partner.sendMessage("§c§l❤ §aSvadba §8» §b" + playerName + " §fsa na teba teleportoval/a.");
                return true;
            }

            if (args[0].equalsIgnoreCase("divorce")){
                if (playerManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš §bpartnera/ku§f.");
                    fail(player);
                    return false;
                }
                if (economy.getBalance(player) < 5000){
                    player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš dostatek financií. §7(§e$5,000§7)");
                    return false;
                }
                economy.withdrawPlayer(player, 5000);
                MarryUtil.divorce(playerName);
                player.sendTitle("§cSvadba", "§c✖ §fzrušil/a si §bmanželstvo§f.", 10, 20*3, 10);
                player.playSound(playerLoc, Sound.ITEM_SHIELD_BREAK, 10, 1);
                return true;
            }

            /*

            Marrying

             */
            String requestName = args[0];
            Player request = Bukkit.getPlayer(requestName);
            if (request == null || !request.isOnline()){
                player.sendMessage("§c§l❤ §aSvadba §8» §b" + requestName + " §fnie je online." );
                fail(player);
                return false;
            }
            PlayerManager requestManager = PlayerUtil.getPlayerManager(requestName);
            if (playerManager.getPartner() != null){
                player.sendMessage("§c§l❤ §aSvadba §8» §fUž máš §bpartnera/ku§f!");
                return true;
            }
            if (requestManager.getPartner() != null){
                player.sendMessage("§c§l❤ §aSvadba §8» §fUž má §bpartnera§f!");
                fail(player);
                return false;
            }
            if (PlayerUtil.getRequests().containsKey(playerName)){
                player.sendMessage("§c§l❤ §aSvadba §8» §fUž si odoslal §bžiadosť§f!");
                return true;
            }
            if (economy.getBalance(player) < 7500){
                player.sendMessage("§c§l❤ §aSvadba §8» §fNemáš dostatek financií. §7(§e$7,500§7)");
                return false;
            }
            if (PlayerUtil.getRequests().containsKey(requestName)){
                if (!PlayerUtil.getRequests().get(requestName).equals(playerName)){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + requestName + " §fuž odoslal žiadosť §bniekomu inému§f!");
                    fail(player);
                    return false;
                }
                player.playSound(playerLoc, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                player.sendTitle("§c§l❤ §aSvadba §c§l❤", "§bPrijal/a §fsi žiadosť o manželstve s §b" + requestName + "§f.", 10, 20*3, 10);
                request.playSound(playerLoc, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                request.sendTitle("§c§l❤ §aSvadba §c§l❤", "§b" + playerName + " prijal/a §ftvoju žiadosť o manželstve.", 10, 20*3, 10);
                MarryUtil.marry(playerName, requestName);
                economy.withdrawPlayer(player, 7500);
                economy.withdrawPlayer(request, 7500);
                PlayerUtil.getRequests().remove(requestName);
                PlayerUtil.getRequests().remove(playerName);
                return true;
            }
            player.sendMessage("§c§l❤ §aSvadba §8» §fOdoslal/a si žiadosť o manželstvo hráči/ke §b" + requestName + "§f.");
            request.sendMessage("§c§l❤ §aSvadba §8» §b" + playerName + " §fti odoslal/a žiadosť o manželstvo.");
            request.playSound(request.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            PlayerUtil.getRequests().put(playerName, requestName);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Marriage.getInstance(), () ->{
                if (PlayerUtil.getRequests().containsKey(playerName)){
                    if (player.isOnline()){
                        player.sendMessage("§c§l❤ §aSvadba §8» §b" + requestName + " neprijal/a §ftvoju žiadosť o manželstvo.");
                    }
                    if (request.isOnline()){
                        request.sendMessage("§c§l❤ §aSvadba §8» §bNeprijal/a §fsi žiadosť o manželstvo hráči/ke §b" + playerName + "§f.");
                    }
                    PlayerUtil.getRequests().remove(playerName);
                }
            }, 200);
            return true;
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase("list")){
                if (!NumberUtil.isInt(args[1])){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + args[1] + " §fnie je číslo!");
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
                        player.sendMessage("§c§l❤ §aSvadba §8» §fNastavil/a si si pohlavie na §b" + genderType.toLowerCase() + "§f.");
                        playerManager.setGender(gender);
                        PlayerUtil.setPlayerManager(playerName, playerManager);
                        return true;
                    }
                }
                player.sendMessage("§c§l❤ §aSvadba §8» §b" + genderType.toLowerCase() + " §fnie je platné pohlavie!");
                fail(player);
                return false;
            }

            if (args[0].equalsIgnoreCase("partner")){
                String requestedInfoName = args[1];
                PlayerManager requestedInfoManager = PlayerUtil.getPlayerManager(requestedInfoName);
                if (requestedInfoManager.getPartner() == null){
                    player.sendMessage("§c§l❤ §aSvadba §8» §b" + requestedInfoName + " §fnemá partnera/ku!");
                }else{
                    player.sendMessage("§c§l❤ §aSvadba §8» §bPartner/ka §fhráča/ky §b" + requestedInfoName + " §fje §b" + requestedInfoManager.getPartner());
                }
                return true;
            }
        }
        player.openInventory(MarryMenu.getMenu(playerName));
        return true;
    }
}
