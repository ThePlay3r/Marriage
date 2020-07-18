package me.pljr.marriage.managers;

import me.pljr.marriage.utils.FormatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final FileConfiguration config;
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    private void pathNotFound(String path){
        console.sendMessage("§cDCCore+: Path " + path + " was not found in " + config.toString());
    }

    private void isNotSound(String sound, String path){
        console.sendMessage("§cDCCore+: " + sound + " is not a sound on " + path +" in " + config.toString());
    }

    private void isNotInt(String integer, String path){
        console.sendMessage("§cDCCore+: " + integer + " is not an int on " + path + " in " + config.toString());
    }

    private void isNotBoolean(String bool, String path){
        console.sendMessage("§cDCCore+: " + bool + " is not a boolean on " + path +" in " + config.toString());
    }

    private void isNotStringlist(String path){
        console.sendMessage("§cDCCore+: Couldn't find a String List on " + path +" in " + config.toString());
    }

    private void isNotEntityType(String entity, String path){
        console.sendMessage("§cDCCore+: " + entity + "is not an entity on " + path + " in " + config.toString());
    }

    private void isNotMaterial(String material, String path){
        console.sendMessage("§cDCCore+: " + material + " is not a material on " + path + " in " + config.toString());
    }

    private void isNotDamageCause(String cause, String path){
        console.sendMessage("§cDCCore+: " + cause + "is not a damage cause on " + path + " in " + config.toString());
    }

    public ConfigManager(FileConfiguration config){
        this.config = config;
    }

    public String getString(String path){
        if (config.isSet(path)){
            return FormatUtil.colorString(config.getString(path));
        }else{
            pathNotFound(path);
            return ChatColor.RED + path;
        }
    }

    public Sound getSound(String path){
        if (config.isSet(path)){
            String soundName = config.getString(path);

            for (Sound sound : Sound.values()){
                if (sound.toString().equalsIgnoreCase(soundName)){
                    return sound;
                }
            }

            isNotSound(soundName, path);
            return Sound.ENTITY_VILLAGER_NO;
        }

        pathNotFound(path);
        return Sound.ENTITY_VILLAGER_NO;
    }

    public int getInt(String path){
        if (config.isSet(path)){
            if (config.isInt(path)){
                return config.getInt(path);
            }

            isNotInt(config.getString(path), path);
            return 1;
        }

        pathNotFound(path);
        return 1;
    }

    public boolean getBoolean(String path){
        if (config.isSet(path)){
            if (config.isBoolean(path)){
                return config.getBoolean(path);
            }

            isNotBoolean(config.getString(path), path);
            return false;
        }

        pathNotFound(path);
        return false;
    }

    public List<String> getStringList(String path){
        if (config.isSet(path)){
            if (!(config.getStringList(path) == null) || config.getStringList(path).size() == 0){
                List<String> stringList = config.getStringList(path);
                List<String> coloredStringList = new ArrayList<>();

                for (String string : stringList){
                    coloredStringList.add(FormatUtil.colorString(string));
                }

                return coloredStringList;
            }

            isNotStringlist(path);
            return new ArrayList<>();
        }

        pathNotFound(path);
        return new ArrayList<>();
    }

    public EntityType getEntityType(String path){
        if (config.isSet(path)){
            String entityName = config.getString(path);

            for (EntityType entity : EntityType.values()){
                if (entity.toString().equalsIgnoreCase(entityName)){
                    return entity;
                }
            }

            isNotEntityType(entityName, path);
            return EntityType.PIG;
        }

        pathNotFound(path);
        return EntityType.PIG;
    }

    public Material getMaterial(String path){
        if (config.isSet(path)){
            String materialName = config.getString(path);

            for (Material material : Material.values()){
                if (material.toString().equalsIgnoreCase(materialName)){
                    return material;
                }
            }

            isNotMaterial(materialName, path);
            return Material.STONE;
        }

        pathNotFound(path);
        return Material.STONE;
    }

    public ItemStack getItemStack(String path){
        if (config.isSet(path)){

            Material type = getMaterial(path+".type");
            String name = getString(path+".name");
            int amount = getInt(path+".amount");
            List<String> lore = getStringList(path+".lore");

            ItemStack itemStack = new ItemStack(type, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            return itemStack;
        }

        pathNotFound(path);
        return new ItemStack(Material.STONE);
    }

    public EntityDamageEvent.DamageCause getDamageCause(String path){
        if (config.isSet(path)){
            String causeName = config.getString(path);

            for (EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()){
                if (cause.toString().equalsIgnoreCase(causeName)){
                    return cause;
                }
            }

            isNotDamageCause(causeName, path);
            return EntityDamageEvent.DamageCause.VOID;
        }

        pathNotFound(path);
        return EntityDamageEvent.DamageCause.VOID;
    }
}
