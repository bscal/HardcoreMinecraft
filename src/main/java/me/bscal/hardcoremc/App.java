package me.bscal.hardcoremc;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import gyurix.configfile.ConfigFile;
import gyurix.mysql.MySQLDatabase;
import me.bscal.hardcoremc.basicneeds.BasicNeedsManager;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin
{

    public static App Get;
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
        Bukkit.getPluginManager().registerEvents(bnm, this);
    }

    public void onDisable() {

    }
}
