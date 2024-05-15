package ca.kaxx.board.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Represents a packet for displaying a scoreboard objective.
 */
public final class ScoreboardDisplayObjectivePacket extends PacketDataSerializer {

    /**
     * Represents a packet for displaying a scoreboard objective.
     */
    public ScoreboardDisplayObjectivePacket() {
        super(Unpooled.buffer());
    }

    /**
     * Sets the type of the scoreboard.
     *
     * @param boardType The type of the scoreboard. Must be non-null.
     */
    public void setType(final @Nonnull Type boardType) {
        writeByte(boardType.ordinal());
    }

    /**
     * Sets the name of the objective.
     *
     * @param objectiveName The name of the objective to set. Cannot be null.
     */
    public void setObjectiveName(final @Nonnull String objectiveName) {
        a(objectiveName);
    }

    /**
     * Builds a PacketPlayOutScoreboardDisplayObjective object.
     *
     * @return The built PacketPlayOutScoreboardDisplayObjective object.
     * @throws RuntimeException if an IOException occurs during the building process.
     */
    public PacketPlayOutScoreboardDisplayObjective build() {
        final PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        try {
            packet.a(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packet;
    }

    public enum Type {
        TAB_LIST, SCORE, PLAYER
    }
}
