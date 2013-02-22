

package org.activityinfo.server.command.handler.sync;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
