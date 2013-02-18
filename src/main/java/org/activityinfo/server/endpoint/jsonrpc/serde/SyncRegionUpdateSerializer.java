/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.jsonrpc.serde;

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
