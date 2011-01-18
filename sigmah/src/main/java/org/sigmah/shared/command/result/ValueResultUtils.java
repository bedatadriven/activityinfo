package org.sigmah.shared.command.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

/**
 * Utility class used to manipulate the values of the flexible elements.
 * 
 * @author tmi
 * 
 */
public final class ValueResultUtils {

    public static final String DEFAULT_VALUE_SEPARATOR = "~";

    /**
     * Provides only static methods.
     */
    private ValueResultUtils() {

    }

    /**
     * Split a list of values (manages entities with Long type id).
     * 
     * @param values
     *            The values list as a single string.
     * @return The values.
     */
    public static List<Long> splitValuesAsLong(Object values) {

        final ArrayList<Long> list = new ArrayList<Long>();

        try {
            if (values != null) {

                final String valuesAsString = (String) values;

                final String[] split = valuesAsString.trim().split(ValueResultUtils.DEFAULT_VALUE_SEPARATOR);

                if (split != null && split.length != 0) {
                    for (final String value : split) {
                        if (value != null && !value.trim().equals("")) {
                            list.add(Long.valueOf(value));
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            // digest exception.
        }

        return list;
    }

    /**
     * Split a list of values (manages entities with Integer type id).
     * 
     * @param values
     *            The values list as a single string.
     * @return The values.
     */
    public static List<Integer> splitValuesAsInteger(Serializable values) {

        final ArrayList<Integer> list = new ArrayList<Integer>();

        try {
            if (values != null) {

                final String valuesAsString = (String) values;

                final String[] split = valuesAsString.trim().split(DEFAULT_VALUE_SEPARATOR);

                if (split != null && split.length != 0) {
                    for (final String value : split) {
                        if (value != null) {
                            list.add(Integer.valueOf(value));
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            // digest exception.
        }

        return list;
    }

    /**
     * Merges a list of values as a single string.<br/>
     * <br/>
     * The <code>null</code> values are ignored.
     * 
     * @param <T>
     *            The type of the values.
     * @param values
     *            The values list.
     * @return The values list as a single string.
     */
    public static <T extends EntityDTO> String mergeValues(List<T> values) {

        final StringBuilder sb = new StringBuilder();

        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {

                final T value = values.get(i);

                if (value != null) {
                    sb.append(String.valueOf(value.getId()));
                    if (i < values.size() - 1) {
                        sb.append(DEFAULT_VALUE_SEPARATOR);
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Merges a list of elements as a single string.<br/>
     * <br/>
     * The <code>null</code> values are ignored.
     * 
     * @param <T>
     *            The type of the elements.
     * @param values
     *            The values list.
     * @return The values list as a single string.
     */
    public static <T extends Serializable> String mergeElements(List<T> values) {

        final StringBuilder sb = new StringBuilder();

        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {

                final T value = values.get(i);

                if (value != null) {
                    sb.append(String.valueOf(value));
                    if (i < values.size() - 1) {
                        sb.append(DEFAULT_VALUE_SEPARATOR);
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Merges a list of elements as a single string.<br/>
     * <br/>
     * The <code>null</code> values are ignored.
     * 
     * @param <T>
     *            The type of the elements.
     * @param values
     *            The values list.
     * @return The values list as a single string.
     */
    public static <T extends Serializable> String mergeElements(T... values) {

        final StringBuilder sb = new StringBuilder();

        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {

                final T value = values[i];

                if (value != null) {
                    sb.append(String.valueOf(value));
                    if (i < values.length - 1) {
                        sb.append(DEFAULT_VALUE_SEPARATOR);
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Split a list of values.
     * 
     * @param values
     *            The values list as a single string.
     * @return The values.
     */
    public static List<String> splitElements(String values) {

        final ArrayList<String> list = new ArrayList<String>();

        try {
            if (values != null) {

                final String[] split = values.trim().split(DEFAULT_VALUE_SEPARATOR);

                if (split != null && split.length != 0) {
                    for (final String value : split) {
                        if (value != null) {
                            list.add(String.valueOf(value));
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            // digest exception.
        }

        return list;
    }
}
