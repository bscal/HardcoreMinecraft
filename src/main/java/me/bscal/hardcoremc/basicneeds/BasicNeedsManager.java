package me.bscal.hardcoremc.basicneeds;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import gyurix.configfile.ConfigData;
import me.bscal.hardcoremc.App;
import me.bscal.hardcoremc.events.HardcoreJoinEvent;
import me.bscal.hardcoremc.player.HardcorePlayer;

public class BasicNeedsManager implements Listener {

    public BasicNeedsManager() {
    }

    public void Update(int period, UUID pUUID, HardcorePlayer hPlayer) {
        if (period % 30 != 0) return;
        hPlayer.needs.UpdateNeeds(-1.0f);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void OnPlayerJoin(HardcoreJoinEvent e) {
        UUID uuid = e.getUUID();

        BasicNeeds needs = new BasicNeeds();

        ConfigData data = App.UserData.getData(uuid.toString());
        if (data.isEmpty()) {
            App.Logger.info("user empty");
            needs.hunger = 100;
            needs.thirst = 100;
            needs.temperature = 100;
            needs.sanity = 100;
            needs.stamina = 100;
            needs.toxicity = 0;
            needs.wellFed = 0;
        }
        else {
            needs.hunger = App.UserData.getFloat(uuid.toString() + ".needs_hunger");
            needs.thirst = App.UserData.getFloat(uuid.toString() + ".needs_thirst");
            needs.temperature = App.UserData.getFloat(uuid.toString() + ".needs_temperature");
            needs.sanity = App.UserData.getFloat(uuid.toString() + ".needs_sanity");
            needs.stamina = App.UserData.getFloat(uuid.toString() + ".needs_stamina");
            needs.toxicity = App.UserData.getFloat(uuid.toString() + ".needs_toxicity");
            needs.wellFed = App.UserData.getFloat(uuid.toString() + ".needs_wellFed");
        }

        e.getHCPlayer().needs = needs;
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId(); 
        Save(uuid, App.Get.GetPlayerManager().GetPlayer(uuid).needs);
    }

    private void Save(UUID uuid, BasicNeeds needs) {
        App.UserData.setObject(uuid.toString() + ".needs_hunger", needs.hunger);
        App.UserData.setObject(uuid.toString() + ".needs_thirst", needs.thirst);
        App.UserData.setObject(uuid.toString() + ".needs_temperature", needs.temperature);
        App.UserData.setObject(uuid.toString() + ".needs_sanity", needs.hunger);
        App.UserData.setObject(uuid.toString() + ".needs_staminia", needs.thirst);
        App.UserData.setObject(uuid.toString() + ".needs_toxicity", needs.temperature);
        App.UserData.setObject(uuid.toString() + ".needs_wellFed", needs.temperature);
        App.UserData.save();
    }

}