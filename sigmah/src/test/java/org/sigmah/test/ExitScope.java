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
