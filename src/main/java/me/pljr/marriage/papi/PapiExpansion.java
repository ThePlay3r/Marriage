package me.pljr.marriage.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgLang;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Lang;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.objects.CorePlayer;
import me.pljr.pljrapi.utils.FormatUtil;
import me.pljr.pljrapi.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class PapiExpansion extends PlaceholderExpansion {

    private Marriage plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public PapiExpansion(Marriage plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "marriage";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.OfflinePlayer Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(player == null){
            return "";
        }

        UUID playerId = player.getUniqueId();
        CorePlayer corePlayer = PlayerManager.getPlayerManager(playerId);

        // %marriage_gender%
        if(identifier.equals("gender")){
            return corePlayer.getGender().toString();
        }

        // %marriage_gender_symbol%
        if(identifier.equals("gender_symbol")){
            Gender gender = corePlayer.getGender();
            switch (gender){
                case MALE: return CfgLang.lang.get(Lang.GENDER_MALE_SYMBOL);
                case NONE: return CfgLang.lang.get(Lang.GENDER_NONE_SYMBOL);
                case FEMALE: return CfgLang.lang.get(Lang.GENDER_FEMALE_SYMBOL);
            }
        }
        // %marriage_gender_color%
        if(identifier.equals("gender_color")){
            Gender gender = corePlayer.getGender();
            switch (gender){
                case MALE: return CfgLang.lang.get(Lang.GENDER_MALE_COLOR);
                case NONE: return CfgLang.lang.get(Lang.GENDER_NONE_COLOR);
                case FEMALE: return CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR);
            }
        }

        // %marriage_state%
        if (identifier.equals("state")){
            if (corePlayer.getPartner() == null){
                return CfgLang.lang.get(Lang.STATE_SINGLE);
            }
            return CfgLang.lang.get(Lang.STATE_MARRIED);
        }

        // %marriage_pvp%
        if (identifier.equals("pvp")){
            if (corePlayer.isPvp()){
                return CfgLang.lang.get(Lang.PVP_ENABLED);
            }
            return CfgLang.lang.get(Lang.PVP_DISABLED);
        }

        // %marriage_home_world%
        if (identifier.equals("home_world")){
            return corePlayer.getHome().getWorld().getName();
        }

        // %marriage_home_x%
        if (identifier.equals("home_x")){
            return corePlayer.getHome().getX()+"";
        }

        // %marriage_home_y%
        if (identifier.equals("home_y")){
            return corePlayer.getHome().getY()+"";
        }

        // %marriage_home_z%
        if (identifier.equals("home_z")){
            return corePlayer.getHome().getZ()+"";
        }

        // %marriage_home_yaw%
        if (identifier.equals("home_yaw")){
            return corePlayer.getHome().getYaw()+"";
        }

        // %marraige_home_pitch%
        if (identifier.equals("home_pitch")){
            return corePlayer.getHome().getPitch()+"";
        }

        // %marriage_partner%
        if(identifier.equals("partner")){
            String partnerName = PlayerUtil.getName(Bukkit.getOfflinePlayer(corePlayer.getPartner()));
            if (partnerName == null){
                return "";
            }
            return partnerName;
        }

        // %marriage_partner_gender%
        if(identifier.equals("partner_gender")){
            UUID partnerId = corePlayer.getPartner();
            if (partnerId == null){
                return "";
            }
            return PlayerManager.getPlayerManager(partnerId).getGender().toString();
        }

        // %marriage_partner_gender_symbol%
        if(identifier.equals("partner_gender_symbol")){
            UUID partnerId = corePlayer.getPartner();
            if (partnerId == null){
                return "";
            }
            Gender gender = PlayerManager.getPlayerManager(partnerId).getGender();
            switch (gender){
                case MALE: return CfgLang.lang.get(Lang.GENDER_MALE_SYMBOL);
                case NONE: return CfgLang.lang.get(Lang.GENDER_NONE_SYMBOL);
                case FEMALE: return CfgLang.lang.get(Lang.GENDER_FEMALE_SYMBOL);
            }
        }
        // %marriage_partner_gender_color%
        if(identifier.equals("partner_gender_color")){
            UUID partnerId = corePlayer.getPartner();
            if (partnerId == null){
                return "";
            }
            Gender gender = PlayerManager.getPlayerManager(partnerId).getGender();
            switch (gender){
                case MALE: return CfgLang.lang.get(Lang.GENDER_MALE_COLOR);
                case NONE: return CfgLang.lang.get(Lang.GENDER_NONE_COLOR);
                case FEMALE: return CfgLang.lang.get(Lang.GENDER_FEMALE_COLOR);
            }
        }

        // %marriage_partner_lastseen%
        if (identifier.equals("partner_lastseen")){
            UUID partnerId = corePlayer.getPartner();
            if (partnerId == null){
                return "";
            }
            Player partner = Bukkit.getPlayer(partnerId);
            if (partner == null || !partner.isOnline()){
                return FormatUtil.formatTime(PlayerManager.getPlayerManager(partnerId).getLastseen());
            }else{
                return CfgLang.lang.get(Lang.ONLINE);
            }
        }

        // %marriage_partner_pvp%
        if (identifier.equals("partner_pvp")){
            UUID partnerId = corePlayer.getPartner();
            if (partnerId == null){
                return "";
            }
            if (PlayerManager.getPlayerManager(partnerId).isPvp()){
                return CfgLang.lang.get(Lang.PVP_ENABLED);
            }
            return CfgLang.lang.get(Lang.PVP_DISABLED);
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }
}
