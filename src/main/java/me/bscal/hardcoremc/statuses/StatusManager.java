package me.bscal.hardcoremc.statuses;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.bscal.hardcoremc.App;

public class StatusManager {
    
    private final static long TICKS_PER_UPDATE = 20;

    private static Map<UUID, HashMap<String, Status>> m_statusMap = new HashMap<UUID, HashMap<String, Status>>();
    private static boolean m_isRunning = false;

    private StatusManager() {}

    public static void AddStatus(UUID uuid, Status s) {
        //Status status = m_statusMap.get(p.getUniqueId()).get(new String(s.name + s.index));
        HashMap<String, Status> map = m_statusMap.get(uuid);

        if (map == null)
            m_statusMap.put(uuid, new HashMap<String, Status>());

        if (map.containsKey(s.toString())) {
            map.get(s.toString()).OnRefresh(s);
        }
        else {
            map.put(s.toString(), s);
            s.OnStart();
        }
    }

    public static void RemoveStatus(UUID uuid, Status s) {
        HashMap<String, Status> map = m_statusMap.get(uuid);

        if (map.containsKey(s.toString())) {
            map.remove(s.toString());
        }

        if (map.isEmpty()) {
            m_statusMap.remove(uuid);
        }
    }

    public static void StartStatusUpdater() {
        if (m_isRunning) return;
        m_isRunning = true;

        // Update scheduler 1 time a second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(App.Get, new Runnable() {
            @Override
            public void run() {
                for (HashMap<String, Status> map : m_statusMap.values()) {
                    for (Status s : map.values()) {
                        // If TickDuration returns true because duration was 0
                        // remove status from nested map and if player map is empty remove player map
                        if (s.TickDuration()) {
                            map.remove(s.toString());
                            if (map.isEmpty())
                                m_statusMap.remove(s.player);
                        }
                        // Updates status
                        s.OnUpdate();
                    }
                }
            }
        }, 0, TICKS_PER_UPDATE);
    }

}