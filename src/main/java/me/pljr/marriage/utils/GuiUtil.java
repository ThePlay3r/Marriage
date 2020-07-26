package me.pljr.marriage.utils;

import me.pljr.marriage.config.CfgMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class GuiUtil {
    public static ItemStack createItem(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, List<String> lore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createHead(String owner, String name, String... lore){
        if (CfgMenu.oldHead){
            ItemStack skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(owner);
            skullMeta.setDisplayName(name);
            skullMeta.setLore(Arrays.asList(lore));
            skull.setItemMeta(skullMeta);
            return skull;
        }
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(owner);
        skullMeta.setDisplayName(name);
        skullMeta.setLore(Arrays.asList(lore));
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack createHead(String owner, String name, List<String> lore){
        if (CfgMenu.oldHead){
            ItemStack skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(owner);
            skullMeta.setDisplayName(name);
            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);
            return skull;
        }
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(owner);
        skullMeta.setDisplayName(name);
        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
