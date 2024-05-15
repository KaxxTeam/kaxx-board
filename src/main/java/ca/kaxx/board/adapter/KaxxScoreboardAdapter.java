package ca.kaxx.board.adapter;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * An interface for a scoreboard adapter that provides data for the KaxxScoreboardHandler.
 */
public interface KaxxScoreboardAdapter {

    /**
     * Retrieves the title for the specified player.
     *
     * @param player The player for which to retrieve the title.
     * @return The title of the player.
     */
    String getTitle(final @Nonnull Player player);

    /**
     * Retrieves the lines for a player's scoreboard.
     *
     * @param player The player whose scoreboard lines are to be retrieved. Must not be null.
     * @return A collection of lines for the player's scoreboard.
     */
    Collection<String> getLines(final @Nonnull Player player);

}