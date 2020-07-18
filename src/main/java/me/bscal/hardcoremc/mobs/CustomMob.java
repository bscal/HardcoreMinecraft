package me.bscal.hardcoremc.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntitySpawnEvent;

public abstract class CustomMob {
    
    public String name;
    public CustomSpawnData spawnData;

    public abstract CustomSpawnData TrySpawnConditions(EntitySpawnEvent e);
    public abstract void Spawn(Entity ent);
    
}