package org.activityinfo.server.endpoint.jsonrpc;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.node.ObjectNode;

import com.extjs.gxt.ui.client.data.RpcMap;

public class RpcMapDeserializer extends StdDeserializer<RpcMap> {

    public RpcMapDeserializer() {
        super(RpcMap.class);
    }

    @Override
    public RpcMap deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();  
        ObjectNode root = (ObjectNode) mapper.readTree(jp);  
        
        RpcMap map = new RpcMap();
        Iterator<Map.Entry<String, JsonNode>> fieldIt = root.getFields();
        while(fieldIt.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldIt.next();
            if(field.getValue().isNumber()) {
                map.put(field.getKey(), field.getValue().getNumberValue());
            } else if(field.getValue().isBoolean()) {
                map.put(field.getKey(), field.getValue().asBoolean());
            } else if(field.getValue().isTextual()) {
                map.put(field.getKey(), field.getValue().asText());
            } 
        }
        return map;
    }
    
    

}
