package me.bscal.hardcoremc.status.statuses;

import org.bukkit.entity.Player;

import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.status.Status;
import me.bscal.hardcoremc.status.StatusGroup;

public class BleedStatus extends Status {

    private final static double DMG_PER_TICK = 1;

    public BleedStatus() {
    }

    public BleedStatus(Player player) {
        super("Bleed", StatusGroup.BLEED, player);
    }

    @Override
    protected void OnUpdate() {
        App.Logger.info("Updating... " + duration);

        double hp = player.getHealth();
        if (hp - DMG_PER_TICK < 0) player.setHealth(0);
        else player.setHealth(hp - DMG_PER_TICK);
    }
}