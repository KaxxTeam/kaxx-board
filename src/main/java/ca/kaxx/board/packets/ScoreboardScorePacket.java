package ca.kaxx.board.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Represents a packet used to send a scoreboard score.
 * This packet is used to commit a score to the scoreboard.
 */
public final class ScoreboardScorePacket extends PacketDataSerializer {

    /**
     * Represents a packet used to send a scoreboard score.
     * This packet is used to commit a score to the scoreboard.
     */
    public ScoreboardScorePacket() {
        super(Unpooled.buffer());
    }

    /**
     * Sets the name for the scoreboard score.
     *
     * @param name The name to set for the scoreboard score. Must not be null.
     */
    public void setName(final @Nonnull String name) {
        a(name);
    }

    /**
     * Sets the scoreboard action for the board.
     *
     * @param scoreboardAction The action to set for the scoreboard. Cannot be null.
     */
    public void setBoardAction(final @Nonnull PacketPlayOutScoreboardScore.EnumScoreboardAction scoreboardAction) {
        a(scoreboardAction);
    }

    /**
     * Sets the objective name for a scoreboard score packet.
     *
     * @param objectiveName The name of the objective. Cannot be null.
     */
    public void setObjectiveName(final @Nonnull String objectiveName) {
        a(objectiveName);
    }

    /**
     * Sets the score for the scoreboard. The score represents the value to be displayed on the scoreboard.
     *
     * @param score The score to set. Must be an integer value.
     * @throws IllegalArgumentException if the score is not a valid integer value.
     */
    public void setScore(final int score) {
        b(score);
    }

    /**
     * Builds a PacketPlayOutScoreboardScore object.
     *
     * @return The built PacketPlayOutScoreboardScore object.
     * @throws RuntimeException if an IOException occurs during building.
     */
    public PacketPlayOutScoreboardScore build() {
        final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

        try {
            packet.a(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packet;
    }
}
