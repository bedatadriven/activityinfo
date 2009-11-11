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
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.workerpool.WorkerPool;
import com.google.gwt.gears.client.workerpool.WorkerPoolMessageHandler;
import com.google.gwt.gears.worker.test.client.RpcCallingWorker;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author Alex Bertram
 */
public class WorkerTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.google.gwt.gears.worker.WorkerTest";
  }

  public void testSimpleWorker() {

    final String msg = "Kilroy was here.";

    WorkerPool pool = Factory.getInstance().createWorkerPool();
    pool.setMessageHandler(new WorkerPoolMessageHandler() {
      @Override
      public void onMessageReceived(MessageEvent event) {

        assertEquals(event.getBody(), msg);
        finishTest();
      }
    });
    int workerId = pool.createWorkerFromUrl(GWT.getModuleBaseURL() + "EchoWorker.js");
    pool.sendMessage(msg, workerId);

    delayTestFinish(5000);
  }

  public void testInitMessage() {

    RpcInitializationMessage msg = RpcInitializationMessage.newInstance();
    assertTrue(RpcInitializationMessage.isA(msg));
    assertEquals(GWT.getModuleBaseURL(), msg.getModuleBaseURL());
  }

  public void testRpcCallingWorkerBaseUrl() {

    WorkerPool pool = Factory.getInstance().createWorkerPool();
    pool.setMessageHandler(new WorkerPoolMessageHandler() {
      @Override
      public void onMessageReceived(MessageEvent event) {
        assertEquals(GWT.getModuleBaseURL(), event.getBody());
        finishTest();
      }
    });
    int workerId = pool.createWorkerFromUrl(GWT.getModuleBaseURL() + "RpcCallingWorker.js");
    pool.sendMessage(RpcInitializationMessage.newInstance(), workerId);
    pool.sendMessage(RpcCallingWorker.ECHO_BASE_URL, workerId);

    delayTestFinish(5000);

  }


}

