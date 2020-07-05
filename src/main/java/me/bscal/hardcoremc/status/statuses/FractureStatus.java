package me.bscal.hardcoremc.status.statuses;

import org.bukkit.entity.Player;

import me.bscal.hardcoremc.status.Status;
import me.bscal.hardcoremc.status.StatusGroup;

public class FractureStatus extends Status {

    public FractureStatus() {
    }

    public FractureStatus(Player player) {
        super("Fracture", StatusGroup.FRACTURE, player);
    }
    
}