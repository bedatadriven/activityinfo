package org.activityinfo.server.util;

public class Jaro {
    /**
     * gets the similarity of the two strings using Jaro distance.
     *
     * @param string1 the first input string
     * @param string2 the second input string
     * @return a value between 0-1 of the similarity
     */
    public float getSimilarity(final String string1, final String string2) {

        //get half the length of the string rounded up - (this is the distance used for acceptable transpositions)
        final int halflen = ((Math.min(string1.length(), string2.length())) / 2) + ((Math.min(string1.length(), string2.length())) % 2);

        //get common characters
        final StringBuilder common1 = getCommonCharacters(string1, string2, halflen);
        final StringBuilder common2 = getCommonCharacters(string2, string1, halflen);

        //check for zero in common
        if (common1.length() == 0 || common2.length() == 0) {
            return 0.0f;
        }

        //check for same length common strings returning 0.0f is not the same
        if (common1.length() != common2.length()) {
            return 0.0f;
        }

        //get the number of transpositions
        int transpositions = 0;
        int n=common1.length();
        for (int i = 0; i < n; i++) {
            if (common1.charAt(i) != common2.charAt(i))
                transpositions++;
        }
        transpositions /= 2.0f;

        //calculate jaro metric
        return (common1.length() / ((float) string1.length()) +
                common2.length() / ((float) string2.length()) +
                (common1.length() - transpositions) / ((float) common1.length())) / 3.0f;
    }

    /**
     * returns a string buffer of characters from string1 within string2 if they are of a given
     * distance seperation from the position in string1.
     *
     * @param string1
     * @param string2
     * @param distanceSep
     * @return a string buffer of characters from string1 within string2 if they are of a given
     *         distance seperation from the position in string1
     */
    private static StringBuilder getCommonCharacters(final String string1, final String string2, final int distanceSep) {
        //create a return buffer of characters
        final StringBuilder returnCommons = new StringBuilder();
        //create a copy of string2 for processing
        final StringBuilder copy = new StringBuilder(string2);
        //iterate over string1
        int n=string1.length();
        int m=string2.length();
        for (int i = 0; i < n; i++) {
            final char ch = string1.charAt(i);
            //set boolean for quick loop exit if found
            boolean foundIt = false;
            //compare char with range of characters to either side

            for (int j = Math.max(0, i - distanceSep); !foundIt && j < Math.min(i + distanceSep, m - 1); j++) {
                //check if found
                if (copy.charAt(j) == ch) {
                    foundIt = true;
                    //append character found
                    returnCommons.append(ch);
                    //alter copied string2 for processing
                    copy.setCharAt(j, (char)0);
                }
            }
        }
        return returnCommons;
    }
}