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

package com.eclipsesource.modelserver.internal.client;

import org.eclipse.emf.common.command.Command;

import com.eclipsesource.modelserver.client.EditingContext;
import com.eclipsesource.modelserver.client.ModelServerClient;
import com.eclipsesource.modelserver.command.CCommand;
import com.eclipsesource.modelserver.common.codecs.EncodingException;
import com.eclipsesource.modelserver.edit.CommandCodec;
import com.eclipsesource.modelserver.edit.DefaultCommandCodec;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Implementation of the client's editing context.
 */
public class EditingContextImpl extends WebSocketListener implements EditingContext {

	private final ModelServerClient owner;
	private final CommandCodec codec = new DefaultCommandCodec();
	private WebSocket socket;
	private int referenceCount = 1;

	/**
	 * Initializes me.
	 */
	public EditingContextImpl(ModelServerClient owner) {
		super();

		this.owner = owner;
	}

	@Override
	public boolean execute(Command command) throws EncodingException {
		CCommand serializable = codec.encode(command);
		String message = owner.encode(serializable);
		return execute(message);
	}

	@Override
	public boolean execute(String command) {
		if (socket == null) {
			return false;
		}
		
		// Wrap the command in a message
		String message = String.format("{data:%s}", command);
		return socket.send(message);
	}

	//
	// WebSocket events
	//

	@Override
	public void onOpen(WebSocket webSocket, Response response) {
		this.socket = webSocket;
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {
		if (webSocket == this.socket) {
			this.socket = null;
		}
	}

	//
	// Reference counting
	//

	public void retain() {
		referenceCount = referenceCount + 1;
	}

	public boolean release() {
		referenceCount = referenceCount - 1;
		return referenceCount <= 0;
	}

}
