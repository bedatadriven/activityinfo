

package org.activityinfo.server.endpoint.jsonrpc.serde;

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

import java.lang.reflect.Type;

import org.activityinfo.shared.command.result.SyncRegionUpdate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SyncRegionUpdateSerializer implements JsonSerializer<SyncRegionUpdate> {

    private final JsonParser parser;

    public SyncRegionUpdateSerializer() {
        parser = new JsonParser();
    }

    @Override
    public JsonElement serialize(SyncRegionUpdate update, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("version", new JsonPrimitive(update.getVersion()));
        object.add("complete", new JsonPrimitive(update.isComplete()));
        if(update.getSql() != null) {
            object.add("sql", parser.parse(update.getSql()));
        }
        return object;
    }
}
