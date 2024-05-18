package ca.kaxx.board.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * The ScoreboardAnimation class represents a scoreboard animation with customizable text, colors, and animation effects.
 */
@Getter
@Setter
public final class ScoreboardAnimation {

    /**
     * Represents a private variable for storing text to be animated on the scoreboard.
     */
    private String text,
    /**
     * Represents the current animation state.
     *
     * This variable is used to store the current animation state of a scoreboard.
     */
    currentAnimation;

    /**
     * The primary color used in the ScoreboardAnimation.
     */
    private final ChatColor primaryColor, /**
     * The secondaryColor variable represents the secondary color used in the ScoreboardAnimation class.
     * It is of type ChatColor.
     */
    secondaryColor;

    /**
     * The cooldown in ticks between animation frames.
     */
    private int cooldown;

    /**
     * The index of the character to color within the text.
     * <p>
     * It determines the position of the character that will be colored differently in the animated text.
     * This index is used in the animation effect, where characters are cycled through and colored accordingly.
     * If the current index is at the end of the text, textToColorCharIndex is reset to 0 and cooldown is set to 50.
     *
     * @see ScoreboardAnimation
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int textToColorCharIndex;

    /**
     * Represents a scoreboard animation with customizable text, colors, and animation effects.
     *
     * @param text              The text to be animated on the scoreboard
     * @param primaryColor      The primary color of the text
     * @param secondaryColor    The secondary color of the text
     * @param cooldown          The cooldown in ticks between animation frames
     * @param textToColorCharIndex The index of the character to color within the text
     */
    public ScoreboardAnimation(final @Nonnull String text, final @Nonnull ChatColor primaryColor, final @Nonnull ChatColor secondaryColor, final int cooldown, final int textToColorCharIndex) {
        this.text = text;

        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        this.cooldown = cooldown;
        this.textToColorCharIndex = textToColorCharIndex;
    }

    /**
     * Constructs a new ScoreboardAnimation object with the given parameters.
     *
     * @param text                the text to be animated
     * @param primaryColor        the primary color for the animated text
     * @param secondaryColor      the secondary color for the animated text
     */
    public ScoreboardAnimation(final @Nonnull String text, final @Nonnull ChatColor primaryColor, final @Nonnull ChatColor secondaryColor) {
        this(text, primaryColor, secondaryColor, 5, 0);
    }

    /**
     * Initializes a new ScoreboardAnimation instance with the given text.
     *
     * @param text The text to animate. Cannot be null.
     */
    public ScoreboardAnimation(final @Nonnull String text) {
        this(text, ChatColor.GOLD, ChatColor.WHITE, 5, 0);
    }

    /**
     * Builds the next frame of the animation.
     * If the cooldown is greater than 0, decreases the cooldown and updates the current animation to include the primary color.
     * Otherwise, constructs the formatted text by appending the primary and secondary colors to the text at the specified index.
     * If there are more characters to color, appends the next character with the secondary color.
     * If there are more characters after the next character, appends the remaining text with the primary color.
     * If there are no more characters to color, resets the index and sets the cooldown to 50.
     * Sets the current animation to the constructed formatted text.
     */
    public void buildNext() {
        final String textToColor = text;

        if (this.cooldown > 0) {
            this.cooldown--;

            this.currentAnimation = primaryColor + textToColor;
            return;
        }

        final StringBuilder formattedText = new StringBuilder();

        if (this.textToColorCharIndex > 0) {
            formattedText.append(textToColor, 0, this.textToColorCharIndex - 1);
            formattedText.append(secondaryColor).append(textToColor.charAt(this.textToColorCharIndex - 1));
        } else {
            formattedText.append(textToColor, 0, this.textToColorCharIndex);
        }

        formattedText.append(secondaryColor).append(textToColor.charAt(this.textToColorCharIndex));

        if (this.textToColorCharIndex + 1 < textToColor.length()) {
            formattedText.append(secondaryColor).append(textToColor.charAt(this.textToColorCharIndex + 1));

            if (this.textToColorCharIndex + 2 < textToColor.length()) {
                formattedText.append(primaryColor).append(textToColor.substring(this.textToColorCharIndex + 2));
            }

            this.textToColorCharIndex++;
        } else {
            this.textToColorCharIndex = 0;
            this.cooldown = 50;
        }

        this.currentAnimation = primaryColor + formattedText.toString();
    }

    /**
     * Retrieves the current animated text.
     *
     * @return The current animated text as a String.
     */
    public String getAnimatedText() {
        return currentAnimation;
    }

}