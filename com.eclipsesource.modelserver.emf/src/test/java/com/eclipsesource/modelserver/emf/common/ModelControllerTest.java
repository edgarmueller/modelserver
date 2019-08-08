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

import com.eclipsesource.modelserver.coffee.model.coffee.AutomaticTask;
import com.eclipsesource.modelserver.coffee.model.coffee.BrewingUnit;
import com.eclipsesource.modelserver.coffee.model.coffee.CoffeeFactory;
import com.eclipsesource.modelserver.coffee.model.coffee.Workflow;
import com.eclipsesource.modelserver.command.CCommand;
import com.eclipsesource.modelserver.command.CCommandFactory;
import com.eclipsesource.modelserver.command.CommandKind;
import com.eclipsesource.modelserver.common.codecs.DecodingException;
import com.eclipsesource.modelserver.common.codecs.EncodingException;
import com.eclipsesource.modelserver.common.codecs.XmiCodec;
import com.eclipsesource.modelserver.emf.common.codecs.JsonCodec;
import com.eclipsesource.modelserver.jsonschema.Json;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.resource.JsonResource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.eclipsesource.modelserver.edit.tests.util.EMFMatchers.eEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class ModelControllerTest {

    private ModelRepository modelRepository;
    private Context context;
    private ModelController modelController;
    private SessionController sessionController;

    @Before
    public void before() {
        modelRepository = mock(ModelRepository.class);
        context = mock(Context.class);
        sessionController = mock(SessionController.class);
        modelController = new ModelController(modelRepository, sessionController);
    }

    @Test
    public void getOneXmiFormat() throws EncodingException {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        final BrewingUnit brewingUnit = CoffeeFactory.eINSTANCE.createBrewingUnit();
        Answer<Void> answer = invocation -> {
            response.set(invocation.getArgument(0));
            return null;
        };
        doAnswer(answer).when(context).json(any(JsonNode.class));
        final LinkedHashMap<String, List<String>> queryParams = new LinkedHashMap<>();
        queryParams.put("format", Collections.singletonList("xmi"));
        when(context.queryParamMap()).thenReturn(queryParams);
        when(modelRepository.getModel("test")).thenReturn(Optional.of(brewingUnit));

        modelController.getOne(context, "test");

        assertThat(response.get().get("data"), is(equalTo(new XmiCodec().encode(brewingUnit))));
    }

    @Test
    public void getAllXmiFormat() throws EncodingException, IOException {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        final BrewingUnit brewingUnit = CoffeeFactory.eINSTANCE.createBrewingUnit();
        Answer<Void> answer = invocation -> {
            response.set(invocation.getArgument(0));
            return null;
        };
        doAnswer(answer).when(context).json(any(JsonNode.class));
        final LinkedHashMap<String, List<String>> queryParams = new LinkedHashMap<>();
        queryParams.put("format", Collections.singletonList("xmi"));
        when(context.queryParamMap()).thenReturn(queryParams);
        final Map<URI, EObject> allModels =
            Collections.singletonMap(URI.createURI("test"), brewingUnit);
        when(modelRepository.getAllModels()).thenReturn(allModels);

        modelController.getAll(context);

        assertThat(response.get().get("data"), is(equalTo(
            Json.object(
                Json.prop("test", new XmiCodec().encode(brewingUnit))
            )
        )));
    }

    @Test
    public void getOneJsonFormat() throws EncodingException {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        final BrewingUnit brewingUnit = CoffeeFactory.eINSTANCE.createBrewingUnit();
        Answer<Void> answer = invocation -> {
            response.set(invocation.getArgument(0));
            return null;
        };
        doAnswer(answer).when(context).json(any(JsonNode.class));
        when(modelRepository.getModel("test")).thenReturn(Optional.of(brewingUnit));

        modelController.getOne(context, "test");

        assertThat(response.get().get("data"), is(equalTo(new JsonCodec().encode(brewingUnit))));
    }

    @Ignore
    @Test
    public void updateXmi() throws EncodingException {
        final BrewingUnit brewingUnit = CoffeeFactory.eINSTANCE.createBrewingUnit();
        final LinkedHashMap<String, List<String>> queryParams = new LinkedHashMap<>();
        queryParams.put("format", Collections.singletonList("xmi"));
        when(context.queryParamMap()).thenReturn(queryParams);
        when(context.body()).thenReturn(
            Json.object(Json.prop("data", new XmiCodec().encode(brewingUnit))).toString()
        );
        when(modelRepository.getResourceSet()).thenReturn(new ResourceSetImpl());
        modelController.update(context, "SuperBrewer3000.json");
        verify(modelRepository, times(1))
            .updateModel(eq("SuperBrewer3000.json"), any(BrewingUnit.class));
    }

    @Test
    public void executeCommand() throws EncodingException, DecodingException {
        ResourceSet rset = new ResourceSetImpl();
        when(modelRepository.getResourceSet()).thenReturn(rset);
        JsonResource res = new JsonResource(URI.createURI("SuperBrewer3000.json"));
        rset.getResources().add(res);
        final AutomaticTask task = CoffeeFactory.eINSTANCE.createAutomaticTask();
        res.getContents().add(task);
        CCommand setCommand = CCommandFactory.eINSTANCE.createCommand();
        setCommand.setType(CommandKind.SET);
        setCommand.setOwner(task);
        setCommand.setFeature("name");
        setCommand.getDataValues().add("Foo");
        JsonResource cmdRes = new JsonResource(URI.createURI("$command.json"));
        cmdRes.getContents().add(setCommand);

        final LinkedHashMap<String, List<String>> queryParams = new LinkedHashMap<>();
        queryParams.put("modeluri", Collections.singletonList("SuperBrewer3000.json"));
        when(context.queryParamMap()).thenReturn(queryParams);
        when(context.body()).thenReturn(Json.object(Json.prop("data", new JsonCodec().encode(setCommand))).toString());
        when(modelRepository.getResourceSet()).thenReturn(rset);
        when(modelRepository.getModel("SuperBrewer3000.json")).thenReturn(Optional.of(task));
        modelController.executeCommand(context, "SuperBrewer3000.json");

        // unload to proxify
        res.unload();
        verify(modelRepository).updateModel(eq("SuperBrewer3000.json"), argThat(eEqualTo(setCommand)));
        verify(sessionController).modelChanged(eq("SuperBrewer3000.json"), argThat(eEqualTo(setCommand)));
    }

    @Test
    public void addCommandNotification() throws EncodingException, DecodingException {
        ResourceSet rset = new ResourceSetImpl();
        when(modelRepository.getResourceSet()).thenReturn(rset);
        JsonResource res = new JsonResource(URI.createURI("SuperBrewer3000.json"));
        rset.getResources().add(res);
        final Workflow workflow = CoffeeFactory.eINSTANCE.createWorkflow();
        res.getContents().add(workflow);
        
        final AutomaticTask task = CoffeeFactory.eINSTANCE.createAutomaticTask();
        CCommand addCommand = CCommandFactory.eINSTANCE.createCommand();
        addCommand.setType(CommandKind.ADD);
        addCommand.setOwner(workflow);
        addCommand.setFeature("nodes");
        addCommand.getObjectsToAdd().add(task);
        addCommand.getObjectValues().add(task);
        JsonResource cmdRes = new JsonResource(URI.createURI("$command.json"));
        cmdRes.getContents().add(addCommand);
        String commandAsString = Json.object(Json.prop("data", Json.text(new JsonCodec().encode(addCommand).toString()))).toString();

        final LinkedHashMap<String, List<String>> queryParams = new LinkedHashMap<>();
        queryParams.put("modeluri", Collections.singletonList("SuperBrewer3000.json"));
        when(context.queryParamMap()).thenReturn(queryParams);
        when(context.body()).thenReturn(commandAsString);
        when(modelRepository.getResourceSet()).thenReturn(rset);
        when(modelRepository.getModel("SuperBrewer3000.json")).thenReturn(Optional.of(task));
        modelController.executeCommand(context, "SuperBrewer3000.json");

        // unload to proxify
        res.unload();
        verify(modelRepository).updateModel(eq("SuperBrewer3000.json"), argThat(eEqualTo(addCommand)));
        verify(sessionController).modelChanged(eq("SuperBrewer3000.json"), argThat(eEqualTo(addCommand)));
    }

}
