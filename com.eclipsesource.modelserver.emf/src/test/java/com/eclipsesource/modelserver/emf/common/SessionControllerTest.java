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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.eclipsesource.modelserver.coffee.model.coffee.CoffeeFactory;
import com.eclipsesource.modelserver.command.CCommand;
import com.eclipsesource.modelserver.command.CCommandFactory;
import com.eclipsesource.modelserver.command.CommandKind;
import com.eclipsesource.modelserver.edit.CommandCodec;
import com.eclipsesource.modelserver.emf.ResourceManager;
import com.eclipsesource.modelserver.emf.configuration.ServerConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import io.javalin.websocket.WsContext;

@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {

	@Mock
	private ServerConfiguration serverConfig;
	@Mock
	private ResourceManager resourceManager;
	@Mock
	private CommandCodec commandCodec;
	@Mock
	private WsContext clientCtx;

	@Mock
	private ModelRepository repository;

	private SessionController sessionController;

	@Test
	public void testSubscribe() {
		when(clientCtx.getSessionId()).thenReturn(UUID.randomUUID().toString());
		when(clientCtx.pathParam("modeluri")).thenReturn("fancytesturi");

		assertFalse(sessionController.isClientSubscribed(clientCtx));

		sessionController.subscribe(clientCtx, clientCtx.pathParam("modeluri"));

		assertTrue(sessionController.isClientSubscribed(clientCtx));
	}

	@Test
	public void testUnsubscribe() {
		testSubscribe();
		assertTrue(sessionController.isClientSubscribed(clientCtx));

		sessionController.unsubscribe(clientCtx);

		assertFalse(sessionController.isClientSubscribed(clientCtx));
	}

	@Test
	public void testCommandSubscription()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		when(clientCtx.getSessionId()).thenReturn(UUID.randomUUID().toString());
		when(clientCtx.pathParam("modeluri")).thenReturn("fancytesturi");
		when(repository.getModel("fancytesturi")).thenReturn(Optional.of(CoffeeFactory.eINSTANCE.createMachine()));

		sessionController.subscribe(clientCtx, clientCtx.pathParam("modeluri"));
		CCommand command = CCommandFactory.eINSTANCE.createCommand();
		command.setType(CommandKind.SET);
		sessionController.modelChanged("fancytesturi", command);

		verify(clientCtx).send(argThat(jsonNodeThat(containsRegex("(?i)\"type\":\"set\""))));
	}

	//
	// Test framework
	//

	@Before
	public void createSessionController() {
		sessionController = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ServerConfiguration.class).toInstance(serverConfig);
				bind(ResourceManager.class).toInstance(resourceManager);
				bind(CommandCodec.class).toInstance(commandCodec);
				bind(ModelRepository.class).toInstance(repository);
			}
		}).getInstance(SessionController.class);

		// Mock sessions are always open
		sessionController.setIsOnlyPredicate(ctx -> true);
	}

	Matcher<Object> jsonNodeThat(Matcher<String> data) {
		return new TypeSafeDiagnosingMatcher<Object>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("JsonNode that ");
				description.appendDescriptionOf(data);
			}

			@Override
			protected boolean matchesSafely(Object item, Description mismatchDescription) {
				if (!(item instanceof JsonNode)) {
					return false;
				}
				JsonNode node = (JsonNode) item;
				String text = node.toString();
				if (!data.matches(text)) {
					data.describeMismatch(text, mismatchDescription);
					return false;
				}
				return true;
			}
		};
	}

	Matcher<String> containsRegex(String pattern) {
		return new CustomTypeSafeMatcher<String>("contains regex '" + pattern + "'") {
			@Override
			protected boolean matchesSafely(String item) {
				java.util.regex.Matcher m = Pattern.compile(pattern).matcher(item);
				return m.find();
			}
		};
	}

}
