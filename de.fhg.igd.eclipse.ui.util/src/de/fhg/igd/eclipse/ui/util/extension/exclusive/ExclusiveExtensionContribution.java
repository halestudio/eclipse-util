// Fraunhofer Institute for Computer Graphics Research (IGD)
// Department Spatial Information Management (GEO)
//
// Copyright (c) 2014 Fraunhofer IGD
//
// This file is part of eclipse-util.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package de.fhg.igd.eclipse.ui.util.extension.exclusive;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import de.fhg.igd.eclipse.ui.util.extension.AbstractExtensionContribution;
import de.fhg.igd.eclipse.ui.util.extension.AbstractFactoryAction;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.exclusive.ExclusiveExtension;
import de.fhg.igd.eclipse.util.extension.exclusive.ExclusiveExtension.ExclusiveExtensionListener;

/**
 * <p>Title: ExclusiveExtensionContribution</p>
 * <p>Description: A contribution that represents and controls an
 * {@link ExclusiveExtension}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class ExclusiveExtensionContribution<T, F extends ExtensionObjectFactory<T>> 
	extends AbstractExtensionContribution<T, F, ExclusiveExtension<T, F>> {
	
	/**
	 * Action that sets a factory as the current extension object
	 */
	public class ExclusiveAction extends AbstractFactoryAction<F> implements ExclusiveExtensionListener<T, F> {
		
		/**
		 * Constructor
		 * 
		 * @param factory the extension object factory
		 */
		public ExclusiveAction(F factory) {
			super(factory, (useRadios)?(IAction.AS_RADIO_BUTTON):(IAction.AS_PUSH_BUTTON));
		}

		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			if (!useRadios || isChecked()) {
				boolean success = getExtension().setCurrent(getFactory());
				if (!success) {
					markDirty();
				}
			}
		}

		/**
		 * @see ExclusiveExtensionListener#currentObjectChanged(Object, ExtensionObjectFactory)
		 */
		@Override
		public void currentObjectChanged(T current, F definition) {
			if (getFactory().equals(definition)) {
				setChecked(true);
				markDirty();
			}
			else {
				setChecked(false);
				markDirty();
			}
		}

	}
	
	private final boolean useRadios;
	
	/**
	 * Default constructor
	 */
	protected ExclusiveExtensionContribution() {
		this(true);
	}

	/**
	 * Constructor
	 * 
	 * @param useRadios if radio items shall be used
	 */
	protected ExclusiveExtensionContribution(boolean useRadios) {
		super();
		this.useRadios = useRadios;
	}

	/**
	 * @see AbstractExtensionContribution#onAdd(ExtensionObjectFactory)
	 */
	@Override
	protected void onAdd(F factory) {
		getExtension().setCurrent(factory);
	}

	/**
	 * @see AbstractExtensionContribution#onRemove(ExtensionObjectFactory)
	 */
	@Override
	protected void onRemove(F factory) {
		if (getExtension().representsCurrent(factory)) {
			getExtension().removeCurrent();
		}
	}

	/**
	 * @see AbstractExtensionContribution#onConfigure(ExtensionObjectFactory)
	 */
	@Override
	protected void onConfigure(F factory) {
		if (getExtension().representsCurrent(factory)) {
			getExtension().setCurrent(factory);
		}
	}

	/**
	 * @see AbstractExtensionContribution#allowConfiguration()
	 */
	@Override
	protected boolean allowConfiguration() {
		return getExtension().getCurrentDefinition().allowConfigure();
	}

	/**
	 * @see AbstractExtensionContribution#createConfigurationAction()
	 */
	@Override
	protected IAction createConfigurationAction() {
		IAction action = new ConfigureFactoryAction(getExtension().getCurrentDefinition());
		
		action.setImageDescriptor(getConfigurationImage());
		
		return action;
	}

	/**
	 * @see AbstractExtensionContribution#createFactoryAction(ExtensionObjectFactory)
	 */
	@Override
	protected IAction createFactoryAction(F factory) {
		ExclusiveAction action = new ExclusiveAction(factory);
		if (useRadios) {
			action.setChecked(getExtension().representsCurrent(factory));
			getExtension().addListener(action);
		}
		return action;
	}

	/**
	 * @see AbstractExtensionContribution#disposeFactoryAction(IAction)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void disposeFactoryAction(IAction action) {
		if (action instanceof ExclusiveExtensionListener<?, ?>) {
			getExtension().removeListener((ExclusiveExtensionListener<T, F>) action);
		}
	}

}
