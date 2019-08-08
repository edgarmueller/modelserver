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

import com.eclipsesource.modelserver.coffee.model.coffee.CoffeeFactory;
import com.eclipsesource.modelserver.coffee.model.coffee.CoffeePackage;
import com.eclipsesource.modelserver.coffee.model.coffee.util.CoffeeAdapterFactory;
import com.eclipsesource.modelserver.command.CCommandFactory;
import com.eclipsesource.modelserver.command.CCommandPackage;
import com.eclipsesource.modelserver.common.codecs.DecodingException;
import com.eclipsesource.modelserver.edit.CommandCodec;
import com.eclipsesource.modelserver.emf.ResourceManager;
import com.eclipsesource.modelserver.emf.configuration.EPackageConfiguration;
import com.eclipsesource.modelserver.emf.configuration.ServerConfiguration;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ModelRepository} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelRepositoryTest {

	@Mock
	private ServerConfiguration serverConfig;
	@Mock
	private CommandCodec commandCodec;
	@Mock
	private Command command;

	private ModelRepository repository;

	/**
	 * Initializes me
	 */
	public ModelRepositoryTest() {
		super();
	}

	@Test
	public void addModel() throws IOException {
		repository.addModel("SuperBrewer3000.json", CoffeeFactory.eINSTANCE.createBrewingUnit());
		assertTrue(repository.hasModel("SuperBrewer3000.json"));
	}

	@Test
	public void updateModel() throws DecodingException {
		repository.updateModel("SuperBrewer3000.json", CCommandFactory.eINSTANCE.createCommand());
		verify(command).execute();
	}

	//
	// Test framework
	//

	@Before
	public void createRepository() throws DecodingException {
		Set<EPackageConfiguration> configurations = new LinkedHashSet<>();
		configurations.add(
			new EPackageConfiguration() {
				@Override
				public String getId() {
					return CoffeePackage.eINSTANCE.getNsURI();
				}
				@Override
				public Collection<String> getFileExtensions() {
					return Lists.newArrayList("coffee", "json");
				}
				@Override
				public void registerEPackage() {
					CoffeePackage.eINSTANCE.eClass();
				}
			}
		);
		configurations.add(
			new EPackageConfiguration() {
				@Override
				public String getId() {
					return CCommandPackage.eINSTANCE.getNsURI();
				}

				@Override
				public Collection<String> getFileExtensions() {
					return Collections.singletonList("command");
				}

				@Override
				public void registerEPackage() {
					CCommandPackage.eINSTANCE.eClass();
				}
			}
		);
		ResourceManager resourceManager = new ResourceManager(configurations);
		when(command.canExecute()).thenReturn(true);
		when(commandCodec.decode(any(), any())).thenReturn(command);
		when(serverConfig.getWorkspaceRoot()).thenReturn(".");
		repository = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ServerConfiguration.class).toInstance(serverConfig);
				bind(ResourceManager.class).toInstance(resourceManager);
				bind(CommandCodec.class).toInstance(commandCodec);
				bind(AdapterFactory.class).toInstance(new CoffeeAdapterFactory());
			}
		}).getInstance(ModelRepository.class);
	}

}
