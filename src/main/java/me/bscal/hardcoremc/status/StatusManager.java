package me.bscal.hardcoremc.status;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import gyurix.configfile.ConfigFile;
import me.bscal.hardcoremc.App;

public class StatusManager implements Listener {

    private final static long TICK_PERIOD = 20;

    private static ConfigFile statusData;

    private static Map<String, Status> m_statusByName = new HashMap<String, Status>();

    // Cache of statuses in-game
    private static Map<StatusType, List<String>> m_statusMap = new HashMap<StatusType, List<String>>();

    private static Map<UUID, HashMap<String, Status>> m_playerStatuses = new HashMap<UUID, HashMap<String, Status>>();
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
        HashMap<String, Status> map = m_playerStatuses.get(uuid);

        // Caches Status effects by type
        // Only applied when a status is activated
        if (!m_statusMap.containsKey(s.type)) {
            m_statusMap.put(s.type, new ArrayList<String>());
        } else {
            if (!m_statusMap.get(s.type).contains(s.toString())) {
                m_statusMap.get(s.type).add(s.toString());
            }
        }

        if (map == null) {
            m_playerStatuses.put(uuid, new HashMap<String, Status>());
            map = m_playerStatuses.get(uuid);
        }

        if (map.containsKey(s.toString())) {
            map.get(s.toString()).OnRefresh(s);
        } else {
            map.put(s.toString(), s);
            s.OnStart();
        }
    }

    public static void RemoveStatus(UUID uuid, Status s) {
        HashMap<String, Status> map = m_playerStatuses.get(uuid);

        if (map.containsKey(s.toString())) {
            map.remove(s.toString());
        }

        if (map.isEmpty()) {
            m_playerStatuses.remove(uuid);
        }
    }

    public static void StartStatusUpdater() {
        if (m_isRunning)
            return;
        m_isRunning = true;

        // Update scheduler 1 time a second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(App.Get, new Runnable() {
            @Override
            public void run() {
                for (HashMap<String, Status> map : m_playerStatuses.values()) {
                    for (Status s : map.values()) {
                        // If TickDuration returns true because duration was 0
                        // remove status from nested map and if player map is empty remove player map
                        if (s.TickDuration()) {
                            map.remove(s.toString());
                            if (map.isEmpty())
                                m_playerStatuses.remove(s.playerUUID);
                        }
                        // Updates status
                        s.OnUpdate();
                    }
                }
            }
        }, 0, TICK_PERIOD);
    }

    public static Status BuildStatus(String name, int duration) {
        Status s = new Status(m_statusByName.get(name));
        s.name = name;
        s.duration = duration;
        return s;
    }

    public static void OnJoin(Player p) {
        ConfigFile sub = statusData.subConfig(p.getUniqueId() + ".statuses");
        for (String key : sub.getStringKeyList()) {
            String name = sub.getString(key + ".name");
            int dur = sub.getInt(key + ".duration");
            App.Logger.info(key);

            Status s = BuildStatus(name, dur);
            App.Logger.info("[[BUILDSTATUS]] " + s.name);
        }
    }

    public static void OnExit(Player p) {
        ConfigFile sub = statusData.subConfig(p.getUniqueId() + ".statuses");

        for (Status s : m_playerStatuses.get(p.getUniqueId()).values()) {
            sub.setObject(String.format("%s.name", s.toString()), s.toString());
            sub.setObject(String.format("%s.duration", s.toString()), s.duration);
        }
        statusData.save();
    }

    public static void OnDeath(Player p) {
        for (Status s : m_playerStatuses.get(p.getUniqueId()).values()) {
            if (s.removeOnDeath) {
                RemoveStatus(p.getUniqueId(), s);
            }
        }
    }

}