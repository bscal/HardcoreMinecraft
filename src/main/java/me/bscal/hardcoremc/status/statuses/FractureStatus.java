package me.bscal.hardcoremc.status.statuses;

import org.bukkit.entity.Player;

import me.bscal.hardcoremc.status.Status;
import me.bscal.hardcoremc.status.StatusType;

public class FractureStatus extends Status {

    public FractureStatus(Player player) {
        super("Fracture", StatusType.FRACTURE, player);
    }
    
}