/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes an inner statement and then exits the scope
 */
public class ExitScope extends Statement {

    private Statement inner;
    private SimpleScope scope;

    public ExitScope(Statement inner, SimpleScope scope) {
        this.inner = inner;
        this.scope = scope;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        errors.clear();
        try {
            inner.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            try {
                scope.exit();
            } catch (Throwable e) {
                errors.add(e);
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
