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
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.linker;

import org.junit.Test;
import org.activityinfo.linker.ApplicationLinker;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;

import java.io.PrintWriter;

/**
 * @author Alex Bertram
 */
public class ApplicationLinkerTest {


    @Test
    public void test() throws UnableToCompleteException {

        TreeLogger logger = new PrintWriterTreeLogger(new PrintWriter(System.out));

        ApplicationLinker linker = new ApplicationLinker();
        linker.readManifestTemplate(logger, "Common.manifest");
        linker.readManifestTemplate(logger, "Permutation.manifest");
        

    }

}
