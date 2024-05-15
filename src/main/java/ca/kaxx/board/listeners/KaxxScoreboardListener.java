package ca.kaxx.board.listeners;

import ca.kaxx.board.KaxxScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * A listener class for scoreboard events.
 */
public final class KaxxScoreboardListener implements Listener {

    /**
     * A class that handles the creation and management of scoreboards for players.
     */
    private final KaxxScoreboardHandler scoreboardHandler;

    /**
     * Class representing a listener for scoreboard events.
     */
    public KaxxScoreboardListener(final @Nonnull KaxxScoreboardHandler scoreboardHandler) {
        this.scoreboardHandler = scoreboardHandler;
    }

    /**
     * Handles the event when a player joins the server.
     *
     * @param event the PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player player = event.getPlayer();

        this.scoreboardHandler.createScoreboard(player.getUniqueId());
    }

    /**
     * Removes the player's quit message and removes the scoreboard for the player.
     *
     * @param event the PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final Player player = event.getPlayer();

        this.scoreboardHandler.removeScoreboard(player.getUniqueId());
    }

}
