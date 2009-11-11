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

package com.google.gwt.gears.worker.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.worker.client.AbstractWorkerEntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Alex Bertram
 */
public class RpcCallingWorker extends AbstractWorkerEntryPoint {

  public final static String ECHO_BASE_URL = "echo base URL, please";

  public final static String CALL_SERVICE = "call the remote service, please.";

  @Override
  public void onMessageReceived(final MessageEvent event) {

    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable e) {
        getPool().sendMessage("Uncaught exception on worker: " +
            e.getClass().getName() + ": " + e.getMessage(), event.getSender());
      }
    });

    if (ECHO_BASE_URL.equals(event.getBody())) {
      getPool().sendMessage(GWT.getModuleBaseURL(), event.getSender());
    } else if (CALL_SERVICE.equals(event.getBody())) {
      //getPool().sendMessage("going to call the service.", event.getSender());
      try {
        doServiceCall(event);
      } catch (Throwable t) {
        getPool().sendMessage("error during service call: " + t.getClass().getName() +
            ": " + t.getMessage(), event.getSender());
      }
    } else {
      getPool().sendMessage("didn't understand your message: " + event.getBody(), event.getSender());
    }
  }

  private void doServiceCall(final MessageEvent event) {
    WorkerServiceAsync service = (WorkerServiceAsync)
        GWT.create(WorkerService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) service;
    endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "service");

    service.multiply(7, 6, new AsyncCallback<Integer>() {
      @Override
      public void onFailure(Throwable throwable) {
        getPool().sendMessage("worker failed: " + throwable.getMessage(),
            event.getSender());
      }

      @Override
      public void onSuccess(Integer result) {
        getPool().sendMessage(Integer.toString(result),
            event.getSender());
      }
    });

  }
}
