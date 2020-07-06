package me.bscal.hardcoremc.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntitySpawnEvent;

public abstract class CustomMob {
    
    public String name;
    public Entity type;

    public boolean CanSpawn(EntitySpawnEvent e) {
        return false;
    }

    public void Spawn(LivingEntity ent) {

    }
    
}