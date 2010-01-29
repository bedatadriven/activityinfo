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

package org.activityinfo.clientjre.dispatch.remote;

import org.activityinfo.client.dispatch.remote.CommandRequest;
import org.activityinfo.clientjre.mock.NullAsyncCallback;
import org.activityinfo.shared.command.GetSchema;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

public class CommandRequestTest {

    @Test
    public void equalCommandsShouldBeMerged() {

        assumeThat(new GetSchema(), is(equalTo(new GetSchema())));

        CommandRequest firstCommand = new CommandRequest(new GetSchema(), null,
                new NullAsyncCallback());
        List<CommandRequest> pending = Collections.singletonList(firstCommand);

        CommandRequest secondRequest = new CommandRequest(new GetSchema(), null, new NullAsyncCallback());

        boolean merged = secondRequest.mergeSuccessfulInto(pending);

        assertThat("merged", merged, is(true));
        assertThat(firstCommand.getCallbacks(), hasItem(first(secondRequest.getCallbacks())));

    }

    private <T> T first(Collection<T> items) {
        return items.iterator().next();
    }

}
