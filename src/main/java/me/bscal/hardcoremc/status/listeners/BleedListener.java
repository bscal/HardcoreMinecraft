package me.bscal.hardcoremc.status.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.statuses.BleedStatus;

public class BleedListener implements Listener {

    @EventHandler
    public void OnEvent(EntityDamageByEntityEvent e) {
        App.Logger.info("Bleed Listener OnEvent");
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Zombie) {
            BleedStatus bs = new BleedStatus((Player) e.getEntity());
            bs.duration = 10f;
            bs.Start();
        }
    }

}