package me.bscal.hardcoremc.status;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import gyurix.configfile.ConfigFile;
import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.utils.PlayerCache;

public class StatusManager implements Listener {

    private final static long TICK_PERIOD = 20;

    private static ConfigFile statusData;

    private static Map<UUID, StatusPlayer> m_playerStatuses = new HashMap<UUID, StatusPlayer>();
    private static boolean m_isRunning = false;
    private static boolean m_isInitialized = false;

    private StatusManager() {
    }

    public static void Init() {
        m_isInitialized = true;

        statusData = new ConfigFile(new File(App.Get.getDataFolder() + File.separator + "statuses.yml"));

        StartStatusUpdater();
    }

    public static void AddStatus(UUID uuid, Status s) {
        if (!m_isInitialized) {
            App.Logger.log(Level.SEVERE, "StatusManager not initialized!");
            return;
        }

        if (!m_playerStatuses.containsKey(uuid))
            m_playerStatuses.put(uuid, new StatusPlayer(uuid));

        StatusPlayer sp = m_playerStatuses.get(uuid);
        if (sp == null) return;
        sp.AddStatus(s);
    }

    public static void RemoveStatus(UUID uuid, Status s) {
        if (!m_isInitialized) {
            App.Logger.log(Level.SEVERE, "StatusManager not initialized!");
            return;
        }

        if (!m_playerStatuses.containsKey(uuid)) return;

        StatusPlayer sp = m_playerStatuses.get(uuid);
        if (sp == null) return;
        sp.RemoveStatus(s);
    }

    public static void StartStatusUpdater() {
        if (m_isRunning)
            return;
        m_isRunning = true;

        // Update scheduler 1 time a second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(App.Get, new Runnable() {
            @Override
            public void run() {
                for (var pair : m_playerStatuses.entrySet()) {
                    // If player is offline skip. No need to remove because
                    // the player's status map is temporarily cached.
                    Player p = Bukkit.getPlayer(pair.getKey());
                    if (p == null) {
                        continue;
                    }
                    else if (!p.isOnline() && !PlayerCache.Contains(p.getUniqueId())) {
                        OnExit(p);
                    }
                    else {
                        pair.getValue().Update();
                    }
                }
            }
        }, 0, TICK_PERIOD);
    }

    public static void OnJoin(Player p) {
        UUID uuid = p.getUniqueId();
        // Check if player is cached in m_playerStatuses already.
        if (m_playerStatuses.containsKey(uuid)) {
            m_playerStatuses.get(uuid).ReapplyStatuesToPlayer();
            return;
        }

        // Adds player
        StatusPlayer sp = new StatusPlayer(uuid);
        m_playerStatuses.put(uuid, sp);
        ConfigFile sub = statusData.subConfig(uuid + ".statuses");
        sp.Load(sub);
        statusData.removeData(uuid.toString());
        statusData.save();
    }

    public static void OnExit(Player p) {
        UUID uuid = p.getUniqueId();
        PlayerCache.AddWithTimeout(uuid, () -> {
            HandleLeftPlayer(uuid, null);
        });
    }

    public static void SaveAllPlayers() {
        m_playerStatuses.forEach((k, v) -> {
            HandleLeftPlayer(k, v);
        });
    }

    private static void HandleLeftPlayer(UUID uuid, StatusPlayer sp) {
        if (sp == null) sp = m_playerStatuses.get(uuid);
        ConfigFile sub = statusData.subConfig(uuid + ".statuses");
        sp.Save(sub);
        statusData.save();
        sp.Clean();
        sp = null;
        m_playerStatuses.remove(uuid);
    }

    public static void OnDeath(UUID pid) {
        m_playerStatuses.get(pid).deadEvent = true;
    }

    public static void OnRespawn(UUID pid) {
        m_playerStatuses.get(pid).respawnEvent = true;
    }

    public static void PrintMap() {
        for (var pair : m_playerStatuses.entrySet()) {
            StatusPlayer value = pair.getValue();
            value.PrintMap();
            App.Logger.info("key, " + pair.getKey() + " value " + value);
        }
    }

}