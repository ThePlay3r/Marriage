package me.pljr.marriage;

import me.pljr.marriage.commands.AMarryCommand;
import me.pljr.marriage.commands.MarryCommand;
import me.pljr.marriage.config.*;
import me.pljr.marriage.listeners.*;
import me.pljr.marriage.managers.MarriageManager;
import me.pljr.marriage.managers.PlayerManager;
import me.pljr.marriage.managers.QueryManager;
import me.pljr.pljrapispigot.PLJRApiSpigot;
import me.pljr.pljrapispigot.database.DataSource;
import me.pljr.pljrapispigot.managers.ConfigManager;
import me.pljr.pljrapispigot.utils.BStatsUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Marriage extends JavaPlugin {
    private static final int BSTATS_ID = 10454;

    public static Logger log;
    private PLJRApiSpigot pljrApiSpigot;

    private ConfigManager configManager;
    private Settings settings;

    private PlayerManager playerManager;
    private QueryManager queryManager;
    private MarriageManager marriageManager;

    @Override
    public void onEnable() {
        BStatsUtil.addMetrics(this, BSTATS_ID);
        log = getLogger();
        if (!setupPLJRApi()) return;
        setupConfig();
        setupDatabase();
        setupManagers();
        setupListeners();
        setupCommands();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiExpansion(this, playerManager).register();
        }
    }

    public boolean setupPLJRApi(){
        if (PLJRApiSpigot.get() == null){
            getLogger().warning("PLJRApi-Spigot is not enabled!");
            return false;
        }
        pljrApiSpigot = PLJRApiSpigot.get();
        return true;
    }

    public void setupConfig(){
        saveDefaultConfig();
        saveConfig();
        configManager = new ConfigManager(this, "config.yml");
        settings = new Settings(configManager);
        Lang.load(new ConfigManager(this, "lang.yml"));
        MenuItem.load(new ConfigManager(this, "menus.yml"));
        SoundType.load(new ConfigManager(this, "sounds.yml"));
        TitleType.load(new ConfigManager(this, "titles.yml"));
        ActionBarType.load(new ConfigManager(this, "actionbars.yml"));
        Gender.load(new ConfigManager(this, "genders.yml"));
    }

    private void setupDatabase(){
        DataSource dataSource = pljrApiSpigot.getDataSource(configManager);
        dataSource.initPool("Marriage-Pool");
        queryManager = new QueryManager(dataSource);
    }

    private void setupManagers(){
        playerManager = new PlayerManager(this, queryManager, settings.isCachePlayers());
        marriageManager = new MarriageManager(this, playerManager);
    }

    private void setupListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(playerManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(playerManager), this);
        pluginManager.registerEvents(new KissListeners(this, playerManager, settings), this);
        pluginManager.registerEvents(new PvPListeners(playerManager), this);
        pluginManager.registerEvents(new SharingListeners(playerManager), this);
    }

    private void setupCommands(){
        new MarryCommand(this, playerManager, marriageManager, settings).registerCommand(this);
        new AMarryCommand(this, playerManager, marriageManager, settings).registerCommand(this);
    }
}
