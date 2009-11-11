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

package com.google.gwt.gears.persistence.rebind.ftl;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * @author Alex Bertram
 */
public class SingleLineDirective implements TemplateDirectiveModel {


  public void execute(Environment env,
                      Map params, TemplateModel[] loopVars,
                      TemplateDirectiveBody body)
      throws TemplateException, IOException {
    // Check if no parameters were given:
    if (!params.isEmpty()) {
      throw new TemplateModelException(
          "This directive doesn't allow parameters.");
    }
    if (loopVars.length != 0) {
      throw new TemplateModelException(
          "This directive doesn't allow loop variables.");
    }

    // If there is non-empty nested content:
    if (body != null) {
      // Executes the nested body. Same as <#nested> in FTL, except
      // that we use our own writer instead of the current output writer.
      body.render(new UpperCaseFilterWriter(env.getOut()));
    } else {
      throw new RuntimeException("missing body");
    }
  }

  /**
   * A {@link Writer} that transforms the character stream to upper case
   * and forwards it to another {@link Writer}.
   */
  private static class UpperCaseFilterWriter extends Writer {

    private final Writer inner;

    UpperCaseFilterWriter(Writer inner) throws IOException {
      this.inner = inner;
    }

    public void write(char[] cbuf, int off, int len)
        throws IOException {
      char[] transformedCbuf = new char[len];
      int i = 0, j = 0;
      boolean lastWasWhitespace = false;
      while (i < len) {
        char c = cbuf[(off + i)];
        if (c == 10 || c == 13)
          c = ' ';
        if (!Character.isWhitespace(c) || !lastWasWhitespace)
          transformedCbuf[j++] = c;

        lastWasWhitespace = Character.isWhitespace(c);
        i++;
      }
      inner.write(transformedCbuf, 0, j);
    }

    public void flush() throws IOException {
      inner.flush();
    }

    public void close() throws IOException {
      inner.close();
    }
  }

}
