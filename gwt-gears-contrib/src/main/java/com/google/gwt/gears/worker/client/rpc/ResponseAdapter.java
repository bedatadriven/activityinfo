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
import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Response;

/**
 * Adapts the Gear's API to the GWT Response object.
 *
 * @author Alex Bertram
 */
public class ResponseAdapter extends Response {

  private final HttpRequest httpRequest;

  private class HeaderAdapter extends Header {

    private String name;
    private String value;

    private HeaderAdapter(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return null;
    }

    @Override
    public String getValue() {
      return null;
    }
  }

  public ResponseAdapter(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  @Override
  public String getHeader(String header) {
    return httpRequest.getResponseHeader(header);
  }

  @Override
  public Header[] getHeaders() {
    String[] lines = httpRequest.getAllResponseHeaders().split("\n\r");
    Header[] headers = new Header[lines.length];
    for (int i = 0; i != lines.length; ++i) {
      int semiIndex = lines[i].indexOf(':');
      headers[i] = new HeaderAdapter(
          lines[i].substring(0, semiIndex - 1),
          lines[i].substring(semiIndex + 1).trim());
    }
    return headers;
  }

  @Override
  public String getHeadersAsString() {
    return httpRequest.getAllResponseHeaders();
  }

  @Override
  public int getStatusCode() {
    return httpRequest.getStatus();
  }

  @Override
  public String getStatusText() {
    return httpRequest.getStatusText();
  }

  @Override
  public String getText() {
    return httpRequest.getResponseText();
  }
}
