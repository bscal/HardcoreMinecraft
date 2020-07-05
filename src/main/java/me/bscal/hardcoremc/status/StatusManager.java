package me.bscal.hardcoremc.status;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import gyurix.configfile.ConfigFile;
import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.player.HardcorePlayer;
import me.bscal.hardcoremc.player.PlayerManager;
import me.bscal.hardcoremc.utils.PlayerCache;

public class StatusManager {

    private static ConfigFile statusData;

    private static boolean m_isInitialized = false;

    /***  
     * Private class should not be constructed initialize with <code>StatusManager.Init()</code>
     * */
    private StatusManager() {
    }


    /***
     * Initializes StatusManager and starts the status updater.
     */
    public static void Init() {
        if (m_isInitialized) {
            App.Logger.log(Level.SEVERE, "StatusManager already initialized!");
            return;
        }
        m_isInitialized = true;

        statusData = new ConfigFile(new File(App.Get.getDataFolder() + File.separator + "statuses.yml"));
    }

    public static void Update(int period, HardcorePlayer hcPlayer) {
        if (period == PlayerManager.FULL_PERIOD) {
            // If player is offline skip. No need to remove because
            // the player's status map is temporarily cached.
            final Player p = hcPlayer.player;
            final boolean isOnline = p.isOnline();
            if (!isOnline && !PlayerCache.Contains(p.getUniqueId())) 
                HandleLeftPlayer(p.getUniqueId());
            else if (!isOnline)
                return;
            else
                hcPlayer.statuses.Update();
        }
    }

    /***
     * Adds a status
     * @param uuid Player UUID
     * @param s Status
     */
    public static void AddStatus(UUID uuid, Status s) {
        if (!m_isInitialized) {
            App.Logger.log(Level.SEVERE, "StatusManager not initialized!");
            return;
        }

        if (!PlayerManager.Singleton.ExistsCheck(uuid)) return;

        PlayerManager.Singleton.GetPlayer(uuid).statuses.AddStatus(s);
    }
    
    /***
     * Removes a status
     * @param uuid Player UUID
     * @param s Status
     */
    public static void RemoveStatus(UUID uuid, Status s) {
        if (!m_isInitialized) {
            App.Logger.log(Level.SEVERE, "StatusManager not initialized!");
            return;
        }

        if (!PlayerManager.Singleton.ExistsCheck(uuid)) return;

        PlayerManager.Singleton.GetPlayer(uuid).statuses.RemoveStatus(s);
    }

    // Event Functions

    public static void OnJoin(HardcorePlayer p, boolean firstJoin) {
        App.Logger.info("loading statuses...");

        if (firstJoin) {
            ConfigFile sub = statusData.subConfig(p.uuid + ".statuses");
            p.statuses.Load(sub);
        }
        else {
            p.statuses.ReapplyStatuesToPlayer();
        }
        
        // Removes old statuses
        statusData.removeData(p.uuid.toString());
        statusData.save();
    }

    public static void OnExit(HardcorePlayer p) {
        App.Logger.info("Saving statuses...");
        HandleLeftPlayer(p.uuid);
    }
    
    public static void OnDeath(HardcorePlayer p) {
        p.statuses.deadEvent = true;
    }
    
    public static void OnRespawn(HardcorePlayer p) {
        p.statuses.deadEvent = true;
    }

    // Private functions

    public static void HandleLeftPlayer(UUID uuid) {
        HandleLeftPlayer(uuid, PlayerManager.Singleton.GetPlayer(uuid).statuses);
    }

    public static void HandleLeftPlayer(UUID uuid, StatusPlayer sp) {
        ConfigFile sub = statusData.subConfig(uuid + ".statuses");
        sp.Save(sub);
        statusData.save();
    }

}