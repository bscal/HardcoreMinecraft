package me.bscal.hardcoremc.statuses;

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
    public float duration;
    public UUID player;
    
    protected boolean m_canStack = false;
    protected boolean m_hasStarted = false;
    protected int m_stacks = 0;

    public Status (String name, Event toListen) {
        this.name = name;

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

    protected void OnEvent(Listener listener, Event e) {
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
        StatusManager.RemoveStatus(player, this);
    }

    @Override
    public String toString() {
        return new String(getClass().getName() + name);
    }

}