package org.activityinfo.server.command;

import java.util.Collections;
import java.util.List;

import org.activityinfo.server.authentication.AuthenticationModuleStub;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.database.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet2;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.Modules;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.teklabs.gwt.i18n.server.LocaleProxy;

/**
 * Test fixture for running hibernate-free 
 * commands.
 *
 * The future.
 *
 */
@Modules({
        TestDatabaseModule.class,
        TemplateModule.class,
        GwtRpcModule.class,
        AuthenticationModuleStub.class
})
public class CommandTestCase2 {
	

    @Inject
    protected CommandServlet2 servlet;
 
    @Inject
    protected Injector injector;

    @Inject
    protected UserDAO users;
    

    protected void setUser(int userId) {
        AuthenticationModuleStub.setUserId(userId);
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
   
    	User user = null;
    	if(AuthenticationModuleStub.getCurrentUser().getUserId()== 0){
    		user = new User(AuthenticationModuleStub.getCurrentUser());
    	}else{
    		user = users.findById(AuthenticationModuleStub.getCurrentUser().getUserId());
            
    	}
    	
    	assert user != null;
        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(user));

        
        List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));

        JdbcScheduler.get().forceCleanup();
        
        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the moment,
        // we'll just add this work aroudn that clears the cache after each command.
//        em.clear();


        CommandResult result = results.get(0);
        if (result instanceof CommandException) {
            throw (CommandException) result;
        }

        return (T) result;
    }
    
    public DispatcherSync getDispatcherSync() {
    	return new DispatcherSync() {
			
			@Override
			public <C extends Command<R>, R extends CommandResult> R execute(C command) {
				try {
					return CommandTestCase2.this.execute(command);
				} catch (CommandException e) {
					throw new RuntimeException(e);
				}
			}
		};
    }

}
