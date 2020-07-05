package me.bscal.hardcoremc.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.bscal.hardcoremc.status.StatusManager;
import me.bscal.hardcoremc.status.StatusPlayer;

public class HardcorePlayer {

    public UUID uuid;
    public Player player;

    public boolean isCached;

    public StatusPlayer statuses;

    public HardcorePlayer(final UUID uuid, final Player player) {
        this.uuid = uuid;
        this.player = player;
        this.isCached = false;
        this.statuses = new StatusPlayer();
    }

    public void Save() {
        StatusManager.HandleLeftPlayer(uuid, statuses);
        statuses = null;
    }

    public void Clean() {
        statuses = null;
    }
    
}