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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.gears.client.workerpool.WorkerPool;
import com.google.gwt.gears.client.workerpool.WorkerPoolMessageHandler;

/**
 * @author Alex Bertram
 */
public abstract class AbstractWorkerEntryPoint implements EntryPoint, WorkerPoolMessageHandler {

  @Override
  public void onModuleLoad() {
    registerWorker();
  }

  private static void fireOnMessage(WorkerPoolMessageHandler handler,
                                    MessageEvent event) {
    if (RpcInitializationMessage.isA(event.getBodyObject())) {
      RpcInitializationMessage msg = event.getBodyObject().cast();
      setModuleBase(msg.getModuleBaseURL());
    } else {
      handler.onMessageReceived(event);
    }
  }

  private static native void setModuleBase(String path) /*-{
        $moduleBase = path;
    }-*/;


  private native void registerWorker() /*-{
        var handler = this;
        google.gears.workerPool.onmessage = function(a, b, message) {
            @com.google.gwt.gears.worker.client.AbstractWorkerEntryPoint::fireOnMessage(Lcom/google/gwt/gears/client/workerpool/WorkerPoolMessageHandler;Lcom/google/gwt/gears/client/workerpool/WorkerPoolMessageHandler$MessageEvent;)(handler,message);
        };
    }-*/;

  /**
   * @return The worker pool to which this worker belongs.
   */
  protected native WorkerPool getPool() /*-{
        return google.gears.workerPool;
    }-*/;

  /**
   * Called upon reciept of message.
   *
   * @param event The Message Event
   */
  public void onMessageReceived(MessageEvent event) {

  }


}
