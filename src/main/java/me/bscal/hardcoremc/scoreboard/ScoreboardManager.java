package me.bscal.hardcoremc.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import gyurix.scoreboard.ScoreboardAPI;
import gyurix.scoreboard.ScoreboardDisplayMode;
import gyurix.scoreboard.Sidebar;
import me.bscal.hardcoremc.events.HardcoreJoinEvent;
import me.bscal.hardcoremc.player.HardcorePlayer;

public class ScoreboardManager implements Listener {

    private final static String TITLE = "HardcoreMC";

    public ScoreboardManager() {
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerJoin(HardcoreJoinEvent e) {
        ScoreboardAPI.playerJoin(e.getHCPlayer().player);
        Sidebar bar = new Sidebar();
        bar.setTitle(TITLE);
        ScoreboardAPI.setSidebar(e.getHCPlayer().player, bar);
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        ScoreboardAPI.playerLeave(e.getPlayer());
    }

    public static void UpdateBoardText(HardcorePlayer hPlayer) {
        // Sidebar BasicNeeds
        Sidebar bar = (Sidebar)ScoreboardAPI.sidebars.get(hPlayer.player.getName()).active;
        bar.setLine(1, "1");
        bar.setLine(2, "2");
        bar.setLine(3, "3");
        bar.setLine(4, "4");
        bar.setLine(5, "5");
        bar.setLine(6, "§7Hunger: " + Float.toString(hPlayer.needs.hunger));
        bar.setLine(7, "§2Thirst: " + Float.toString(hPlayer.needs.thirst));
        bar.setLine(8, "§4Temp:   "   + Float.toString(hPlayer.needs.temperature));
        bar.setLine(9, "§3Sanity: " + Float.toString(hPlayer.needs.sanity));
        bar.setLine(10, "10");
        bar.setLine(11, "11");
        bar.setLine(12, "12");
        bar.setLine(13, "13");
        bar.setLine(14, "14");
        bar.setLine(15, "15");
        //ScoreboardAPI.setSidebar(hPlayer.player, bar);
    }

    public static void SetBoardText(Player p, int line, String text) {
        Sidebar sb = (Sidebar)ScoreboardAPI.sidebars.get(p.getName()).active;
        sb.setLine(line, text);
    }

    public static void ToggleVisible(Player p) {
        Sidebar sb = (Sidebar)ScoreboardAPI.sidebars.get(p.getName()).active;
        sb.setVisible(!sb.isVisible());
    }

}
