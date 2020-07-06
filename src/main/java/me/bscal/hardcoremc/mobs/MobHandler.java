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

    private final List<CustomMob> m_spawnables = new ArrayList<CustomMob>();

    private final Map<Entity, List<CustomMob>> m_customMobs;

    public MobHandler() {
        m_customMobs = new HashMap<Entity, List<CustomMob>>();
    }

    // Events
    public void OnEntitySpawn(EntitySpawnEvent e) {
        
    }

    public CustomSpawnData GetMobData(EntitySpawnEvent e) {
        var list = m_customMobs.get(e.getEntity());

        m_spawnables.clear();

        for (CustomMob mob : list) {
            if (mob.CanSpawn(e)) {
                m_spawnables.add(mob);
            }
        }

        return null;
    }

}

class CustomSpawnData {
        
}