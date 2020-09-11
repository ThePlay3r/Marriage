package me.pljr.marriage;

import me.pljr.marriage.commands.AmarryCommand;
import me.pljr.marriage.commands.MarryCommand;
import me.pljr.marriage.config.*;
import me.pljr.marriage.listeners.*;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.managers.QueryManager;
import me.pljr.marriage.menus.MarryMenu;
import me.pljr.pljrapi.PLJRApi;
import me.pljr.pljrapi.database.DataSource;
import me.pljr.pljrapi.managers.ConfigManager;
import me.pljr.pljrapi.utils.SpigotUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marriage extends JavaPlugin {
    private static PlayerManager playerManager;
    private static ConfigManager configManager;
    private static Marriage instance;
    private static QueryManager query;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupPLJRApi()) return;
        updateCheck();
        setupConfig();
        setupManagers();
        setupDatabase();
        loadListeners();
        loadCommands();
        setupPapi();
        setupBungee();
    }

    private boolean setupPLJRApi(){
        PLJRApi api = (PLJRApi) Bukkit.getServer().getPluginManager().getPlugin("PLJRApi");
        if (api == null){
            Bukkit.getConsoleSender().sendMessage("§cMarriage: PLJRApi not found, disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }else{
            Bukkit.getConsoleSender().sendMessage("§aMarriage: Hooked into PLJRApi!");
            return true;
        }
    }

    private void updateCheck(){
        Bukkit.getScheduler().runTaskAsynchronously(this, ()->{
            boolean isUpToDate = SpigotUtil.upToDate(81807, this.getDescription().getVersion());
            if (!isUpToDate){
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Marriage: Your current version is not up-to-date!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        });
    }

    private void setupBungee(){
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void setupPapi(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiExpansion(this).register();
        }
    }

    private void setupConfig(){
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        configManager = new ConfigManager(config, "§cMarriage:", "config.yml");
        CfgDefaulthome.load();
        CfgMenu.load();
        CfgLang.load();
        CfgSettings.load();
        CfgSounds.load();
    }

    private void setupManagers(){
        playerManager = new PlayerManager();
    }

    private void setupDatabase(){
        DataSource dataSource = PLJRApi.getDataSource();
        query = new QueryManager(dataSource);
        query.setupTables();
        for (Player player : Bukkit.getOnlinePlayers()){
            query.loadPlayer(player.getUniqueId());
        }
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
        getCommand("amarry").setExecutor(new AmarryCommand());
    }

    public static QueryManager getQuery() {
        return query;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
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
        for (Player player : Bukkit.getOnlinePlayers()){
            query.savePlayerSync(player.getUniqueId());
        }
    }
}
