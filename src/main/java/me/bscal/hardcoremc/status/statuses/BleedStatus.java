package me.bscal.hardcoremc.status.statuses;

import org.bukkit.entity.Player;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.Status;
import me.bscal.hardcoremc.status.StatusType;

public class BleedStatus extends Status {


    public BleedStatus(Player player) {
        super("Bleed", StatusType.BLEED, player);
    }

    @Override
    protected void OnUpdate() {
        App.Logger.info("Updating... " + duration);
    }
}