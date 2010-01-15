/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */
package org.activityinfo.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.dao.hibernate.LoadDataSet;
import org.junit.AfterClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InjectionSupport extends BlockJUnit4ClassRunner {
    private Injector injector;
    private TestScopeModule scopeModule;
    private List<Module> modules;

    public InjectionSupport(Class<?> klass) throws InitializationError {
        super(klass);

        modules = new ArrayList<Module>();


        scopeModule = new TestScopeModule();
        modules.add(scopeModule);

        Modules moduleClasses = klass.getAnnotation(Modules.class);
        for (Class moduleClass : moduleClasses.value()) {
            try {
                modules.add((Module) moduleClass.getConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Exception thrown while creating modules for test " +
                        klass.getName() + ":\n   could not instantiate module class " +
                        moduleClass.getName(), e);
            }
        }
        injector = Guice.createInjector(modules);
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        // We'll just have to wait for Guice to throw errors if there is a problem
    }

    @Override
    protected Object createTest() throws Exception {
        scopeModule.getTestScope().enter();
        return injector.getInstance(getTestClass().getJavaClass());
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        Statement statement = super.classBlock(notifier);
        for (Module module : modules) {
            statement = withModuleAfterClasses(statement, module);
        }
        return statement;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement = super.methodInvoker(method, test);

        statement = withLoadDataSets(method, statement, test);
        statement = new ExitScope(statement, scopeModule.getTestScope());
        return statement;
    }

    private Statement withLoadDataSets(FrameworkMethod method, Statement statement, Object target) {
        OnDataSet ods = method.getAnnotation(OnDataSet.class);

        if (ods == null)
            ods = target.getClass().getAnnotation(OnDataSet.class);

        return ods == null ? statement :
                new LoadDataSet(injector.getInstance(EntityManagerFactory.class), statement, ods.value(), target);
    }

    /**
     * Returns a {@link Statement}: run all non-overridden {@code @AfterClass} methods on this class
     * and superclasses before executing {@code statement}; all AfterClass methods are
     * always executed: exceptions thrown by previous steps are combined, if
     * necessary, with exceptions from AfterClass methods into a
     * {@link org.junit.internal.runners.model.MultipleFailureException}.
     */
    protected Statement withModuleAfterClasses(Statement statement, Module module) {
        List<FrameworkMethod> afters = getAnnotatedModuleMethods(module, AfterClass.class);
        return afters.isEmpty() ? statement :
                new RunAfters(statement, afters, module);
    }

    protected List<FrameworkMethod> getAnnotatedModuleMethods(Module module, Class annotationClass) {
        List<FrameworkMethod> annotated = new ArrayList<FrameworkMethod>();
        for (Method method : module.getClass().getMethods()) {
            if (method.getAnnotation(annotationClass) != null) {
                annotated.add(new FrameworkMethod(method));
            }
        }
        return annotated;
    }


    public static class TestScopeModule extends AbstractModule {

        private SimpleScope testScope;

        public TestScopeModule() {
        }

        @Override
        protected void configure() {
            testScope = new SimpleScope();

            // tell Guice about the scope
            bindScope(TestScoped.class, testScope);

            // make our scope instance injectable
            bind(SimpleScope.class)
                    .annotatedWith(Names.named("test"))
                    .toInstance(testScope);
        }

        public SimpleScope getTestScope() {
            return testScope;
        }

    }
}
