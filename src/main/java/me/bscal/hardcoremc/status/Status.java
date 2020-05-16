package me.bscal.hardcoremc.status;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import me.bscal.hardcoremc.App;

public class Status implements Listener {

    public String name;
    public StatusType type;
    public float duration;
    public UUID uuid;
    
    protected boolean m_hasStarted = false;
    protected boolean m_canStack = false;
    protected int m_stacks = 0;

    public Status (String name, StatusType type, Event toListen, UUID uuid) {
        this.name = name;
        this.type = type;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvent(
            toListen.getClass(),
            this, 
            EventPriority.NORMAL, 
            new EventExecutor(){
                @Override
                public void execute(Listener listener, Event event) throws EventException {
                    if (!(listener instanceof Status)) return;
                    
                    Status s = (Status)listener;
                    App.Logger.info("Status Event Fired. " + s.name);
                    s.OnEvent(listener, event);
                }
            }, 
            App.Get);
    }

    public void Start() {
        StatusManager.AddStatus(uuid, this);
    }

    protected void OnEvent(Listener listener, Event event) {
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
        StatusManager.RemoveStatus(uuid, this);
    }

    @Override
    public String toString() {
        return new String(type.value + "_" + name);
    }

}