package ca.kaxx.board;

import ca.kaxx.board.adapter.KaxxScoreboardAdapter;
import ca.kaxx.board.animation.ScoreboardAnimation;
import ca.kaxx.board.listeners.KaxxScoreboardListener;
import ca.kaxx.board.task.KaxxScoreboardUpdateTask;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;


/**
 * A class that handles the creation and management of scoreboards for players.
 * <p>
 * This library has been based on the code of <a href="https://github.com/Vekooo">Vekooo</a>.
 * <p>
 * Thanks to that big goat for the code.
 */

@Getter
@Setter
public final class KaxxScoreboardHandler {

    /**
     * Represents a map of scoreboards associated with player UUIDs.
     *
     * <p>This variable stores a mapping between UUIDs and their corresponding KaxxScoreboard instances.</p>
     */
    private final Map<UUID, KaxxScoreboard> scoreboards;
    
    /**
     * Represents an adapter for the KaxxScoreboardHandler class.
     * This adapter is responsible for providing the title and lines for the scoreboard of a player.
     */
    private KaxxScoreboardAdapter adapter;
    
    /**
     * The refresh rate for updating the scoreboard.
     */
    private long refreshRate;

    /**
     * A listener class for scoreboard events.
     */
    private final KaxxScoreboardListener listener;
    /**
     * Represents a task that updates the scoreboards for all players in the scoreboard handler.
     */
    private final KaxxScoreboardUpdateTask updateTask;

    private ScoreboardAnimation scoreboardAnimation;

    /**
     * A class that handles the creation and management of scoreboards for players.
     */
    private KaxxScoreboardHandler(final @Nonnull Plugin plugin) {
        this.scoreboards = Maps.newHashMap();

        this.refreshRate = 2;

        Bukkit.getPluginManager().registerEvents(this.listener = new KaxxScoreboardListener(this), plugin);

        this.updateTask = new KaxxScoreboardUpdateTask(this);
        this.updateTask.runTaskTimerAsynchronously(plugin, 0L, refreshRate);
    }

    /**
     * Removes all references and cancels the update task associated with this KaxxScoreboardHandler instance.
     * Should be called when the scoreboard functionality is no longer needed.
     */
    public synchronized void cleanup() {
        this.adapter = null;
        HandlerList.unregisterAll(this.listener);

        this.scoreboards.values().forEach(KaxxScoreboard::destroy);
        this.scoreboards.clear();
        this.updateTask.cancel();
    }

    /**
     * Creates a new instance of KaxxScoreboardHandler.
     *
     * @param plugin The plugin instance.
     * @return The newly created KaxxScoreboardHandler instance.
     */
    public synchronized static KaxxScoreboardHandler create(final @Nonnull Plugin plugin) {
        return new KaxxScoreboardHandler(plugin);
    }

    /**
     * Sets the adapter for the KaxxScoreboardHandler.
     *
     * @param adapter the adapter to set. Cannot be null.
     * @throws NullPointerException if the adapter is null.
     */
    public void setAdapter(final @Nonnull KaxxScoreboardAdapter adapter) {
        Preconditions.checkNotNull(adapter, "Adapter cannot be null");
        this.adapter = adapter;
    }

    /**
     * Creates a scoreboard for the player with the given UUID if one does not already exist.
     *
     * @param uniqueId the UUID of the player
     * @throws IllegalArgumentException if the player with the given UUID is not online
     */
    public void createScoreboard(final @Nonnull UUID uniqueId) {
        if (hasScoreboard(uniqueId)) {
            return;
        }

        final Player player = Bukkit.getPlayer(uniqueId);

        if (player == null) {
            throw new IllegalArgumentException("Player with UUID " + uniqueId + " is not online");
        }

        this.scoreboards.put(uniqueId, new KaxxScoreboard(player));
    }

    /**
     * Removes the scoreboard for a player with the given unique ID.
     *
     * @param uniqueId the UUID of the player
     * @throws IllegalArgumentException if the player is not online
     * @throws NullPointerException if the KaxxScoreboard is null
     */
    public void removeScoreboard(final @Nonnull UUID uniqueId) {
        if (!hasScoreboard(uniqueId)) {
            return;
        }

        final Player player = Bukkit.getPlayer(uniqueId);

        if (player == null) {
            throw new IllegalArgumentException("Player with UUID " + uniqueId + " is not online");
        }

        final KaxxScoreboard scoreboard = getScoreboard(uniqueId);
        Preconditions.checkNotNull(scoreboard, "KaxxScoreboard is null");

        this.scoreboards.remove(uniqueId);
        scoreboard.destroy();
    }

    /**
     * Checks if a player identified by their UUID has a scoreboard.
     *
     * @param uuid The UUID of the player.
     * @return {@code true} if the player has a scoreboard, {@code false} otherwise.
     */
    public boolean hasScoreboard(UUID uuid) {
        return scoreboards.containsKey(uuid);
    }

    /**
     * Retrieves the scoreboard associated with the given unique ID.
     *
     * @param uniqueId the unique ID of the player
     * @return the scoreboard associated with the given unique ID, or null if no scoreboard is found
     */
    public @Nullable KaxxScoreboard getScoreboard(final @Nonnull UUID uniqueId) {
        return scoreboards.get(uniqueId);
    }
    
    
}
