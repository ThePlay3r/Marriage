package me.pljr.marriage;

import me.pljr.marriage.commands.AMarryCommand;
import me.pljr.marriage.commands.MarryCommand;
import me.pljr.marriage.config.*;
import me.pljr.marriage.listeners.*;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.managers.QueryManager;
import me.pljr.pljrapispigot.database.DataSource;
import me.pljr.pljrapispigot.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marriage extends JavaPlugin {
    private static Marriage instance;

    private static ConfigManager configManager;

    private static PlayerManager playerManager;
    private static QueryManager queryManager;

    @Override
    public void onEnable() {
        instance = this;
        setupConfig();
        setupDatabase();
        setupManagers();
        setupListeners();
        setupCommands();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiExpansion(this, playerManager).register();
        }
    }

    public void setupConfig(){
        saveDefaultConfig();
        saveConfig();
        configManager = new ConfigManager(this, "config.yml");
        CfgSettings.load(configManager);
        Lang.load(new ConfigManager(this, "lang.yml"));
        MenuItem.load(new ConfigManager(this, "menus.yml"));
        SoundType.load(new ConfigManager(this, "sounds.yml"));
        TitleType.load(new ConfigManager(this, "titles.yml"));
        ActionBarType.load(new ConfigManager(this, "actionbars.yml"));
        Gender.load(new ConfigManager(this, "genders.yml"));
    }

    private void setupDatabase(){
        DataSource dataSource = DataSource.getFromConfig(configManager);
        queryManager = new QueryManager(this, dataSource);
    }

    private void setupManagers(){
        playerManager = new PlayerManager(queryManager);
    }

    private void setupListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(playerManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(playerManager), this);
        pluginManager.registerEvents(new KissListeners(this, playerManager), this);
        pluginManager.registerEvents(new PvPListeners(playerManager), this);
        pluginManager.registerEvents(new SharingListeners(playerManager), this);
    }

    private void setupCommands(){
        new MarryCommand(this, playerManager).registerCommand(this);
        new AMarryCommand(playerManager).registerCommand(this);
    }

    public static Marriage getInstance() {
        return instance;
    }
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
