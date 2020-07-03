package me.bscal.hardcoremc.status.listeners;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.StatusManager;
import me.bscal.hardcoremc.status.statuses.FractureStatus;
import me.bscal.hardcoremc.utils.StatusListenerDebug;

public class FractureListener implements Listener {

    private final static double MIN_DAMAGE = 8;
    private final static double MIN_CHANCE = .25;

    private final static double CHANCE_PER_DAMAGE = .05;

    private final static double MAX_DAMAGE = 14;
    private final static double MAX_CHANCE = .8;

    private Random m_rand = new Random();

    @EventHandler
    public void OnFall(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != DamageCause.FALL) return;

        double chance = m_rand.nextDouble();
        double dmg = e.getFinalDamage();
        double overDmg = dmg - MIN_DAMAGE;
        double overChance = (overDmg > 0) ? 0 : overDmg * CHANCE_PER_DAMAGE;

        if (dmg > MIN_DAMAGE) {
            if (chance > MIN_CHANCE + overChance) {
                CreateFracture((Player) e.getEntity());
            }
        }

        if (App.Debug)
            StatusListenerDebug.Print("OnFall", dmg > MIN_DAMAGE, "%", chance, "dmg", dmg, "Over%", overChance, "overDmg", overDmg);
        }

    private void CreateFracture(Player p) {
        FractureStatus fs = new FractureStatus(p);
        fs.duration = 1200;
        fs.persistent = true;
        fs.Start();
    }
    
}