/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

/**
 * 
 */
public class AdminLocalState {
    int version = 0;
    boolean complete = false;
    int lastId;

    public AdminLocalState(String localState) {
        if(localState != null) {
            String[] tokens = localState.split(",");
            version = Integer.parseInt(tokens[0]);

            if(tokens.length == 1) {
                complete = true;
            } else {
                complete = false;
                lastId = Integer.parseInt(tokens[1]);
            }
        }
    }

    @Override
    public String toString() {
        if(complete) {
            return Integer.toString(version);
        } else {
            return version + "," + lastId;
        }
    }
}
