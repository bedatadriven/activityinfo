/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler.sync;


public class AdminLocalState {
    private int version = 0;
    private boolean complete = false;
    private int lastId;

    public AdminLocalState(String localState) {
        if(localState != null) {
            String[] tokens = localState.split(",");
            setVersion(Integer.parseInt(tokens[0]));

            if(tokens.length == 1) {
                setComplete(true);
            } else {
                setComplete(false);
                setLastId(Integer.parseInt(tokens[1]));
            }
        }
    }

    @Override
    public String toString() {
        if(isComplete()) {
            return Integer.toString(getVersion());
        } else {
            return getVersion() + "," + getLastId();
        }
    }

	void setVersion(int version) {
		this.version = version;
	}

	int getVersion() {
		return version;
	}

	void setComplete(boolean complete) {
		this.complete = complete;
	}

	boolean isComplete() {
		return complete;
	}

	void setLastId(int lastId) {
		this.lastId = lastId;
	}

	int getLastId() {
		return lastId;
	}
}
