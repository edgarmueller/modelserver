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

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.websocket.WsConnectContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import com.eclipsesource.modelserver.emf.EMFJsonConverter;
import com.eclipsesource.modelserver.emf.ResourceManager;
import com.eclipsesource.modelserver.emf.configuration.ServerConfiguration;
import com.google.inject.Inject;


import io.javalin.apibuilder.CrudHandler;

public class ModelController implements CrudHandler {

	@Inject
	private ResourceManager resourceManager;
	@Inject
	private ServerConfiguration serverConfiguration;
	@Inject
	private SessionController sessionController;

	private ResourceSet resourceSet = new ResourceSetImpl();

	public ModelController() {
		JavalinJackson.configure(EMFJsonConverter.setupDefaultMapper());
	}

	@Override
	public void create(Context ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(Context ctx, String modeluri) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getAll(Context ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getOne(Context ctx, String modeluri) {
		loadModel(modeluri).map(ctx::json).orElse(ctx.status(404));
	}

	@Override
	public void update(Context ctx, String sessionId) {
		final EMFJsonConverter emfJsonConverter = new EMFJsonConverter();
		final String body = ctx.body();
		try {
			final JsonNode json = JavalinJackson.getObjectMapper().readTree(body);
			final Optional<EObject> maybeEObject = emfJsonConverter.fromJson(json.get("data").toString());
			maybeEObject.map(eObject -> {
				sessionController.sessionChanged(sessionId);
				return ctx.json(eObject).status(200);
			}).orElse(ctx.status(400));
		} catch (IOException e) {
			ctx.status(400).json(WsResponse.error("Invalid JSON"));
		}


	}

	private Optional<EObject> loadModel(String filePath) {
		String baseURL = serverConfiguration.getWorkspaceRoot();
		if (!filePath.startsWith(baseURL)) {
			filePath = baseURL + "/" + filePath;
		}

		final URI uri = URI.createURI(filePath);
		return resourceManager.loadModel(uri, resourceSet, EObject.class);
	}
}
