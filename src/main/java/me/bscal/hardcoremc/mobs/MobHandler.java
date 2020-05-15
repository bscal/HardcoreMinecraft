package me.bscal.hardcoremc.mobs;

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
public class MobHandler implements Listener{

    private final static int DEFAULT_LEVEL = 1;
    private final static int MAX_LEVEL = 99;

    private final static float DMG_PER = 1f;
    private final static float HP_PER = 1f;

    public MobHandler() {}

    // Events
    public void OnEntitySpawn(EntitySpawnEvent e) {
        
    }

    public static void SetMobLevel(Entity ent, int level) {
        LivingEntity le = (LivingEntity) ent;

        // Sets mob damage
        AttributeInstance attr = le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeModifier attrModifier = new AttributeModifier("level_dmg", level * DMG_PER, Operation.ADD_NUMBER);
        attr.addModifier(attrModifier);

        // Sets mob hp
        attr = le.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attrModifier = new AttributeModifier("level_hp", level * HP_PER, Operation.ADD_NUMBER);
        attr.addModifier(attrModifier);
        le.setHealth(attr.getValue());
    }

}