package me.bscal.hardcoremc.status.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.statuses.BleedStatus;

public class BleedListener implements Listener {


    @EventHandler
    protected void OnEvent(EntityDamageByEntityEvent e) {
        App.Logger.info("Bleed Listener OnEvent");

        if (!(e.getEntity() instanceof Player)) return;

        BleedStatus bs = new BleedStatus("Bleed", e.getEntity().getUniqueId());
        bs.duration = 10f;
        bs.Start();
        
    }
}