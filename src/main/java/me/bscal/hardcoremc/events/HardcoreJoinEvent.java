package me.bscal.hardcoremc.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.bscal.hardcoremc.player.HardcorePlayer;

public class HardcoreJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final HardcorePlayer m_hPlayer;
    private final boolean m_isCached;

    public HardcoreJoinEvent(final HardcorePlayer hPlayer, final boolean isCached) {
        m_hPlayer = hPlayer;
        m_isCached = isCached;
    }

    public HardcorePlayer getHCPlayer() {
        return m_hPlayer;
    }

    public boolean isCached() {
        return m_isCached;
    }

    public UUID getUUID() {
        return m_hPlayer.uuid;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    
}