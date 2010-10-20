package org.sigmah.client.page.project.logframe;

/**
 * Defines a code displayer.
 * 
 * @author tmi
 * 
 * @param <T>
 *            The type
 */
public abstract class CodePolicy<T> {

    /**
     * Gets the character indexed by the given <code>index</code> in the
     * alphabet. Indexes start at <code>0</code> and ends at <code>25</code>.<br/>
     * <br/>
     * <ul>
     * <li>0 -> a</li>
     * <li>1 -> b</li>
     * <li>2 -> c</li>
     * <li>...</li>
     * <li>25 -> z</li>
     * </ul>
     * 
     * @param index
     *            The character index.
     * @param upper
     *            Upper character?
     * @return The corresponding character.
     */
    public static char getLetter(int index, boolean upper) {
        return getLetter(index, upper, 0);
    }

    /**
     * Gets the character indexed by the given <code>index</code> in the
     * alphabet. Indexes start at <code>start</code> and ends at
     * <code>start + 25</code>.<br/>
     * <br/>
     * <ul>
     * <li>start -> a</li>
     * <li>start + 1 -> b</li>
     * <li>start + 2 -> c</li>
     * <li>...</li>
     * <li>start + 25 -> z</li>
     * </ul>
     * 
     * @param index
     *            The character index.
     * @param upper
     *            Upper character?
     * @param start
     *            The index of the first letter <code>a</code>.
     * @return The corresponding character.
     */
    public static char getLetter(int index, boolean upper, int start) {

        // Adjusts index.
        index = start > 0 ? index - start : index + start;

        // Checks the index.
        if (index < 0 || index > 25) {
            throw new IllegalArgumentException("The index #" + index
                    + " doesn't not refer to a valid alphabetical character (must be between 0 and 25).");
        }

        // Computes the ascii code shift to get alphabetical characters.
        final int shift;
        if (upper) {
            shift = 65;
        } else {
            shift = 97;
        }

        return (char) (index + shift);
    }

    /**
     * Gets the string representation of the code.
     * 
     * @param code
     *            The integer code.
     * @param userObject
     *            The user object.
     * @return The code as a string.
     */
    public abstract String getCode(int code, T userObject);
}
