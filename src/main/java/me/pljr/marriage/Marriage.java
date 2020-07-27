package me.pljr.marriage;

import me.pljr.marriage.commands.MarryCommand;
import me.pljr.marriage.config.*;
import me.pljr.marriage.database.DataSource;
import me.pljr.marriage.database.QueryManager;
import me.pljr.marriage.listeners.*;
import me.pljr.marriage.managers.ConfigManager;
import me.pljr.marriage.menus.MarryMenu;
import me.pljr.marriage.papi.PapiExpansion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class Marriage extends JavaPlugin {
    private static FileConfiguration config;
    private static ConfigManager configManager;
    private static Marriage instance;
    private static QueryManager query;
    private static Economy econ = null;
    private static DataSource dataSource;
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        instance = this;
        setupConfig();
        setupDatabase();
        loadListeners();
        setupVault();
        loadCommands();
        setupPapi();
    }

    private void setupVault(){
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void setupPapi(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiExpansion(this).register();
        }
    }

    private void setupConfig(){
        saveDefaultConfig();
        createResource("translations/config-SK.yml", false);
        createResource("translations/readme.txt", false);
        config = getConfig();
        configManager = new ConfigManager(config);
        CfgDefaulthome.load();
        CfgMenu.load();
        CfgMessages.load();
        CfgOptions.load();
        CfgSounds.load();
    }

    private void createResource(String resourcePath, boolean replace){
        File file = new File(resourcePath);
        if (file.exists()) return;
        saveResource(resourcePath, replace);
    }

    private void setupDatabase(){
        dataSource = new DataSource();
        dataSource.load();
        dataSource.initPool();
        query = new QueryManager(dataSource);
        query.setupTables();
    }

    private void loadListeners(){
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new MarryMenu(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(), this);
    }

    private void loadCommands(){
        getCommand("marry").setExecutor(new MarryCommand());
    }

    public static Economy getEconomy() {
        return econ;
    }
    public static QueryManager getQuery() {
        return query;
    }
    public static FileConfiguration getConf() {
        return config;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static Marriage getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
