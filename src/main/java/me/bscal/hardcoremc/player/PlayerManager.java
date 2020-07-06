package me.bscal.hardcoremc.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.StatusManager;
import me.bscal.hardcoremc.status.StatusPlayer;
import me.bscal.hardcoremc.utils.PlayerCache;

public class PlayerManager implements Listener {
    
    public static PlayerManager Singleton;

    private final static long TICK_PERIOD = 10;

    public final static long TWICE_PER_SEC = 10 / TICK_PERIOD;
    public final static long PER_SEC = 20 / TICK_PERIOD;
    public final static long PER_THIRTY_SEC = (20 * 30) / TICK_PERIOD;


    private long m_currentPeriod;

    private final Map<UUID, HardcorePlayer> players = new HashMap<UUID, HardcorePlayer>();

    public PlayerManager() {
        if (Singleton != null) {
            App.Logger.severe("PlayerManager already initialized!");
            return;
        }

        Singleton = this;
    }

    public void StartUpdater() {
        // Update scheduler 1 time a second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(App.Get, new Runnable() {
            @Override
            public void run() {
                m_currentPeriod++;

                App.Logger.info("Updating PlayerManager | Period: " + m_currentPeriod);

                for (var pair : players.entrySet()) {
                    StatusManager.Update(m_currentPeriod, pair.getValue());
                }
                
            }
        }, 20, TICK_PERIOD);
    }

    public boolean ExistsCheck(final UUID uuid) {
        final boolean exists = players.containsKey(uuid);
        if (exists) return exists;

        final Player p = Bukkit.getPlayer(uuid);
        
        if (p != null) {
            App.Logger.warning("ExistsCheck returned false but Player is online. Creating HardcorePlayer instance...");
            players.put(uuid, new HardcorePlayer(uuid, p));
            return true;
        }
            
        App.Logger.severe("ExistsCheck returned false and Player was null");
        return false;
    }

    public HardcorePlayer GetPlayer(UUID uuid) {
        return players.get(uuid);
    }

        /***
     * Saves and clears and StatusManager player data.
     */
    public void SaveAllPlayers() {
        players.forEach((k, v) -> {
            v.Save();

            v = null;
        });
        players.clear();
    }

    // ----- Event Listeners -----
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        boolean firstJoin = false;

        if (!PlayerCache.Contains(uuid)) {
            players.put(uuid, new HardcorePlayer(uuid, e.getPlayer()));
            firstJoin = true;
        }
        
        HardcorePlayer hcPlayer = players.get(uuid);
        StatusManager.OnJoin(hcPlayer, firstJoin);
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        HardcorePlayer hcPlayer = players.get(e.getPlayer().getUniqueId());
        
        PlayerCache.AddWithTimeout(hcPlayer.uuid, () -> {
            StatusManager.OnExit(hcPlayer);

            // Tries best to make sure GC will free memory and remove pair
            HardcorePlayer p = players.remove(e.getPlayer().getUniqueId());
            if (p != null) { 
                p.Clean();
                p = null;
             }
        });
    }

    @EventHandler 
    public void OnPlayerDeath(PlayerDeathEvent e) {
        HardcorePlayer hcPlayer = players.get(e.getEntity().getUniqueId());
        StatusManager.OnDeath(hcPlayer);
    }
    
    @EventHandler
    public void OnPlayerRespawn(PlayerRespawnEvent e) {
        HardcorePlayer hcPlayer = players.get(e.getPlayer().getUniqueId());
        StatusManager.OnRespawn(hcPlayer);
    }

    // ----- Utility Function -----

    public void PrintMap() {
        for (var pair : players.entrySet()) {
            StatusPlayer value = pair.getValue().statuses;
            value.PrintMap();
            App.Logger.info("key, " + pair.getKey() + " value " + pair.getValue().player.getDisplayName());
        }
    }
}