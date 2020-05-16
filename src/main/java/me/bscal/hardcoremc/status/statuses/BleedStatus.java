package me.bscal.hardcoremc.status.statuses;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.CustomStatus;
import me.bscal.hardcoremc.status.StatusType;

public class BleedStatus extends CustomStatus {

    private static final EntityDamageByEntityEvent EVENT = new EntityDamageByEntityEvent(null, null, null, 0);

    protected EntityDamageByEntityEvent m_event = null;

    public BleedStatus(String name, UUID uuid) {
        super(name, StatusType.BLEED, EVENT, uuid);
    }

    @Override
    protected void OnEvent(Listener listener, Event event) {
        if (event.getClass().equals(EVENT.getClass())) {
            m_event = (EntityDamageByEntityEvent) event;
            
            if (m_event.getEntity() instanceof Player) {
                App.Logger.info("Bleeding...");
            }
        }
    }

    @Override
    protected void OnUpdate() {
        App.Logger.info("Updating... " + duration);
    }
}