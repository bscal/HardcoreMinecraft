package me.bscal.hardcoremc.status.listeners;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.bscal.hardcoremc.status.statuses.FractureStatus;

public class FractureListener implements Listener {

    private final static double MIN_DAMAGE = 8;
    private final static double MIN_CHANCE = .2;
    private final static double MAX_DAMAGE = 14;
    private final static double MAX_CHANCE = .8;

    private Random m_rand = new Random();

    @EventHandler
    public void OnFall(EntityDamageEvent e) {
        if (!(e instanceof Player)) return;
        if (e.getCause() != DamageCause.FALL) return;

        double chance = m_rand.nextDouble();

        if (e.getDamage() > MIN_DAMAGE) {
            if (chance < MIN_CHANCE) {
                CreateFracture((Player) e.getEntity());
            }
        }
        else if (e.getDamage() > MAX_DAMAGE) {
            if (chance < MAX_CHANCE) {
                CreateFracture((Player) e.getEntity());
            }
        }
    }

    private void CreateFracture(Player p) {
        FractureStatus fs = new FractureStatus(p);
        fs.duration = 1200;
        fs.persistent = true;
        fs.Start();
    }
    
}