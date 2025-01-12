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

import org.jetbrains.annotations.NotNull;

import com.eclipsesource.modelserver.jsonschema.JsonSchema;
import com.google.inject.Inject;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SchemaController implements Handler {

	@Inject
	private ModelRepository modelRepository;

	@Override
	public void handle(@NotNull Context ctx) {
		modelRepository.getModel(ctx.queryParam("modeluri")).ifPresentOrElse(
				instance -> ctx.json(JsonResponse.success(JsonSchema.from(instance.eClass()))),
				() -> ctx.status(404).json(JsonResponse.error(String.format("Schema for '%s' not found!", ctx.queryParam("modeluri"))))
		);
	}
}
