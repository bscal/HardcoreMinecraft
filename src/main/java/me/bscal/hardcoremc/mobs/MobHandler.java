package me.bscal.hardcoremc.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

/***
 * Class for handling spawning and setting levels/stats for mobs
 */
public class MobHandler implements Listener {

    private final static int DEFAULT_LEVEL = 1;
    private final static int MAX_LEVEL = 99;

    private final static float DMG_PER = 1f;
    private final static float HP_PER = 1f;

    private final Map<Entity, List<CustomMob>> m_customMobs;

    public MobHandler() {
        m_customMobs = new HashMap<Entity, List<CustomMob>>();
    }

    public void RegisterMob(CustomMob mob, Entity ent) {
        if (!m_customMobs.containsKey(ent)) {
            m_customMobs.put(ent, new ArrayList<CustomMob>());
        }

        m_customMobs.get(ent).add(mob);
    }

    // Events
    public void OnEntitySpawn(EntitySpawnEvent e) {
        final Entity ent = e.getEntity();
        if (m_customMobs.containsKey(ent)) return;

        CustomMob mobToSpawn = null;

        for (CustomMob mob : m_customMobs.get(ent)) {
            final CustomSpawnData sData = mob.TrySpawnConditions(e);

            if (mobToSpawn == null || sData.canSpawn && mobToSpawn.spawnData.weight < sData.weight)
                mobToSpawn = mob;
        }

        mobToSpawn.Spawn(ent);
    }
}

class CustomSpawnData {
    boolean canSpawn;
    float weight;

    public CustomSpawnData(boolean canSpawn, float weight) {
        this.canSpawn = canSpawn;
        this.weight = weight;
    }
}