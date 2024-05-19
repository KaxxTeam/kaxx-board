package ca.kaxx.board.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * The ScoreboardObjectivePacket class represents a scoreboard objective packet used in network communication.
 */
public final class ScoreboardObjectivePacket extends PacketDataSerializer {

    /**
     * This class represents a scoreboard objective packet used in network communication.
     */
    public ScoreboardObjectivePacket() {
        super(Unpooled.buffer());
    }

    /**
     * Sets the name of the objective for the scoreboard.
     *
     * @param objectiveName the name of the objective to set.
     *                      Must be non-null.
     */
    public void setObjectiveName(final @Nonnull String objectiveName) {
        a(objectiveName);
    }

    /**
     * Sets the scoreboard action for the board.
     *
     * @param action the scoreboard action to set. Must not be null.
     * @throws NullPointerException if the action is null.
     */
    public void setBoardAction(final @Nonnull ScoreboardAction action) {
        writeByte(action.ordinal());
    }

    /**
     * Sets the title of the scoreboard.
     *
     * @param title the title to set for the scoreboard.
     *              Must be non-null and contain 0 to 48 characters.
     * @throws IllegalArgumentException if the title is null or contains more than 48 characters.
     */
    public void setTitle(final @Nonnull String title) {
        a(title);
    }

    /**
     * Sets the display type of the scoreboard.
     *
     * @param displayType the display type to set for the scoreboard. Must be non-null.
     */
    public void setDisplayType(final @Nonnull IScoreboardCriteria.EnumScoreboardHealthDisplay displayType) {
        a(displayType);
    }

    /**
     * Builds a PacketPlayOutScoreboardObjective object.
     *
     * @return The built PacketPlayOutScoreboardObjective object.
     * @throws RuntimeException if there is an IOException during the build process.
     */
    public PacketPlayOutScoreboardObjective build() {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        try {
            packet.a(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packet;
    }
}
