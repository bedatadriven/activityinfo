/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.jsonrpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.server.endpoint.gwtrpc.CommandServlet;
import org.sigmah.server.endpoint.jsonrpc.serde.SyncRegionUpdateSerializer;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.InvalidAuthTokenException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * An adapter class for the GWT-RPC interface that allows
 * non-GWT clients to execute commands using the {@code Command}
 * encoded as JSON.
 *
 */
@Singleton
public class JsonCommandServlet extends HttpServlet {

    private final CommandServlet commandServlet;
    private final Gson gson;

    @Inject
    public JsonCommandServlet(CommandServlet commandServlet) {
        this.commandServlet = commandServlet;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(SyncRegionUpdate.class, new SyncRegionUpdateSerializer())
                .create();
    }

    /**
     * Only handles commands for {@code Command} classes prefixed by "Get"
     * Properties of the {@code Command} object should be provided by url parameters
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String commandName = "org.sigmah.shared.command.Get" + parseCommandName(req);
            Command command = unmarshalCommandFromParameters(commandName, req);

            CommandResult result = commandServlet.execute(getAuthToken(req), command);

            resp.setContentType("application/json");
            resp.getWriter().print(gson.toJson(result));

        } catch (BadRequestException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        } catch (InvalidAuthTokenException e) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (CommandException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String getAuthToken(HttpServletRequest req) {

        String authToken = req.getHeader("X-ActivityInfo-AuthToken");
        if(authToken != null && authToken.length()!=0) {
            return authToken;
        }

        authToken = req.getParameter("authToken");

        return authToken;
    }


    private String parseCommandName(HttpServletRequest req) throws BadRequestException {
        String uri = req.getRequestURI();
        int slash = uri.lastIndexOf('/');
        if(slash == -1) {
            throw new BadRequestException("Expected url in the form /rpc/CommandName");
        }
        return uri.substring(slash+1);
    }



    @LogException
    private Command unmarshalCommandFromParameters(String commandName, HttpServletRequest req) throws ServletException, BadRequestException {
        Command command;

        try {
            command = (Command) Class.forName(commandName).newInstance();
        } catch (InstantiationException e) {
            throw new ServletException("Exception instantiating Command object", e);
        } catch (IllegalAccessException e) {
            throw new ServletException("Exception instantiating Command object", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Exception instantiating Command object", e);
        }

        // look for setters
        for(Method method : command.getClass().getMethods()) {
            if(isSetter(method)) {
                String property = propertyNameFromSetter(method);
                if(req.getParameterValues(property) != null) {
                    try {
                        method.invoke(command, convert(property, req.getParameter(property), method.getParameterTypes()[0]));
                    } catch (IllegalAccessException e) {
                        throw new ServletException(e);
                    } catch (InvocationTargetException e) {
                        throw new ServletException(e);
                    }
                }
            }
        }

        return command;
    }

    private Object convert(String name, String parameter, Class aClass) throws BadRequestException, ServletException {
        try {
            if(aClass.equals(String.class)) {
                return parameter;
            } else if(aClass.equals(Integer.class) || aClass.equals(Integer.TYPE)) {
                return Integer.parseInt(parameter);
            } else if(aClass.equals(Double.class) || aClass.equals(Double.TYPE))  {
                return Double.parseDouble(parameter);
            } else if(aClass.equals(Long.class) || aClass.equals(Long.TYPE)) {
                return Long.parseLong(parameter);
            } else if(aClass.equals(Boolean.class) || aClass.equals(Boolean.TYPE) ) {
                return "false".equals(parameter.toLowerCase());
            } else {
                throw new ServletException("No converter implemented yet for type " + aClass.getName()) ;
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Could not parse parameter '" + name + "' as integer");
        }
    }

    private String propertyNameFromSetter(Method method) {
        return method.getName().substring(3,4).toLowerCase() + method.getName().substring(4);
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") && Modifier.isPublic(method.getModifiers()) &&
                !Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 1;
    }
}
