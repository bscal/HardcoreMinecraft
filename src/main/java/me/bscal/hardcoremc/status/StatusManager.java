package me.bscal.hardcoremc.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.bscal.hardcoremc.App;

public class StatusManager {
    
    private final static long TICK_PERIOD = 20;

    // Cache of statuses in-game
    private static Map<StatusType, List<String>> m_statusMap = new HashMap<StatusType, List<String>>();
    
    private static Map<UUID, HashMap<String, Status>> m_playerStatuses = new HashMap<UUID, HashMap<String, Status>>();
    private static boolean m_isRunning = false;

    private StatusManager() {}

    public static void AddStatus(UUID uuid, Status s) {
        HashMap<String, Status> map = m_playerStatuses.get(uuid);

        // Caches Status effects by type
        // Only applied when a status is activated
        if (!m_statusMap.containsKey(s.type)) {
            m_statusMap.put(s.type, new ArrayList<String>());
        }
        else {
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
        }
        else {
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
        if (m_isRunning) return;
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
                                m_playerStatuses.remove(s.uuid);
                        }
                        // Updates status
                        s.OnUpdate();
                    }
                }
            }
        }, 0, TICK_PERIOD);
    }

}