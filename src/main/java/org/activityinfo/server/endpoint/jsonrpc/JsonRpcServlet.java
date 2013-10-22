package org.activityinfo.server.endpoint.jsonrpc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.module.SimpleModule;

import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class JsonRpcServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(JsonRpcServlet.class.getName());
    
    private final DispatcherSync dispatcher;
    private final ObjectMapper objectMapper;
    
    @Inject
    public JsonRpcServlet(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
        
        SimpleModule module = new SimpleModule("Command", new Version(1, 0, 0, null));  
        module.addDeserializer(Command.class, new CommandDeserializer());  
        module.addDeserializer(RpcMap.class, new RpcMapDeserializer());
       
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        
        // to ensure that VoidResult is handled without error
        objectMapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS); 
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      
        Command command;
        try {
            String json = new String(ByteStreams.toByteArray(req.getInputStream()));
            command = objectMapper.readValue(json, Command.class);
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to deserialize command", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        CommandResult result = dispatcher.execute(command);
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), result);
    }    
}
