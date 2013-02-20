package org.activityinfo.server.endpoint.refine;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.module.SimpleModule;

public class JsonDtoModule extends SimpleModule {

    public JsonDtoModule(String name, Version version) {
        super(name, version);
        addSerializer(new DtoSerializer());
    }

}
