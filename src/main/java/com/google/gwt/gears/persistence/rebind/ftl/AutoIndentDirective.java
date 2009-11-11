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

import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.core.Environment;

import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 * @author Alex Bertram
 */
public class AutoIndentDirective implements TemplateDirectiveModel {


  public void execute(Environment env,
                      Map params, TemplateModel[] loopVars,
                      TemplateDirectiveBody body) throws TemplateException, IOException {

    StringWriter inWriter = new StringWriter();
    body.render(inWriter);

    int depth = 0;

    StringBuffer buffer = new StringBuffer();

    boolean nextLineIsIdented = false;
    boolean continuation = false;
    boolean inMultiLineComment = false;

    BufferedReader lineReader = new BufferedReader(new StringReader(inWriter.getBuffer().toString()));
    while(lineReader.ready()) {

      String line = lineReader.readLine();
      if(line == null)
        break;
      line = line.trim();

      if(line.length() == 0) {
        buffer.append("\n");
        continue;
      }

      if(line.startsWith("}"))
        depth --;

      if(nextLineIsIdented)
        depth ++;

      for(int i=0;i<depth;++i) {
        buffer.append("  ");
      }
      if(continuation)
        buffer.append("    ");

      if(inMultiLineComment && line.startsWith("*"))
        buffer.append(" ");

      buffer.append(line).append("\n");

      if(nextLineIsIdented) {
        depth --;
        nextLineIsIdented = false;
      }

      if(line.startsWith("/*"))
        inMultiLineComment = true;

      String statement = "";
      if(!inMultiLineComment)
        statement = stripComments(line);

      continuation = false;
      if(statement.endsWith("{")) {
        depth++;
      } else if((statement.startsWith("if") || statement.startsWith("else"))) {
        if(!statement.endsWith(";") && !statement.endsWith("}")) {
          nextLineIsIdented = true;
        }
      } else if(!inMultiLineComment && statement.length()>0 &&
          !statement.endsWith(";") && !statement.endsWith("}")) {
        continuation = true;
      }

      if(line.endsWith("*/"))
         inMultiLineComment = false;

    }

    env.getOut().write(buffer.toString());
  }



  private String stripComments(String line) {
    int commentStart = line.indexOf("//");
    if(commentStart != -1)
      return line.substring(0, commentStart).trim();

    commentStart = line.indexOf("/*");
    if(commentStart != -1)
      return line.substring(0, commentStart).trim();

    return line;
  }

}
