package org.activityinfo.server.command;


import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.AuthenticationModuleStub;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet2;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.endpoint.gwtrpc.ServerExecutionContext;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;

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
        MockHibernateModule.class,
        TemplateModule.class,
        GwtRpcModule.class,
        AuthenticationModuleStub.class
})
public class CommandTestCase2 {
	

    @Inject
    protected CommandServlet2 servlet;
 
    @Inject
    protected Injector injector;

    
    protected void setUser(int userId) {
        AuthenticationModuleStub.setUserId(userId);
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
   
    	User user = null;
    	if(AuthenticationModuleStub.getCurrentUser().getUserId()== 0){
    		user = new User();
    		user.setName("Anonymous");
    		user.setEmail("Anonymous@anonymous");
    	} else {
    		user = new User();
    		user.setId(AuthenticationModuleStub.getCurrentUser().getUserId());
    		user.setEmail("foo@foo.com");
    		user.setName("Foo Name");
    		user.setLocale("en");
    	}
    	
    	assert user != null;
        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(user));

        
        ServerExecutionContext context = new ServerExecutionContext(injector);
        T result =  context.startExecute(command);
        
        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the moment,
        // we'll just add this work aroudn that clears the cache after each command.
        injector.getInstance(EntityManager.class).clear();
        return result;
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
