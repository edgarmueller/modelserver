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
package com.eclipsesource.modelserver.command.impl;

import com.eclipsesource.modelserver.command.CCommand;
import com.eclipsesource.modelserver.command.CCommandPackage;
import com.eclipsesource.modelserver.command.CCompoundCommand;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Compound Command</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link com.eclipsesource.modelserver.command.impl.CCompoundCommandImpl#getCommands
 * <em>Commands</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CCompoundCommandImpl extends CCommandImpl implements CCompoundCommand {
	/**
	 * The cached value of the '{@link #getCommands() <em>Commands</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCommands()
	 * @generated
	 * @ordered
	 */
	protected EList<CCommand> commands;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CCompoundCommandImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CCommandPackage.Literals.COMPOUND_COMMAND;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<CCommand> getCommands() {
		if (commands == null) {
			commands = new EObjectContainmentEList<CCommand>(CCommand.class, this,
					CCommandPackage.COMPOUND_COMMAND__COMMANDS);
		}
		return commands;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case CCommandPackage.COMPOUND_COMMAND__COMMANDS:
			return ((InternalEList<?>) getCommands()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case CCommandPackage.COMPOUND_COMMAND__COMMANDS:
			return getCommands();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case CCommandPackage.COMPOUND_COMMAND__COMMANDS:
			getCommands().clear();
			getCommands().addAll((Collection<? extends CCommand>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case CCommandPackage.COMPOUND_COMMAND__COMMANDS:
			getCommands().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case CCommandPackage.COMPOUND_COMMAND__COMMANDS:
			return commands != null && !commands.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // CCompoundCommandImpl
