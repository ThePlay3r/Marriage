package me.pljr.marriage.config;

import me.pljr.pljrapispigot.builders.ItemBuilder;
import me.pljr.pljrapispigot.managers.ConfigManager;
import me.pljr.pljrapispigot.xseries.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum MenuItem {
    PLAYER_HEAD(new ItemBuilder(XMaterial.PLAYER_HEAD).withName("&e{name}").withLore("&8» &fClick for divorce.").create()),
    MARRY_HEART(new ItemBuilder(XMaterial.RED_STAINED_GLASS).withName("&f").create()),
    MARRY_WHITE(new ItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE).withName("&f").create()),
    MARRY_BACKGROUND(new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE).withName("&f").create()),
    MARRY_LAST_SEEN(new ItemBuilder(XMaterial.REDSTONE_LAMP).withName("&eLast seen").withLore("&8» &fClick to find out,", " &fwhen your partner was", " &flast seen on the server.").create()),
    MARRY_SHARED_XP(new ItemBuilder(XMaterial.EXPERIENCE_BOTTLE).withName("&eShared Experience").withLore("&8» &fGained XP are halved", " &fand shared with your partner.").create()),
    MARRY_TP_HOME(new ItemBuilder(XMaterial.RED_BED).withName("&eHome Teleportation").withLore("&8» &fClick to be teleported to", " &fyour shared home.").create()),
    MARRY_GIFT(new ItemBuilder(XMaterial.CHEST).withName("&eGift").withLore("&8» &fClick to surprise your", " &fpartner with a lovely gift!", " &7(Will send your held item to", " &7your partner)").create()),
    MARRY_TP(new ItemBuilder(XMaterial.ENDER_PEARL).withName("&eTeleportation").withLore("&8» &fClick to be teleported to", " &fyour partner.").create()),
    MARRY_PVP(new ItemBuilder(XMaterial.DIAMOND_SWORD).withName("&ePvP").withLore("&8» &fClick to turn ON/OFF your", " &fPvP with partner.").create()),
    MARRY_SET_HOME(new ItemBuilder(XMaterial.OAK_DOOR).withName("&eSet Home").withLore("&8» &fClick to set your shared home", " &fto your current location.").create()),
    MARRY_SHARED_FOOD(new ItemBuilder(XMaterial.COOKED_PORKCHOP).withName("&eShared Food").withLore("&8» &fGained food is halved", " &fand shared with your partner.").create()),
    MARRY_PRIVATE_CHAT(new ItemBuilder(XMaterial.PAPER).withName("&eShared Chat").withLore("&8» &fClick to start writing", " &fto your shared chat.").create());

    private static HashMap<MenuItem, ItemStack> menuItems;
    private final ItemStack defaultValue;

    MenuItem(ItemStack defaultValue){
        this.defaultValue = defaultValue;
    }

    public static void load(ConfigManager config){
        menuItems = new HashMap<>();
        FileConfiguration fileConfig = config.getConfig();
        for (MenuItem menuItem : values()){
            if (!fileConfig.isSet(menuItem.toString())){
                config.setSimpleItemStack(menuItem.toString(), menuItem.defaultValue);
            }
            menuItems.put(menuItem, config.getSimpleItemStack(menuItem.toString()));
        }
        config.save();
    }

    public ItemStack get(){
        return menuItems.getOrDefault(this, defaultValue);
    }
}
