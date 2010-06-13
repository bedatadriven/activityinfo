package org.activityinfo.server.dao.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class FilterHelper {

    public static List<String> cumulativeList(List<String> tokens, int startIndex) {
        return cumulativeList(tokens, startIndex, tokens.size());
    }

    public static List<String> cumulativeList(List<String> tokens, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        List<String> cumul = new ArrayList<String>(tokens.size());
        cumul.add(tokens.get(startIndex));
        sb.append(tokens.get(startIndex));
        for(int i=startIndex+1; i!=endIndex;++i) {
            sb.append(' ').append(tokens.get(i));
            cumul.add(sb.toString());
        }
        return cumul;
    }

    public static String join(List<String> tokens, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        for(int i=startIndex; i!=endIndex; ++i) {
            if(i>startIndex) {
                sb.append(' ');
            }
            sb.append(tokens.get(i));
        }
        return sb.toString();
    }
}
