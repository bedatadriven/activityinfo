package org.sigmah.client.util;

/**
 * Utility class to manipulate numbers.
 * 
 * @author tmi
 * 
 */
public final class NumberUtils {

    /**
     * Provides only static methods.
     */
    private NumberUtils() {
    }

    /**
     * Truncate the decimal part of a number to keep only 2 decimals.
     * 
     * @param n
     *            The number, must not be <code>null</code>.
     * @return The truncated number.
     */
    public static String truncate(Number n) {
        return truncate(n, 2);
    }

    /**
     * Truncate the decimal part of a number.
     * 
     * @param n
     *            The number, must not be <code>null</code>.
     * @param decimals
     *            The number of decimals. <code>0</code> will truncate all the
     *            decimal part.
     * @return The truncated number.
     */
    public static String truncate(Number n, int decimals) {

        if (n == null) {
            throw new IllegalArgumentException("n must not be null.");
        }

        if (decimals < 0) {
            throw new IllegalArgumentException("decimals must not be lower than 0.");
        }

        // Retrieves the number as double.
        final Double d = n.doubleValue();
        final String asString = d.toString();

        // Searches the decimal separator.
        final int index = asString.indexOf('.');

        final String truncatedDoubleAsString;

        // If the number as no decimal part, nothing to do.
        if (index == -1) {
            truncatedDoubleAsString = asString;
        }
        // Truncates the decimal part.
        else {

            // Truncates all the decimal part.
            if (decimals == 0) {
                truncatedDoubleAsString = asString.substring(0, index);
            } else {

                final int last = index + 1 + decimals;

                if (last > asString.length()) {
                    truncatedDoubleAsString = asString;
                } else {
                    truncatedDoubleAsString = asString.substring(0, last);
                }
            }
        }

        return truncatedDoubleAsString;
    }

    /**
     * Computes a ratio and returns it as string.
     * 
     * @param n
     *            The number.
     * @param in
     *            The ratio number.
     * @return The ratio.
     */
    public static String ratioAsString(Number n, Number in) {
        return truncate(ratio(n, in)) + " %";
    }

    /**
     * Computes a ratio.
     * 
     * @param n
     *            The number.
     * @param in
     *            The ratio number.
     * @return The ratio.
     */
    public static double ratio(Number n, Number in) {

        if (n == null) {
            throw new IllegalArgumentException("n must not be null.");
        }

        if (in == null) {
            throw new IllegalArgumentException("in must not be null.");
        }

        return n.doubleValue() / in.doubleValue() * 100;
    }
}
