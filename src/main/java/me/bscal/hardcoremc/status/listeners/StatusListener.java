package me.bscal.hardcoremc.status.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import me.bscal.hardcoremc.App;

public abstract class StatusListener implements Listener {

    public StatusListener(Class<? extends Event> toListen) {

        Bukkit.getPluginManager().registerEvent(
            toListen,
            this, 
            EventPriority.NORMAL, 
            new EventExecutor(){
                @Override
                public void execute(Listener listener, Event event) throws EventException {
                    OnEvent(event);
                }
            }, 
            App.Get);
    }
    
    protected abstract void OnEvent(Event event);

}