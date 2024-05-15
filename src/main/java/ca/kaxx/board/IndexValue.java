package ca.kaxx.board;

import javax.annotation.Nonnull;

/**
 * IndexValue represents a pair of index and value used in various methods of the KaxxScoreboard class.
 * The index should be between 0 and 15 (inclusive), and the value should not be null.
 */
public record IndexValue(int index, @Nonnull String value) {
}
