package me.bscal.hardcoremc.status.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.StatusManager;

public class StatusListener implements Listener {
    
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        App.Logger.info("loading statuses...");
        StatusManager.OnJoin(e.getPlayer());
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        App.Logger.info("Saving statuses...");
        StatusManager.OnExit(e.getPlayer());
    }

    @EventHandler 
    public void OnPlayerDeath(PlayerDeathEvent e) {
        StatusManager.OnDeath(e.getEntity());
    }
    
    @EventHandler
    public void OnPlayerRespawn(PlayerRespawnEvent e) {

    }

}