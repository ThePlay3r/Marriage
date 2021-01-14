package me.pljr.marriage;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pljr.marriage.config.Lang;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.MarriagePlayer;
import me.pljr.pljrapispigot.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class PapiExpansion extends PlaceholderExpansion {
    private final Marriage plugin;
    private final PlayerManager playerManager;

    public PapiExpansion(Marriage plugin, PlayerManager playerManager){
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return "9hx";
    }

    @Override
    public String getIdentifier(){
        return "marriage";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        return onOfflinePlaceholderRequest(player, identifier);
    }

    public String onOfflinePlaceholderRequest(OfflinePlayer player, String identifier){
        if(player == null){
            return "No player specified!";
        }

        MarriagePlayer marriagePlayer = playerManager.getPlayer(player.getUniqueId());

        if (identifier.startsWith("partner_")){
            if (marriagePlayer.getPartnerID() == null) return "";
            OfflinePlayer offlinePartner = Bukkit.getOfflinePlayer(marriagePlayer.getPartnerID());
            return onOfflinePlaceholderRequest(offlinePartner, identifier.replace("partner_", ""));
        }

        switch (identifier){
            case "gender": return marriagePlayer.getGender().getName();
            case "gender_symbol": return marriagePlayer.getGender().getSymbol();
            case "gender_color": return marriagePlayer.getGender().getColor();
            case "state":
                if (marriagePlayer.getPartnerID() == null) return Lang.NOT_MARRIED.get();
                return Lang.MARRIED.get();
            case "pvp":
                if (marriagePlayer.isPvp()) return Lang.ACTIVE.get();
                return Lang.INACTIVE.get();
            case "food":
                if (marriagePlayer.isSharedFood()) return Lang.ACTIVE.get();
                return Lang.INACTIVE.get();
            case "xp":
                if (marriagePlayer.isSharedXP()) return Lang.ACTIVE.get();
                return Lang.INACTIVE.get();
            case "home_world":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getWorld().getName();
            case "home_x":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getX()+"";
            case "home_y":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getY()+"";
            case "home_z":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getZ()+"";
            case "home_yaw":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getYaw()+"";
            case "home_pitch":
                if (marriagePlayer.getHome() == null) return "";
                return marriagePlayer.getHome().getPitch()+"";
            case "partner":
                if (marriagePlayer.getPartnerID() == null) return "";
                return PlayerUtil.getName(marriagePlayer.getPartnerID());
        }

        return "";
    }
}
