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

package com.google.gwt.gears.worker.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.worker.test.client.WorkerService;
import com.google.gwt.gears.worker.test.client.WorkerServiceAsync;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Alex Bertram
 */
public class RpcTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.google.gwt.gears.worker.WorkerTest";
  }

  public void testRpcCall() {

    WorkerServiceAsync service = (WorkerServiceAsync)
        GWT.create(WorkerService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) service;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "service";
    endpoint.setServiceEntryPoint(moduleRelativeURL);

    service.multiply(6, 7, new AsyncCallback<Integer>() {
      @Override
      public void onFailure(Throwable throwable) {
        fail(throwable.getMessage());
      }

      @Override
      public void onSuccess(Integer result) {
        assertEquals(42, (int) result);
        finishTest();
      }
    });

    delayTestFinish(5000);

  }
}
