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

package com.google.gwt.gears.worker.linker;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.CompilationResult;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.dev.util.Util;
import com.google.gwt.util.tools.Utility;

import java.io.IOException;
import java.util.SortedSet;

/**
 * @author Alex Bertram
 */
@LinkerOrder(LinkerOrder.Order.PRIMARY)
public class WorkerLinker extends AbstractLinker {


  @Override
  public String getDescription() {
    return "Gears Worker Linker";
  }

  protected static void replaceAll(StringBuffer buf, String search,
                                   String replace) {
    int len = search.length();
    for (int pos = buf.indexOf(search); pos >= 0; pos = buf.indexOf(search,
        pos + 1)) {
      buf.replace(pos, pos + len, replace);
    }
  }

  @Override
  public ArtifactSet link(TreeLogger logger, LinkerContext context, ArtifactSet artifacts)
      throws UnableToCompleteException {

    logger = logger.branch(TreeLogger.Type.INFO, "Linking " + context.getModuleName() +
        " with WorkerLinker");

    SortedSet<CompilationResult> results = artifacts.find(CompilationResult.class);
    if (results.size() != 1) {
      logger.log(TreeLogger.Type.ERROR, "Workers can only have one permutation. Make sure you're not " +
          "referencing DOM code, etc.");
      throw new UnableToCompleteException();
    }

    CompilationResult result = results.first();
    if (result.getJavaScript().length != 1) {
      logger.log(TreeLogger.Type.ERROR, "The CompilationResult must contain only one fragment. (Are you trying to use GWT.runAsync)?");
    }

    StringBuffer script = new StringBuffer();
    // the Compilation result includes only definitions, so put them first
    script.append(result.getJavaScript()[0]);


    StringBuffer template;
    try {
      template = new StringBuffer(
          Utility.getFileFromClassPath("com/google/gwt/gears/worker/linker/WorkerTemplate.js"));
    } catch (IOException e) {
      logger.log(TreeLogger.Type.ERROR, "Could not load the template script.");
      throw new UnableToCompleteException();
    }

    replaceAll(template, "__MODULE_NAME__", context.getModuleName());
    replaceAll(template, "__STRONG_NAME__", result.getStrongName());
    script.append(template.toString());

    // we want to output a single js file
    ArtifactSet toReturn = new ArtifactSet(artifacts);
    toReturn.add(emitBytes(logger, Util.getBytes(script.toString()),
        context.getModuleName() + ".js"));

    return toReturn;
  }
}
