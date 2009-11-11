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

/**
 * FreeMarker template directive that removes the last comma from a list,
 * if the comma is present.
 *
 * @author Alex Bertram
 */
public class CsvDirective implements TemplateDirectiveModel {

  public void execute(Environment environment,
                      Map map,
                      TemplateModel[] templateModels,
                      TemplateDirectiveBody body)
      throws TemplateException, IOException {


    StringWriter writer = new StringWriter();
    body.render(writer);

    String s = writer.toString().trim();
    if(s.endsWith(",")) {
      s = s.substring(0, s.length()-1);
    }

    environment.getOut().write(s);

  }
}
