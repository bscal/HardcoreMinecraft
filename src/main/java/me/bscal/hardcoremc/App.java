package me.bscal.hardcoremc;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import gyurix.configfile.ConfigFile;
import gyurix.mysql.MySQLDatabase;
import me.bscal.hardcoremc.basicneeds.BasicNeedsManager;
import me.bscal.hardcoremc.scoreboard.ScoreboardManager;
import me.bscal.hardcoremc.status.StatusManager;
import me.bscal.hardcoremc.status.listeners.BleedListener;
import me.bscal.hardcoremc.status.listeners.FractureListener;
import me.bscal.hardcoremc.status.listeners.StatusListener;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin implements Listener
{

    public static App Get;
    public static boolean Debug = true;
    public static Logger Logger;
    public static ConfigFile Cfg;
    public static ConfigFile UserData;
    public static MySQLDatabase Database;

    private BasicNeedsManager bnm;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
    }

    public void onEnable() {
        Get = this;
        Logger = Bukkit.getLogger();
        saveDefaultConfig();
        Logger.info(getDataFolder() + File.separator + "config.yml");
        Cfg = new ConfigFile(new File(getDataFolder() + File.separator + "config.yml"));
        UserData = new ConfigFile(new File(getDataFolder() + File.separator + "user_data.yml"));

        bnm = new BasicNeedsManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(bnm, this);
        pluginManager.registerEvents(new ScoreboardManager(), this);
        pluginManager.registerEvents(new StatusListener(), this);
        
        pluginManager.registerEvents(new BleedListener(), this);
        pluginManager.registerEvents(new FractureListener(), this);
        

        StatusManager.Init();
        StatusManager.StartStatusUpdater();

        if (Debug) {
            App.Logger.info("[PRINTING STATUS MAP]");
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                StatusManager.PrintMap();
            }, 60, 20 * 30);
        }
    }

    public void onDisable() {
        StatusManager.SaveAllPlayers();
    }
}
