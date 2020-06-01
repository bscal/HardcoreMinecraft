package me.bscal.hardcoremc.status;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class Status implements Listener {

    public String name;
    public StatusType type;
    public float duration;
    public UUID playerUUID;
    public Player player;
    
    public boolean removeOnDeath = true;
    public boolean persistent = false;
    
    protected boolean m_hasStarted = false;
    protected boolean m_canStack = false;
    protected int m_stacks = 0;

    public Status (String name, StatusType type, Player player) {
        this.name = name;
        this.type = type;
        this.player = player;
        this.playerUUID = player.getUniqueId();
    }

    public Status(Status status) {
        this.name = status.name;
        this.type = status.type;
        this.duration = status.duration;
        this.playerUUID = status.playerUUID;
        this.player = status.player;
        this.removeOnDeath = status.removeOnDeath;
        this.persistent = status.persistent;
        this.m_stacks = status.m_stacks;
        this.m_hasStarted = status.m_hasStarted;
        this.m_canStack = status.m_canStack;
    }

    public void Start() {
        StatusManager.AddStatus(playerUUID, this);
    }

    protected void OnStart() {
    }

    protected void OnEnd() {
    }

    protected void OnUpdate() {
    }

    protected void OnRefresh(Status newStatus) {
        if (m_canStack)
            m_stacks++;
        duration = newStatus.duration;
    }

    protected boolean TickDuration() {
        if (duration == 0) {
            OnEnd();
            return true;
        }

        duration--;
        return false;
    }

    public void Destroy() {
        StatusManager.RemoveStatus(playerUUID, this);
    }

    @Override
    public String toString() {
        return new String(type.value + "_" + name);
    }

}