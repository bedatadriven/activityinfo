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

package com.google.gwt.gears.persistence.rebind;

import com.google.gwt.gears.persistence.rebind.ftl.SingleLineDirective;
import com.google.gwt.gears.persistence.rebind.ftl.CsvDirective;
import com.google.gwt.gears.persistence.rebind.ftl.AutoIndentDirective;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * @author Alex Bertram
 */
public class Templates {

  public static Configuration get() {
    Configuration cfg = new Configuration();
    // Specify the data source where the template files come from.
    cfg.setClassForTemplateLoading(Templates.class, "/persistence/");
    cfg.setDefaultEncoding("UTF-8");

    // Specify how templates will see the data-model. This is an advanced topic...
    // but just use this:
    cfg.setObjectWrapper(new DefaultObjectWrapper());

    cfg.setSharedVariable("singleline", new SingleLineDirective());
    cfg.setSharedVariable("csv", new CsvDirective());
    cfg.setSharedVariable("autoindent", new AutoIndentDirective());

    return cfg;
  }
}
