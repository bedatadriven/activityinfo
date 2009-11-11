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

package com.google.gwt.gears.worker.client.rpc;

import com.google.gwt.gears.client.httprequest.HttpRequest;
import com.google.gwt.http.client.Request;

/**
 * Adapts the Gear's Request object to the GWT Request API.
 * This allows us to play nicely with some of the GWT HTTP tools.
 *
 * @author Alex Bertram
 */
public class RequestAdapter extends Request {

  private final HttpRequest httpRequest;

  public RequestAdapter(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  @Override
  public void cancel() {
    httpRequest.abort();
  }

  @Override
  public boolean isPending() {
    return httpRequest.getReadyState() != 4;
  }
}
