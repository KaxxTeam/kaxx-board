package ca.kaxx.board;

import ca.kaxx.board.packets.*;
import lombok.Data;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a scoreboard for a player.
 */
@Data
public final class KaxxScoreboard {

    /**
     * Represents the connection for a player in the KaxxScoreboard class.
     * This variable holds the reference to the player's connection.
     */
    private final PlayerConnection connection;

    /**
     * Represents a private final array of strings for storing variable entries.
     */
    private final String[] entries,
    /**
     * The variable keys represents the keys of a scoreboard.
     */
    keys;

    /**
     * Represents the display packet used to show a scoreboard objective.
     *
     * <p>This packet is a subclass of {@link PacketDataSerializer} and is used to configure and build a {@link PacketPlayOutScoreboardDisplayObjective} object.</p>
     * <p>It provides methods to set the type of the scoreboard and the name of the objective, as well as a method to build the final display packet.</p>
     * <p>Example usage:</p>
     * {@code ScoreboardDisplayObjectivePacket objectiveDisplay = new ScoreboardDisplayObjectivePacket();}<br>
     * {@code objectiveDisplay.setType(ScoreboardDisplayObjectivePacket.Type.SCORE);}<br>
     * {@code objectiveDisplay.setObjectiveName("board");}<br>
     * {@code PacketPlayOutScoreboardDisplayObjective displayPacket = objectiveDisplay.build();}
     */
    private final PacketPlayOutScoreboardDisplayObjective displayPacket;

    /**
     * Determines whether the scoreboard has an objective.
     */
    private boolean hasObjective;

    /**
     * Initializes a KaxxScoreboard for the specified player.
     *
     * @param player The player for whom the scoreboard is initialized. Cannot be null.
     */
    public KaxxScoreboard(final @Nonnull Player player) {
        this.connection = ((CraftPlayer) player).getHandle().playerConnection;

        this.entries = new String[15];
        this.keys = new String[15];

        this.hasObjective = false;

        this.displayPacket = createObjectiveDisplay();
    }

    /**
     * Creates a PacketPlayOutScoreboardDisplayObjective to display a scoreboard objective.
     *
     * @return The created PacketPlayOutScoreboardDisplayObjective.
     */
    private PacketPlayOutScoreboardDisplayObjective createObjectiveDisplay() {
        final ScoreboardDisplayObjectivePacket objectiveDisplay = new ScoreboardDisplayObjectivePacket();

        objectiveDisplay.setType(ScoreboardDisplayObjectivePacket.Type.SCORE);
        objectiveDisplay.setObjectiveName("board");

        return objectiveDisplay.build();
    }

    /**
     * Checks if the given index is out of the allowed range.
     *
     * @param index the index to check
     * @return true if the index is not within the range of 0 to 14 (inclusive), false otherwise
     */
    private boolean isUnCorrectIndex(final int index) {
        return (index < 0 || index > 14);
    }

    /**
     * Returns the score corresponding to the given index.
     *
     * @param index the index of the score
     * @return the score as a string
     */
    private String getScore(final int index) {
        return ChatColor.values()[index].toString();
    }

    /**
     * Commits a score to the scoreboard.
     *
     * @param index The index of the score to commit.
     * @param name The name of the score.
     * @param boardAction The scoreboard action for the score.
     */
    private void commitScore(final int index, final @Nonnull String name,
                             final @Nonnull PacketPlayOutScoreboardScore.EnumScoreboardAction boardAction) {

        entries[index] = boardAction == PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE ? name : null;

        final ScoreboardScorePacket score = new ScoreboardScorePacket();

        score.setName(name);
        score.setBoardAction(boardAction);
        score.setObjectiveName("board");
        score.setScore(index);

        this.connection.sendPacket(score.build());
    }

    /**
     * Check if the specified index has a line.
     *
     * @param index The index to check
     * @return true if the specified index has a line, false otherwise
     */
    private boolean hasLine(final int index) {
        return entries[index] != null;
    }

    /**
     * Sets the title of the scoreboard.
     *
     * @param title the title to set for the scoreboard.
     *              Must be non-null and contain 0 to 32 characters.
     * @throws IllegalArgumentException if the title is null or contains more than 32 characters.
     */
    public void setTitle(final @Nonnull String title) {
        if (title.length() > 32) {
            throw new IllegalArgumentException("Title must contain 0 to 32 characters !");
        }

        final ScoreboardAction action;

        if (!hasObjective) {
            action = ScoreboardAction.CREATE;
            hasObjective = true;
        } else {
            action = ScoreboardAction.UPDATE;
        }

        final ScoreboardObjectivePacket objective = new ScoreboardObjectivePacket();

        objective.setObjectiveName("board");
        objective.setBoardAction(action);
        objective.setTitle(title);
        objective.setDisplayType(IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);

        this.connection.sendPacket(objective.build());
        this.connection.sendPacket(displayPacket);
    }

    /**
     * Returns the last colors used in a given string.
     *
     * @param str     the string from which to extract the last colors
     * @param length  the length of the string
     * @param lag     the lag value to determine the colors
     * @return the last colors used in the string
     */
    private String getLastColors(final @Nonnull String str, final int length, final int lag) {
        if (length == 0){
            return "";
        }

        int i = length - 1;

        char previousChar = str.charAt(i), currentChar;
        final char[] colors = new char[2];
        int colorIndex;

        if (lag == 1) {
            colors[((currentChar = str.charAt(16)) > 106 && previousChar < 114) ? 1 : 0] = currentChar;
        }

        while (--i > -1) {
            if ((currentChar = str.charAt(i)) == 167) {
                colorIndex = (previousChar > 106 && previousChar < 114) ? 1 : 0;
                if (colors[colorIndex] != 0) {
                    continue;
                }

                colors[colorIndex] = previousChar;
                if (colors[colorIndex ^ 1] == 0) {
                    continue;
                }

                break;
            }
            previousChar = currentChar;
        }

        final StringBuilder colorsBuilder = new StringBuilder(4);

        if (colors[0] != 0) {
            colorsBuilder.append("§").append(colors[0]);
        }

        if (colors[1] != 0) {
            colorsBuilder.append("§").append(colors[1]);
        }

        return colorsBuilder.toString();
    }

    /**
     * Sets the content of a line in the scoreboard.
     *
     * @param index The index of the line. Must be between 0 and 15.
     * @param line The content of the line. Must be between 0 and 32 characters long.
     * @throws IllegalArgumentException If the index is out of bounds or the line is too long.
     */
    public void setLine(final int index, final @Nonnull String line) {
        if (isUnCorrectIndex(index)) {
            throw new IllegalArgumentException("Index must be between 0 and 15 !");
        }
        if (line.length() > 32) {
            throw new IllegalArgumentException("Line " + index + " must contain 0 to 32 characters !");
        }

        final int length = line.length();
        final int lag = (length > 16 && line.charAt(15) == 167) ? 1 : 0;
        final int maxPrefixLength = 16 - lag;
        final int prefixLength = Math.min(length, maxPrefixLength);

        final String lastColors = getLastColors(line, prefixLength, lag);

        final int maxIndex = 32 - lastColors.length();

        if (length > maxIndex) {
            throw new IllegalArgumentException("Due to colors, line " + index + " cannot be greater than "
                    + maxIndex + "!");
        }

        final String score = getScore(index);
        final boolean hasLine = hasLine(index);

        if (!hasLine) {
            commitScore(index, score, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        } else if (this.keys[index] != null) {
            this.keys[index] = null;
        }

        final String name = Integer.toString(index);

        final ScoreboardTeamPacket team = new ScoreboardTeamPacket();

        team.setName(name);
        team.setTeamAction(hasLine ? ScoreboardAction.UPDATE : ScoreboardAction.CREATE);
        team.setCustomName(name);
        team.setPrefix(line.substring(0, prefixLength));
        team.setSuffix(length > maxPrefixLength ? (lastColors + line.substring(maxPrefixLength)) : "");
        team.setVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
        team.setCollision();
        team.setTeamColor(EnumChatFormat.RESET);
        team.setEntry(score);

        this.connection.sendPacket(team.build());
    }

    /**
     * Creates a line in the scoreboard with the given index and value.
     * Throws IllegalArgumentException if the index is not between 0 and 14 (inclusive),
     * or if the value's length is greater than 56 characters.
     *
     * @param valueIndex an IndexValue object representing the index and value of the line
     */
    public void createLine(final @Nonnull IndexValue valueIndex) {
        final int index = valueIndex.index();

        if (isUnCorrectIndex(index)) {
            throw new IllegalArgumentException("Index must be between 0 and 15 !");
        }

        final String key = valueIndex.value();

        final int keyLength = key.length();

        if (keyLength > 56) {
            throw new IllegalArgumentException("Line " + index + "'s static key must contain 0 to 56 characters !");
        }

        if (hasLine(index)) {
            removeLine(index);
        }

        final String prefix;

        int prefixLength;

        if (keyLength > 40) {
            prefixLength = 16;
            prefix = key.substring(0, 16);
        } else {
            prefixLength = 0;
            prefix = "";
        }

        final int lag = (prefixLength > 0 && prefix.charAt(15) == 167) ? 1 : 0;

        prefixLength = prefixLength - lag;

        final String lastColors = getLastColors(prefix, prefixLength, lag);

        final int maxIndex = 56 - lastColors.length();

        if (keyLength > maxIndex) {
            throw new IllegalArgumentException("Due to colors, line " + index + "'s key cannot be greater than "
                    + maxIndex + "!");
        }

        final String score = lastColors + key.substring(prefixLength, keyLength);

        keys[index] = prefix;
        commitScore(index, score, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

        final String name = Integer.toString(index);

        final ScoreboardTeamPacket team = new ScoreboardTeamPacket();

        team.setName(name);
        team.setTeamAction(ScoreboardAction.CREATE);
        team.setCustomName(name);
        team.setPrefix(prefix);
        team.setSuffix("");
        team.setVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
        team.setCollision();
        team.setTeamColor(EnumChatFormat.RESET);
        team.setEntry(score);

        this.connection.sendPacket(team.build());
    }

    /**
     * Sets the value for a specific index in the scoreboard.
     *
     * @param valueIndex the index and value pair to set
     * @param value      the value to set
     * @param <T>        the type of the value
     * @throws IllegalArgumentException if the index is out of range or the value is too long
     */
    public <T> void setValue(final @Nonnull IndexValue valueIndex, final @Nonnull T value) {
        final int index = valueIndex.index();

        if (isUnCorrectIndex(index)) {
            throw new IllegalArgumentException("Index must be between 0 and 15 !");
        }

        final String key = keys[valueIndex.index()];

        if (key == null) {
            throw new IllegalArgumentException("Line " + index + "'s does not have static key!");
        }

        final String suffix = String.valueOf(value);

        if (suffix.length() > 16) {
            throw new IllegalArgumentException("Line " + index + "'s suffix must contain 0 to 16 characters!");
        }

        final String name = Integer.toString(index);

        final ScoreboardTeamPacket team = new ScoreboardTeamPacket();

        team.setName(name);
        team.setTeamAction(ScoreboardAction.UPDATE);
        team.setCustomName(name);
        team.setPrefix(keys[index]);
        team.setSuffix(suffix);
        team.setVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
        team.setCollision();
        team.setTeamColor(EnumChatFormat.RESET);
        team.setEntry(getScore(index));

        this.connection.sendPacket(team.build());
    }

    /**
     * Removes a line from the scoreboard at the specified index.
     *
     * @param index the index of the line to be removed
     * @throws IllegalArgumentException if the index is not between 0 and 15 (inclusive)
     */
    public void removeLine(final int index) {
        if (isUnCorrectIndex(index)) {
            throw new IllegalArgumentException("Index must be between 0 and 15 !");
        }

        final String score = getScore(index);

        final ScoreboardTeamPacket team = getScoreboardTeamPacket(index, score);

        this.connection.sendPacket(team.build());
        commitScore(index, score, PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE);

        this.entries[index] = null;
        this.keys[index] = null;
    }

    /**
     * Returns a ScoreboardTeamPacket object for a given index and score.
     *
     * @param index the index of the line
     * @param score the score for the line
     * @return the ScoreboardTeamPacket object
     * @throws IllegalArgumentException if the line index does not exist
     */
    private ScoreboardTeamPacket getScoreboardTeamPacket(final int index, final @Nonnull String score) {
        if (!hasLine(index)) {
            throw new IllegalArgumentException("Line index " + index + " does not exist!");
        }

        final String name = Integer.toString(index);

        final ScoreboardTeamPacket team = new ScoreboardTeamPacket();

        team.setName(name);
        team.setTeamAction(ScoreboardAction.DELETE);
        team.setCustomName(name);
        team.setPrefix("");
        team.setSuffix("");
        team.setVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
        team.setCollision();
        team.setTeamColor(EnumChatFormat.RESET);
        team.setEntry(score);

        return team;
    }

    /**
     * Destroys the KaxxScoreboard instance.
     * This method removes all entries from the scoreboard, sends a packet to delete
     * the scoreboard objective, and clears the display packet.
     */
    public void destroy() {
        this.hasObjective = false;

        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) {
                continue;
            }

            removeLine(i);
        }

        final ScoreboardObjectivePacket packet = new ScoreboardObjectivePacket();

        packet.setObjectiveName("board");
        packet.setBoardAction(ScoreboardAction.DELETE);
        packet.setTitle("");
        packet.setDisplayType(IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);

        this.connection.sendPacket(packet.build());
        this.connection.sendPacket(displayPacket);
    }

}
