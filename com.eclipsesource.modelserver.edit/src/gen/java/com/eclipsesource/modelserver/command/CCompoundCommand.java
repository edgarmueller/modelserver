/**
 * Copyright (c) 2019 EclipseSource and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * 
 */
package com.eclipsesource.modelserver.command;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Compound
 * Command</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link com.eclipsesource.modelserver.command.CCompoundCommand#getCommands
 * <em>Commands</em>}</li>
 * </ul>
 *
 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCompoundCommand()
 * @model
 * @generated
 */
public interface CCompoundCommand extends CCommand {
	/**
	 * Returns the value of the '<em><b>Commands</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link com.eclipsesource.modelserver.command.CCommand}. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Commands</em>' containment reference list.
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCompoundCommand_Commands()
	 * @model containment="true"
	 * @generated
	 */
	EList<CCommand> getCommands();

} // CCompoundCommand
