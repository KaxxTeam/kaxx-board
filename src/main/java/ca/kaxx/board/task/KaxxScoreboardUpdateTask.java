package ca.kaxx.board.task;

import ca.kaxx.board.KaxxScoreboard;
import ca.kaxx.board.KaxxScoreboardHandler;
import ca.kaxx.board.adapter.KaxxScoreboardAdapter;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class KaxxScoreboardUpdateTask extends BukkitRunnable {

    /**
     * Represents a handler for scoreboards in the KaxxScoreboard library.
     */
    private final KaxxScoreboardHandler scoreboardHandler;

    /**
     * Updates the scoreboards for all players in the scoreboard handler.
     *
     * @param scoreboardHandler The scoreboard handler that manages the scoreboards.
     */
    public KaxxScoreboardUpdateTask(final @Nonnull KaxxScoreboardHandler scoreboardHandler) {
        this.scoreboardHandler = scoreboardHandler;
    }

    /**
     * Updates the scoreboards for all players in the scoreboard handler.
     */
    @Override
    public void run() {

        for (final Map.Entry<UUID, KaxxScoreboard> entry : scoreboardHandler.getScoreboards().entrySet()) {
            final Player player = Bukkit.getPlayer(entry.getKey());
            final KaxxScoreboard scoreboard = entry.getValue();

            if (player == null || scoreboard == null) {
                continue;
            }

            final KaxxScoreboardAdapter adapter = scoreboardHandler.getAdapter();

            if (adapter == null) {
                continue;
            }

            scoreboard.setTitle(translate(adapter.getTitle(player)));

            final List<String> lines = Lists.newArrayList(adapter.getLines(player));

            Comparator<String> comparator = Comparator.comparingInt(lines::indexOf);

            comparator = comparator.reversed();

            lines.sort(comparator);

            for (int i = 0; i < lines.size(); i++) {
                scoreboard.setLine(i, translate(lines.get(i)));
            }
        }
    }


    /**
     * Translates a given text by replacing color codes with the specified format.
     *
     * @param text The text to be translated.
     * @return The translated text with color codes replaced.
     */
    private String translate(final @Nonnull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}