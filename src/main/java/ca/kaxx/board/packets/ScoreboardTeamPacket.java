package ca.kaxx.board.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This class represents a Scoreboard Team Packet.
 * It extends the PacketDataSerializer class.
 */
public final class ScoreboardTeamPacket extends PacketDataSerializer {

    /**
     * Constructs a new ScoreboardTeamPacket object.
     * This constructor initializes the underlying buffer using the Unpooled.buffer() method from the parent class PacketDataSerializer.
     */
    public ScoreboardTeamPacket() {
        super(Unpooled.buffer());
    }

    /**
     * Sets the name of the scoreboard team.
     *
     * @param name The name of the scoreboard team. Must not be null.
     */
    public void setName(final @Nonnull String name) {
        a(name);
    }

    /**
     * Sets the specified team action for the scoreboard.
     *
     * @param boardAction The team action to be set. Must not be null.
     */
    public void setTeamAction(final @Nonnull ScoreboardAction boardAction) {
        writeByte(boardAction.ordinal());
    }

    /**
     * Sets a custom name for the scoreboard team.
     *
     * @param customName The custom name to set for the team. Must be non-null.
     */
    public void setCustomName(final @Nonnull String customName) {
        a(customName);
    }

    /**
     * Sets the prefix for the scoreboard team.
     *
     * @param prefix The prefix to set for the team. Must not be null.
     */
    public void setPrefix(final @Nonnull String prefix) {
        a(prefix);
    }

    /**
     * Sets the suffix for the scoreboard team.
     *
     * @param suffix The suffix to set for the scoreboard team. Must not be null.
     */
    public void setSuffix(final @Nonnull String suffix) {
        a(suffix);
    }

    /**
     * Sets the visibility of the scoreboard team name tag.
     *
     * @param visibility The visibility option for the team name tag. Cannot be null.
     */
    public void setVisibility(final @Nonnull ScoreboardTeamBase.EnumNameTagVisibility visibility) {
        writeByte(visibility.ordinal());
    }

    /**
     * Sets the collision value for a scoreboard team packet.
     */
    public void setCollision() {
        a("never");
    }

    /**
     * Sets the team color for the scoreboard team.
     *
     * @param color The color to set for the team. Must not be null.
     * @throws NullPointerException if the color is null.
     */
    public void setTeamColor(final @Nonnull EnumChatFormat color) {
        writeByte(color.b());
    }

    /**
     * Sets the entry for a scoreboard team.
     *
     * @param entry The entry for the scoreboard team. Cannot be null.
     */
    public void setEntry(final @Nonnull String entry) {
        b(1);
        a(entry);
    }

    /**
     * Builds a PacketPlayOutScoreboardTeam object.
     *
     * @return The built PacketPlayOutScoreboardTeam object.
     * @throws RuntimeException If an IOException occurs during the build process.
     */
    public PacketPlayOutScoreboardTeam build() {
        final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        try {
            packet.a(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packet;
    }
}
