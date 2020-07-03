package me.bscal.hardcoremc.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.bscal.hardcoremc.App;

public class PlayerCache {

    private final static long TIMEOUT = 20 * 3 * 1;

    private final static Set<UUID> m_cache = new HashSet<UUID>();

    private PlayerCache() {}

    public static void Add(UUID uuid) {
        m_cache.add(uuid);
    }

    public static void AddWithTimeout(UUID uuid, Runnable runnable) {
        m_cache.add(uuid);
        App.Logger.info("PlayerCache: Caching for " + TIMEOUT / 1000 + " secs");
        Bukkit.getScheduler().runTaskLater(App.Get, () -> {
            m_cache.remove(uuid);
            var p = Bukkit.getPlayer(uuid);
            if (p == null || !p.isOnline()) {
                m_cache.remove(uuid);
                runnable.run();
            }
            App.Logger.info("PlayerCache: Running. " + uuid);
        }, TIMEOUT);
    }

    public static void Remove(UUID uuid) {
        m_cache.remove(uuid);
    }

    public static boolean Contains(UUID uuid) {
        return m_cache.contains(uuid);
    }
    
}