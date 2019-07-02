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

import static io.javalin.apibuilder.ApiBuilder.*;


import com.eclipsesource.modelserver.common.Routing;
import com.google.inject.Inject;

import io.javalin.Javalin;

public class ModelServerRouting extends Routing {

	private Javalin javalin;

	@Inject
	public ModelServerRouting(Javalin javalin) {
		this.javalin = javalin;
	}

	@Override
	public void bindRoutes() {
		javalin.routes(() -> {
			path("api/v1/", () -> {
				// subscribe to session
				ws("subscribe/:sessionId", wsHandler -> {
					wsHandler.onConnect(ctx -> {
						final String sessionId = ctx.pathParam("sessionId");
						getController(SessionController.class).subscribeSession(ctx, sessionId);
					});
				});
				get("schema/:modeluri", getController(SchemaController.class));
				path("model", () -> {
					// get
					path(":modeluri", () -> {
						get(ctx -> getController(ModelController.class).getOne(ctx, ctx.pathParam("modeluri")));
					});
					// update
					path(":modeluri/:sessionId", () -> {
						patch(ctx ->
							getController(ModelController.class)
								.update(ctx, ctx.pathParam("sessionId"))
						);
					});
					// create session
					ws(":modeluri", wsHandler -> {
						wsHandler.onConnect(ctx -> {
							getController(SessionController.class).createSession(ctx, ctx.pathParam("modeluri"));
						});
					});
				});
			});
		});
	}
}
