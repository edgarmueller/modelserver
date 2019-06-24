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

import com.eclipsesource.modelserver.emf.ResourceManager;
import com.eclipsesource.modelserver.emf.configuration.ServerConfiguration;
import com.eclipsesource.modelserver.jsonschema.Json;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsHandler;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionController extends WsHandler {

    private Map<String, Set<WsConnectContext>> sessionIdToClients = Maps.newConcurrentMap();
    private Map<String, String> sessionIdToModelUri = Maps.newConcurrentMap();

    @Inject
    private ResourceManager resourceManager;

    @Inject
    private ServerConfiguration serverConfiguration;

    public void createSession(WsConnectContext ctx, String modeluri) {
        final String sessionId = createSessionId();
        final Set<WsConnectContext> clients = ConcurrentHashMap.newKeySet();
        clients.add(ctx);
        sessionIdToClients.put(sessionId, clients);
        sessionIdToModelUri.put(sessionId, modeluri);
        ctx.send(WsResponse.success(Json.object(Json.prop("sessionId", Json.text(sessionId)))));
    }

    public void subscribeSession(WsConnectContext ctx, String sessionId) {
        Optional
            .ofNullable(
                sessionIdToClients.computeIfPresent(sessionId, (k, clients) -> {
                    clients.add(ctx);
                    return clients;
                })
            ).map(clients -> ctx.send(WsResponse.success(Json.object(Json.prop("sessionId", Json.text(sessionId))))))
            .orElseGet(() -> ctx.send(WsResponse.error("Session " + sessionId + " not found")));
    }

    public void sessionChanged(String sessionId) {
        sessionIdToClients.computeIfPresent(sessionId, (sid, clients) -> {
            for (WsConnectContext wsClient : clients) {
                String filePath = sessionIdToModelUri.get(sessionId);

                String baseURL = serverConfiguration.getWorkspaceRoot();
                if (!filePath.startsWith(baseURL)) {
                    filePath = baseURL + "/" + filePath;
                }

                final URI uri = URI.createURI(filePath);

                final Optional<EObject> maybeEObject = resourceManager.loadModel(uri, new ResourceSetImpl(), EObject.class);
                wsClient.send(
                    maybeEObject
                        .map(JavalinJackson.INSTANCE::toJson)
                        .orElse(WsResponse.error("Could not load changed object").asText())
                );
            }
            return clients;
        });
    }

    boolean hasSession(String sessionId) {
        return sessionIdToClients.containsKey(sessionId);
    }

    String createSessionId() {
        return UUID.randomUUID().toString();
    }
}
