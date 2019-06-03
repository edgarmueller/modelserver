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
package com.eclipsesource.modelserver.emf.launch;

import java.net.URL;
import java.util.Collection;

import com.eclipsesource.modelserver.emf.configuration.ServerConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ModelServerLauncher {

	private Collection<? extends Module> modules;
	private Injector injector;
	private String workspaceRoot;

	public ModelServerLauncher() {
	}

	public ModelServerLauncher(URL workspaceRoot) {
		this();
	}

	protected Injector doSetup() {
		Injector injector = Guice.createInjector(modules);
		ServerConfiguration configuration = injector.getInstance(ServerConfiguration.class);
		configuration.setWorkspaceRoot(workspaceRoot);
		return injector;
	}

	public void start() {
		if (injector == null) {
			injector = doSetup();
		}
		run();
	}

	protected void run() {
		// TODO Setup and init middleware communication
	}

	public void shutdown() {

	}

	public void setModules(Collection<? extends Module> modules) {
		this.modules = modules;
	}

	public Collection<? extends Module> getModules() {
		return modules;
	}

	public String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}

	public Injector getInjector() {
		return injector;
	}

}
