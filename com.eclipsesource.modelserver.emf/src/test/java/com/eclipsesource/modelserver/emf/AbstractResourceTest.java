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
package com.eclipsesource.modelserver.emf;

import java.io.IOException;
import java.util.Collections;

import com.eclipsesource.modelserver.common.codecs.EMFJsonConverter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractResourceTest {
	public static final String RESOURCE_PATH = "src/test/resources/";
	protected ResourceSetImpl resourceSet;

	@Before
	public void initializeResourceSet() {
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("json",
				new JsonResourceFactory(EMFJsonConverter.setupDefaultMapper()));
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	}

	protected Resource loadResource(String file) throws IOException {
		Resource resource = resourceSet.createResource(URI.createFileURI(toFullPath(file)));
		resource.load(Collections.EMPTY_MAP);
		return resource;
	}

	protected String toFullPath(String file) {
		return RESOURCE_PATH + file;
	}

	@After
	public void tearDownResourceSet() {
		if (resourceSet != null) {
			resourceSet.getResources().stream().forEach(Resource::unload);
		}
	}

}
