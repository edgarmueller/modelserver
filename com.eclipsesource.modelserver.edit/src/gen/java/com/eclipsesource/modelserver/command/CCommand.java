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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Command</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getType
 * <em>Type</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getOwner
 * <em>Owner</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getFeature
 * <em>Feature</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getIndices
 * <em>Indices</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getDataValues
 * <em>Data Values</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getObjectValues
 * <em>Object Values</em>}</li>
 * <li>{@link com.eclipsesource.modelserver.command.CCommand#getObjectsToAdd
 * <em>Objects To Add</em>}</li>
 * </ul>
 *
 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand()
 * @model
 * @generated
 */
public interface CCommand extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The literals are
	 * from the enumeration
	 * {@link com.eclipsesource.modelserver.command.CommandKind}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see com.eclipsesource.modelserver.command.CommandKind
	 * @see #setType(CommandKind)
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_Type()
	 * @model required="true"
	 * @generated
	 */
	CommandKind getType();

	/**
	 * Sets the value of the
	 * '{@link com.eclipsesource.modelserver.command.CCommand#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see com.eclipsesource.modelserver.command.CommandKind
	 * @see #getType()
	 * @generated
	 */
	void setType(CommandKind value);

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owner</em>' reference.
	 * @see #setOwner(EObject)
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_Owner()
	 * @model required="true"
	 * @generated
	 */
	EObject getOwner();

	/**
	 * Sets the value of the
	 * '{@link com.eclipsesource.modelserver.command.CCommand#getOwner
	 * <em>Owner</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Owner</em>' reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(EObject value);

	/**
	 * Returns the value of the '<em><b>Feature</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Feature</em>' attribute.
	 * @see #setFeature(String)
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_Feature()
	 * @model required="true"
	 * @generated
	 */
	String getFeature();

	/**
	 * Sets the value of the
	 * '{@link com.eclipsesource.modelserver.command.CCommand#getFeature
	 * <em>Feature</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Feature</em>' attribute.
	 * @see #getFeature()
	 * @generated
	 */
	void setFeature(String value);

	/**
	 * Returns the value of the '<em><b>Indices</b></em>' attribute list. The list
	 * contents are of type {@link java.lang.Integer}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the value of the '<em>Indices</em>' attribute list.
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_Indices()
	 * @model
	 * @generated
	 */
	EList<Integer> getIndices();

	/**
	 * Returns the value of the '<em><b>Data Values</b></em>' attribute list. The
	 * list contents are of type {@link java.lang.String}. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Values</em>' attribute list.
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_DataValues()
	 * @model
	 * @generated
	 */
	EList<String> getDataValues();

	/**
	 * Returns the value of the '<em><b>Object Values</b></em>' reference list. The
	 * list contents are of type {@link org.eclipse.emf.ecore.EObject}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Object Values</em>' reference list.
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_ObjectValues()
	 * @model
	 * @generated
	 */
	EList<EObject> getObjectValues();

	/**
	 * Returns the value of the '<em><b>Objects To Add</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.emf.ecore.EObject}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the value of the '<em>Objects To Add</em>' containment reference
	 *         list.
	 * @see com.eclipsesource.modelserver.command.CCommandPackage#getCommand_ObjectsToAdd()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getObjectsToAdd();

} // CCommand
