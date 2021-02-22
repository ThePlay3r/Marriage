package me.pljr.marriage.config;

import me.pljr.pljrapispigot.managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum Lang {
    HELP("" +
            "\n&a&lMarriage Help" +
            "\n" +
            "\n&e/marry <player> &8» &fSends/Accepts a marriage request." +
            "\n&e/marry seen &8» &fChecks when your partner was last connected." +
            "\n&e/marry tp &8» &fTeleportation to your partner." +
            "\n&e/marry gift &8» &fGives currently held item to your partner." +
            "\n&e/marry sethome &8» &fSets your location as the current shared home." +
            "\n&e/marry home &8» &fTeleportation to your shared home." +
            "\n&e/marry chat <message> &8» &fSends message to shared private chat." +
            "\n&e/marry pvp &8» &fToggles your PvP with partner." +
            "\n&e/marry xp &8» &fToggles your XP Sharing with partner." +
            "\n&e/marry food &8» &fToggles your Food Sharing with partner." +
            "\n&e/marry list [page] &8» &fList of all married couples." +
            "\n&eStatus: &f%marriage_state%" +
            "\n"),

    ADMIN_HELP("" +
            "\n&a&lMarriage Admin-Help" +
            "\n" +
            "\n&e/amarry spy &8» &fToggles displaying private chats of other players." +
            "\n&e/amarry marry <player> <player> &8» &fForces players to marry each other. &7(With no cost)" +
            "\n&e/amarry unmarry <player> &8» &fForces players to divorce. &7(With no cost)" +
            "\n&e/amarry sethome <player> &8» &fSets location as the current shared home for player." +
            "\n&e/amarry home <player> &8» &fTeleportation to player's shared home." +
            "\n&e/amarry pvp <player> &8» &fToggles player's PvP with partner." +
            "\n&e/amarry food <player> &8» &fToggles player's Food Sharing with partner." +
            "\n&e/amarry xp <player> &8» &fToggles player's XP Sharing with partner." +
            "\n&e/amarry reload &8» &fReloads the configuration. &7(Not recommended)" +
            "\n"),

    MARRY_MENU_TITLE("&8&lMarriage"),
    RELOAD("&aMarriage Configuration has been reloaded."),
    MARRIED("Married"),
    NOT_MARRIED("Single"),
    ACTIVE("&aActive"),
    INACTIVE("&cInactive"),
    DISABLED("&c&l❤ &aMarriage &8» &fThis feature is disabled!"),
    LIST_HEADER("&c&l❤ &aMarriage &8» &fList of online couples:"),
    LIST_FORMAT("&7- {player} &f+ {partner}"),
    GENDER_CHANGE("&c&l❤ &aMarriage &8» &fGender has been changed to {gender}&f."),
    GENDER_CHANGE_ERROR("&c&l❤ &aMarriage &8» &fCould not change gender to {gender}."),
    CHAT_FORMAT("&4&l❤&c&l-Chat &a{name} &8» &e{message}"),
    CHAT_USAGE("&c&l❤ &aMarriage &8» &bUsage: &f/marry c <text>"),
    CHAT_SPY("&7❤-Chat &c{name} &7» &c{message}"),
    MARRY_BROADCAST("&c&l❤ &aMarriage &8» &b{player} &fand &b{partner} &fare now married."),
    MARRY_REQUEST("&c&l❤ &aMarriage &8» &fSuccessfully send a marriage request to {name}."),
    MARRY_REQUEST_PARTNER("&c&l❤ &aMarriage &8» &fYou received a marriage request from {name}." +
            "\n&7You can accept it, using &b/marry {name}&7."),
    UNMARRY_BROADCAST("&c&l❤ &aMarriage &8» &b{player} &fand &b{partner} &fhave been divorced."),
    FOOD_TOGGLE("&c&l❤ &aMarriage &8» &fFood sharing is now {state}&f."),
    FOOD_TOGGLE_ADMIN("&c&l❤ &aMarriage &8» &fFood sharing has been {state} &ffor {name}."),
    XP_TOGGLE("&c&l❤ &aMarriage &8» &fXP sharing is now {state}&f."),
    XP_TOGGLE_ADMIN("&c&l❤ &aMarriage &8» &fXP sharing has been {state} &ffor {name}."),
    PVP_TOGGLE("&c&l❤ &aMarriage &8» &fPvP is now {state}&f."),
    PVP_TOGGLE_ADMIN("&c&l❤ &aMarriage &8» &fPvP is now {state} &ffor {name}."),
    SPY_TOGGLE("&c&l❤ &aMarriage &8» &fSpying is now {state}&f."),
    HAVE_PARTNER("&c&l❤ &aMarriage &8» &fYou already &bhave &fa &bpartner&f."),
    HAS_PARTNER("&c&l❤ &aMarriage &8» &b{name} &falready has a partner."),
    NO_HELD_ITEM("&c&l❤ &aMarriage &8» &fYou don't have any item in main hand!"),
    GIFT("&c&l❤ &aMarriage &8» &fYou send a gift to your partner."),
    GIFT_PARTNER("&c&l❤ &aMarriage &8» &fYou received a gift from your partner."),
    NO_HOME("&c&l❤ &aMarriage &8» &fYou don't have a shared home."),
    NO_HOME_PLAYER("&c&l❤ &aMarriage &8» &f{name} does not have a shared home."),
    TP_HOME("&c&l❤ &aMarriage &8» &fYou have been teleported to your shared home."),
    TP_HOME_PARTNER("&c&l❤ &aMarriage &8» &fYour partner has teleported to your home."),
    TP_HOME_ADMIN("&c&l❤ &aMarriage &8» &fTeleported to a shared home of {name}."),
    SET_HOME("&c&l❤ &aMarriage &8» &fYou successfully set a shared home."),
    SET_HOME_PARTNER("&c&l❤ &aMarriage &8» &fYour partner has set a shared home."),
    SET_HOME_ADMIN("&c&l❤ &aMarriage &8» &fYou set a shared home of {name}."),
    LAST_SEEN("&c&l❤ &aMarriage &8» &fYour partner has been last seen {time} &fago."),
    TP("&c&l❤ &aMarriage &8» &fYou have successfully teleported to your shared home."),
    TP_PARTNER("&c&l❤ &aMarriage &8» &fYour partner has teleported to your shared home."),
    PARTNER_OFFLINE("&c&l❤ &aMarriage &8» &fYour partner is offline!"),
    NO_PARTNER_PLAYER("&c&l❤ &aMarriage &8» &b{name} &fdoes not have a partner."),
    NO_PARTNER("&c&l❤ &aMarriage &8» &fYou don't have a &bpartner&f!");

    public static HashMap<Lang, String> lang;
    private final String defaultValue;

    Lang(String defaultValue){
        this.defaultValue = defaultValue;
    }

    public static void load(ConfigManager config){
        lang = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (Lang lang : values()){
            if (!fileConfig.isSet(lang.toString())){
                fileConfig.set(lang.toString(), lang.getDefault());
            }
            Lang.lang.put(lang, config.getString(lang.toString()));
        }
        config.save();
    }

    public String get(){
        return lang.get(this);
    }

    public String getDefault(){
        return defaultValue;
    }
}
