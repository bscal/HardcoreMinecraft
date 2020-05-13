package me.bscal.hardcoremc.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gyurix.scoreboard.ScoreboardAPI;
import gyurix.scoreboard.Sidebar;
import gyurix.scoreboard.SidebarLine;

public class ScoreboardManager implements Listener {

    private final static Sidebar MAIN_SIDEBAR = new Sidebar();

    public ScoreboardManager() {
        MAIN_SIDEBAR.setTitle("HardcoreMC");

        // Clears all lines
        for (SidebarLine sbl : MAIN_SIDEBAR.lines) {
            sbl.setText("");
        }
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        //ScoreboardAPI.playerJoin(e.getPlayer());
        ScoreboardAPI.setSidebar(e.getPlayer(), MAIN_SIDEBAR);
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e) {
        ScoreboardAPI.playerLeave(e.getPlayer());
    }

    public static void UpdateBoardText(Player p, int line, String text) {
        Sidebar sb = (Sidebar)ScoreboardAPI.sidebars.get(p.getName()).active;
        sb.setLine(line, text);
    }

    public static void ToggleVisible(Player p) {
        Sidebar sb = (Sidebar)ScoreboardAPI.sidebars.get(p.getName()).active;
        sb.setVisible(!sb.isVisible());
    }

}
