package me.bscal.hardcoremc.status;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import gyurix.configfile.ConfigFile;
import me.bscal.hardcoremc.App;

public class StatusPlayer {

    public UUID uuid;
    public boolean cached;
    public Map<String, Status> statusByName;
    public Map<StatusGroup, ArrayList<Status>> statusesByGroup;

    public boolean deadEvent = false;
    public boolean respawnEvent = false;

    public StatusPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.cached = false;
        statusByName = new HashMap<String, Status>();
        statusesByGroup = new HashMap<StatusGroup, ArrayList<Status>>();
    }

    public void Update() {
        for (Status s : statusByName.values()) {
            if (deadEvent && s.removeOnDeath)
                s.Destroy(true);
            if (respawnEvent)
                s.OnRespawn();

            if (s.aliveForTick && s.player.isDead()) continue;
            // If TickDuration returns true because duration was 0
            // remove status from nested map and if player map is empty remove player map
            if (s.doRemove || s.TickDuration()) {
                s.Destroy(false);
            }
            // Updates status
            s.OnUpdate();
        }

        if (deadEvent) deadEvent = false;
        if (respawnEvent) respawnEvent = false;
    }

    public void RemoveStatus(final Status s) {
        if (statusByName.containsKey(s.toString())) {
            statusByName.remove(s.toString());
            statusesByGroup.get(s.group).remove(s);
        }

        if (statusesByGroup.size() > 9) CleanLists();
    }

    public void AddStatus(final Status s) {
        statusByName.put(s.toString(), s);
        boolean contains = statusesByGroup.containsKey(s.group);
        if (!contains) { // new list 
            statusesByGroup.put(s.group, new ArrayList<Status>());
        }
        statusesByGroup.get(s.group).add(s);
    }
    
    public void ReapplyStatuesToPlayer() {
        statusByName.forEach((k,v) -> {v.Apply();});
    }

    public void Save(ConfigFile config) {
        for (Status s : statusByName.values()) {
            App.Logger.info("[SAVING STATUS] " + s.name);
            config.setObject(s.toString() + ".class", s.getClass().getName());
            config.setObject(s.toString() + ".data", s.serialize());
            //sub.setObject(String.format("%s.name", s.toString()), s.toString());
            //sub.setObject(String.format("%s.duration", s.toString()), s.duration);
        }
    }

    public void Load(ConfigFile config) {
        for (String key : config.getStringKeyList()) {
            App.Logger.info("[BUILDING STATUS] " + key);
            String className = config.getString(key + ".class");
            Status s = null;
            try {
                Class<?> clazz = Class.forName(className);
                s = (Status) clazz.getConstructor().newInstance();
                App.Logger.info(s.getClass().getName() + " " + s.getClass().getSuperclass());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (s == null) {
                App.Logger.severe("Status in Load() is null");
                return;
            }

            s.deserialize(config.getString(key + ".data"));
            s.Start();
        }
    }

    public void Clean() {
        uuid = null;
        cached = false;
        statusByName.clear();
        statusByName = null;
        statusesByGroup.clear();
        statusesByGroup = null;
    }

    public void PrintMap() {
        for (var pair : statusByName.entrySet()) {
            App.Logger.info("key, " + pair.getKey() + " value " + pair.getValue());
        }
        App.Logger.info(" ");
        for (var pair : statusesByGroup.entrySet()) {
            App.Logger.info("key, " + pair.getKey() + " value " + pair.getValue());
        }
    }

    private void CleanLists() {
        for (var pair : statusesByGroup.entrySet()) {
            if (pair.getValue().isEmpty()) statusesByGroup.remove(pair.getKey());
        }
    }

}