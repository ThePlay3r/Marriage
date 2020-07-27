package me.pljr.marriage.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pljr.marriage.Marriage;
import me.pljr.marriage.config.CfgMessages;
import me.pljr.marriage.enums.Gender;
import me.pljr.marriage.enums.Message;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.utils.FormatUtil;
import me.pljr.marriage.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

        String playerName = player.getName();
        PlayerManager playerManager = PlayerUtil.getPlayerManager(playerName);

        // %marriage_gender%
        if(identifier.equals("gender")){
            return playerManager.getGender().toString();
        }

        // %marriage_gender_symbol%
        if(identifier.equals("gender_symbol")){
            Gender gender = playerManager.getGender();
            switch (gender){
                case MALE: return CfgMessages.messages.get(Message.GENDER_MALE_SYMBOL);
                case NONE: return CfgMessages.messages.get(Message.GENDER_NONE_SYMBOL);
                case FEMALE: return CfgMessages.messages.get(Message.GENDER_FEMALE_SYMBOL);
            }
        }
        // %marriage_gender_color%
        if(identifier.equals("gender_color")){
            Gender gender = playerManager.getGender();
            switch (gender){
                case MALE: return CfgMessages.messages.get(Message.GENDER_MALE_COLOR);
                case NONE: return CfgMessages.messages.get(Message.GENDER_NONE_COLOR);
                case FEMALE: return CfgMessages.messages.get(Message.GENDER_FEMALE_COLOR);
            }
        }

        // %marriage_state%
        if (identifier.equals("state")){
            if (playerManager.getPartner() == null){
                return CfgMessages.messages.get(Message.STATE_SINGLE);
            }
            return CfgMessages.messages.get(Message.STATE_MARRIED);
        }

        // %marriage_pvp%
        if (identifier.equals("pvp")){
            if (playerManager.isPvp()){
                return CfgMessages.messages.get(Message.PVP_ENABLED);
            }
            return CfgMessages.messages.get(Message.PVP_DISABLED);
        }

        // %marriage_home_world%
        if (identifier.equals("home_world")){
            return playerManager.getHome().getWorld().getName();
        }

        // %marriage_home_x%
        if (identifier.equals("home_x")){
            return playerManager.getHome().getX()+"";
        }

        // %marriage_home_y%
        if (identifier.equals("home_y")){
            return playerManager.getHome().getY()+"";
        }

        // %marriage_home_z%
        if (identifier.equals("home_z")){
            return playerManager.getHome().getZ()+"";
        }

        // %marriage_home_yaw%
        if (identifier.equals("home_yaw")){
            return playerManager.getHome().getYaw()+"";
        }

        // %marraige_home_pitch%
        if (identifier.equals("home_pitch")){
            return playerManager.getHome().getPitch()+"";
        }

        // %marriage_partner%
        if(identifier.equals("partner")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            return partnerName;
        }

        // %marriage_partner_gender%
        if(identifier.equals("partner_gender")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            return PlayerUtil.getPlayerManager(partnerName).getGender().toString();
        }

        // %marriage_partner_gender_symbol%
        if(identifier.equals("partner_gender_symbol")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            Gender gender = PlayerUtil.getPlayerManager(partnerName).getGender();
            switch (gender){
                case MALE: return CfgMessages.messages.get(Message.GENDER_MALE_SYMBOL);
                case NONE: return CfgMessages.messages.get(Message.GENDER_NONE_SYMBOL);
                case FEMALE: return CfgMessages.messages.get(Message.GENDER_FEMALE_SYMBOL);
            }
        }
        // %marriage_partner_gender_color%
        if(identifier.equals("partner_gender_color")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            Gender gender = PlayerUtil.getPlayerManager(partnerName).getGender();
            switch (gender){
                case MALE: return CfgMessages.messages.get(Message.GENDER_MALE_COLOR);
                case NONE: return CfgMessages.messages.get(Message.GENDER_NONE_COLOR);
                case FEMALE: return CfgMessages.messages.get(Message.GENDER_FEMALE_COLOR);
            }
        }

        // %marriage_partner_lastseen%
        if (identifier.equals("partner_lastseen")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            Player partner = Bukkit.getPlayer(partnerName);
            if (partner == null || !partner.isOnline()){
                return FormatUtil.formatTime(PlayerUtil.getPlayerManager(partnerName).getLastseen());
            }else{
                return CfgMessages.messages.get(Message.ONLINE);
            }
        }

        // %marriage_partner_pvp%
        if (identifier.equals("partner_pvp")){
            String partnerName = playerManager.getPartner();
            if (partnerName == null){
                return "";
            }
            if (PlayerUtil.getPlayerManager(partnerName).isPvp()){
                return CfgMessages.messages.get(Message.PVP_ENABLED);
            }
            return CfgMessages.messages.get(Message.PVP_DISABLED);
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }
}
