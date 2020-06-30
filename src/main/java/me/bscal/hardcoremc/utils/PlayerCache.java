package me.bscal.hardcoremc.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.bscal.hardcoremc.App;

public class PlayerCache {

    private final static long TIMEOUT = 60 * 3 * 1000;

    private final static Set<UUID> m_cache = new HashSet<UUID>();

    private PlayerCache() {}

    public static void Add(UUID uuid) {
        m_cache.add(uuid);
    }

    public static void AddWithTimeout(UUID uuid, Runnable runnable) {
        m_cache.add(uuid);

        Bukkit.getScheduler().runTaskLaterAsynchronously(App.Get, () -> {
            m_cache.remove(uuid);
            if (Bukkit.getPlayer(uuid) == null)
                runnable.run();
        }, TIMEOUT);
    }

    public static void Remove(UUID uuid) {
        m_cache.remove(uuid);
    }

    public static boolean Contains(UUID uuid) {
        return m_cache.contains(uuid);
    }
    
}