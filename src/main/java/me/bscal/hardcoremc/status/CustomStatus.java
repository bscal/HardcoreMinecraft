package me.bscal.hardcoremc.status;

import java.util.UUID;

import org.bukkit.event.Event;

public abstract class CustomStatus extends Status {

    public CustomStatus(String name, StatusType type, Event toListen, UUID uuid) {
        super(name, type, toListen, uuid);
    }

    
}