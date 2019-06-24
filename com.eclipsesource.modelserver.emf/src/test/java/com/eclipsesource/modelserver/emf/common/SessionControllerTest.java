/*******************************************************************************
 * Copyright (c) 2019 EclipseSource and others.
 *
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License v. 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0.
 *
 *   This Source Code may also be made available under the following Secondary
 *   Licenses when the conditions for such availability set forth in the Eclipse
 *   Public License v. 2.0 are satisfied: GNU General Public License, version 2
 *   with the GNU Classpath Exception which is available at
 *   https://www.gnu.org/software/classpath/license.html.
 *
 *   SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 *******************************************************************************/
package com.eclipsesource.modelserver.emf.common;

import io.javalin.websocket.WsConnectContext;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SessionControllerTest {

    @Test
    public void createSession() {
        String sessionId = UUID.randomUUID().toString();
        SessionController sessionController = new SessionController();
        final SessionController spy = spy(sessionController);
        doReturn(sessionId).when(spy).createSessionId();
        final WsConnectContext client = mock(WsConnectContext.class);
        when(client.send(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        when(client.pathParam("modeluri")).thenReturn("test");
        spy.createSession(client, client.pathParam("modeluri"));
        assertTrue(spy.hasSession(sessionId));
    }
}
