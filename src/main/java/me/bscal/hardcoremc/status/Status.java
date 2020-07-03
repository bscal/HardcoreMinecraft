package me.bscal.hardcoremc.status;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.bscal.hardcoremc.App;

public class Status {

    public String name;
    public StatusGroup group;
    public float duration;
    public UUID playerUUID;
    public Player player;
    public boolean doRemove;
    
    public boolean removeOnDeath = true;
    public boolean persistent = false;
    public boolean aliveForTick = true;
    
    protected boolean m_hasStarted = false;
    protected boolean m_canStack = false;
    protected int m_stacks = 0;

    public Status() {
    }

    public Status (String name, StatusGroup group, Player player) {
        this.name = name;
        this.group = group;
        this.player = player;
        this.playerUUID = player.getUniqueId();
    }

    public Status(Status status) {
        this.name = status.name;
        this.group = status.group;
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
        Apply();
    }

    protected void Apply() {

    }

    protected void OnStart() {
    }

    protected void OnEnd(boolean byDeath) {
    }

    protected void OnUpdate() {
    }

    protected void OnRespawn() {
        Apply();
    }

    protected void OnRefresh(Status newStatus) {
        if (m_canStack)
            m_stacks++;
        duration = newStatus.duration;
    }

    protected boolean TickDuration() {
        if (duration <= 0) return true;

        duration--;
        return false;
    }

    public void Destroy(boolean byDeath) {
        OnEnd(byDeath);
        StatusManager.RemoveStatus(playerUUID, this);
    }

    public String toString() {
        return new String(group.key + "_" + name);
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name + ';');
        sb.append(this.group.toString() + ";");
        sb.append(this.duration + ";");
        sb.append(this.playerUUID.toString() + ";");
        sb.append(this.removeOnDeath + ";");
        sb.append(this.persistent + ";");
        sb.append(this.m_stacks + ";");
        sb.append(this.m_hasStarted + ";");
        sb.append(this.m_canStack + ";");
        return sb.toString();
    }

    public void deserialize(String data) {
        String[] values = data.split(";");

        final int EXPECTED = 9;
        if (values.length < EXPECTED || values.length > EXPECTED) {
            App.Logger.severe("Status deserialization values out of bounds. " + values.length + "/" + EXPECTED);
            return;
        }

        try {
            this.name = values[0];
            this.group = StatusGroup.valueOf(values[1]);
            this.duration = Float.parseFloat(values[2]);
            this.playerUUID = UUID.fromString(values[3]);
            this.removeOnDeath = Boolean.parseBoolean(values[4]);
            this.persistent = Boolean.parseBoolean(values[5]);
            this.m_stacks = Integer.parseInt(values[6]);
            this.m_hasStarted = Boolean.parseBoolean(values[7]);
            this.m_canStack = Boolean.parseBoolean(values[8]);
        }
        catch (Exception e) {
            App.Logger.severe(e.getMessage());
            e.printStackTrace();
            return;
        }
        this.player = Bukkit.getPlayer(playerUUID);
    }
}