package me.bscal.hardcoremc;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import gyurix.configfile.ConfigFile;
import gyurix.mysql.MySQLDatabase;
import me.bscal.hardcoremc.basicneeds.BasicNeedsManager;
import me.bscal.hardcoremc.player.HardcorePlayer;
import me.bscal.hardcoremc.player.PlayerManager;
import me.bscal.hardcoremc.scoreboard.ScoreboardManager;
import me.bscal.hardcoremc.status.StatusManager;
import me.bscal.hardcoremc.status.listeners.BleedListener;
import me.bscal.hardcoremc.status.listeners.FractureListener;

public class App extends JavaPlugin
{
    private final static int MAX_TICKS_PER_SEC = 20;
    private final static long TICK_PERIOD = 10;

    public final static long TWICE_PER_SEC = 10 / TICK_PERIOD;
    public final static long PER_SEC = 20 / TICK_PERIOD;
    public final static long PER_THIRTY_SEC = (20 * 30) / TICK_PERIOD;

    public static App Get;
    public static boolean Debug = true;
    public static Logger Logger;
    public static ConfigFile Cfg;
    public static ConfigFile UserData;
    public static MySQLDatabase Database;

    private PlayerManager playerManager;

    private BasicNeedsManager bnm;

    private int m_currentPeriod;

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

        playerManager = new PlayerManager();

        bnm = new BasicNeedsManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(playerManager, this);
        pluginManager.registerEvents(bnm, this);
        pluginManager.registerEvents(new ScoreboardManager(), this);
        
        pluginManager.registerEvents(new BleedListener(), this);
        pluginManager.registerEvents(new FractureListener(), this);
        

        StatusManager.Init();

        if (Debug) {
            App.Logger.info("[PRINTING STATUS MAP]");
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                playerManager.PrintMap();
            }, 60, 20 * 30);
        }

        StartUpdater();
    }

    public void onDisable() {
        playerManager.SaveAllPlayers();
    }

    private void StartUpdater() {
        if (playerManager == null) Logger.severe("PlayerManager singleton is null. StartUpdater cannot work properly");

        // Update scheduler 1 time a second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(App.Get, new Runnable() {
            @Override
            public void run() {
                // Update per single manager here

                // Update per player here
                // Updating of player data is done every 20 ticks or ~second
                if (Bukkit.getCurrentTick() % MAX_TICKS_PER_SEC == 0) {
                    App.Logger.info("Updating PlayerManager | Period: " + m_currentPeriod);
                    
                    for (var pair : playerManager.GetPlayers().entrySet()) {
                        UUID uuid = pair.getKey();
                        HardcorePlayer hPlayer = pair.getValue();
                        
                        StatusManager.Update(m_currentPeriod, hPlayer);
                        bnm.Update(m_currentPeriod, uuid, hPlayer);
                    }
                    m_currentPeriod++;
                } 
            }
        }, 20L, 1L);
    }
}
