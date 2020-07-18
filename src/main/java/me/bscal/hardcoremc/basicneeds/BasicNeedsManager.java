package me.bscal.hardcoremc.basicneeds;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gyurix.configfile.ConfigData;
import gyurix.scoreboard.ScoreboardAPI;
import gyurix.scoreboard.Sidebar;
import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.player.HardcorePlayer;

public class BasicNeedsManager implements Listener {

    private final HashMap<UUID, BasicNeeds> m_playerNeeds = new HashMap<>();

    public BasicNeedsManager() {
    }

    public void Update(int period, UUID pUUID, HardcorePlayer hPlayer) {
        if (period % 30 != 0) return;
        m_playerNeeds.get(pUUID).UpdateNeeds(-1.0f);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        BasicNeeds needs = new BasicNeeds();

        ConfigData data = App.UserData.getData(uuid.toString());
        if (data.isEmpty()) {
            App.Logger.info("user empty");
            needs.hunger = 100;
            needs.thirst = 100;
            needs.temperature = 100;
        }
        else {
            needs.hunger = App.UserData.getFloat(uuid.toString() + ".needs_hunger");
            needs.thirst = App.UserData.getFloat(uuid.toString() + ".needs_thirst");
            needs.temperature = App.UserData.getFloat(uuid.toString() + ".needs_temperature");
        }

            
        m_playerNeeds.put(uuid, needs);



        // Sidebar BasicNeeds

        Sidebar bar = new Sidebar();
        bar.setTitle("Hardcore Craft");
        bar.setLine(6, Float.toString(needs.hunger));
        bar.setLine(7, Float.toString(needs.hunger));
        bar.setLine(8, Float.toString(needs.hunger));
        ScoreboardAPI.setSidebar(e.getPlayer(), bar);
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        Save(uuid, m_playerNeeds.get(uuid));
    }

    private void Save(UUID uuid, BasicNeeds needs) {
        App.UserData.setObject(uuid.toString() + ".needs_hunger", needs.hunger);
        App.UserData.setObject(uuid.toString() + ".needs_thirst", needs.thirst);
        App.UserData.setObject(uuid.toString() + ".needs_temperature", needs.temperature);
        App.UserData.save();
    }

}