package org.sigmah.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.google.inject.Injector;

public class InjectDependencies extends Statement {

    private Statement inner;
    private Injector injector;
    private SimpleScope scope;
	private Object test;

    public InjectDependencies(Statement inner, Injector injector, SimpleScope scope, Object test) {
    	this.inner = inner;
    	this.scope = scope;
    	this.test = test;
    	this.injector = injector;
	}

	@Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        errors.clear();
        try {
        	System.out.println("=============++> Entering Test Scope");

        	scope.enter();
        	injector.injectMembers(test);
            inner.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            try {
            	System.out.println("=============++> Exiting Test Scope");
                scope.exit();

            } catch (Throwable e) {
                errors.add(e);
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
