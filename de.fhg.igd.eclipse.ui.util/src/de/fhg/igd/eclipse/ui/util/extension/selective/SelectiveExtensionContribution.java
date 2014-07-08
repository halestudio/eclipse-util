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

package de.fhg.igd.eclipse.ui.util.extension.selective;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import de.fhg.igd.eclipse.ui.util.Messages;
import de.fhg.igd.eclipse.ui.util.extension.AbstractExtensionContribution;
import de.fhg.igd.eclipse.ui.util.extension.AbstractFactoryAction;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.selective.SelectiveExtension;

/**
 * <p>Title: ExclusiveExtensionContribution</p>
 * <p>Description: A contribution that represents and controls an
 * {@link SelectiveExtension}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class SelectiveExtensionContribution<T, F extends ExtensionObjectFactory<T>> 
	extends AbstractExtensionContribution<T, F, SelectiveExtension<T, F>>  {
	
	/**
	 * Action that sets a factory as the current extension object
	 */
	public class SelectiveAction extends AbstractFactoryAction<F> {
		
		/**
		 * Constructor
		 * 
		 * @param factory the extension object factory
		 */
		public SelectiveAction(F factory) {
			super(factory, IAction.AS_CHECK_BOX);
		}

		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			if (isChecked()) {
				boolean success = getExtension().activate(getFactory());
				if (!success) {
					setChecked(false);
				}
			}
			else {
				getExtension().deactivate(getFactory());
			}
		}

	}
	
	/**
	 * Action offering configuration for extension object factories
	 */
	public class ConfigurationAction extends Action implements IMenuCreator {

		private Menu menu = null;
		
		/**
		 * Default constructor
		 */
		public ConfigurationAction() {
			super(Messages.SelectiveExtensionContribution_0, IAction.AS_DROP_DOWN_MENU);
			
			setImageDescriptor(getConfigurationImage());
			
			setMenuCreator(this);
		}
		
		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			//TODO run default action
		}

		/**
		 * @see IMenuCreator#dispose()
		 */
		@Override
		public void dispose() {
			if (menu != null) {
				menu.dispose();
			}
		}

		/**
		 * @see IMenuCreator#getMenu(Control)
		 */
		@Override
		public Menu getMenu(Control parent) {
			dispose();
			
			menu = new Menu(parent);
			fillMenu(menu);
			
			return menu;
		}

		/**
		 * @see IMenuCreator#getMenu(Menu)
		 */
		@Override
		public Menu getMenu(Menu parent) {
			dispose();
			
			menu = new Menu(parent);
			fillMenu(menu);
			
			return menu;
		}

		/**
		 * Fill the given menu
		 * 
		 * @param menu the menu
		 */
		private void fillMenu(Menu menu) {
			int index = menu.getItemCount();
			
			for (F factory : getExtension().getFactories()) {
				if (factory.allowConfigure()) {
					IAction action = new ConfigureFactoryAction(factory);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(menu, index++);
				}
			}
		}
		
	}

	/**
	 * @see AbstractExtensionContribution#onAdd(ExtensionObjectFactory)
	 */
	@Override
	protected void onAdd(F factory) {
		getExtension().activate(factory);
	}

	/**
	 * @see AbstractExtensionContribution#onRemove(ExtensionObjectFactory)
	 */
	@Override
	protected void onRemove(F factory) {
		getExtension().deactivate(factory);
	}

	/**
	 * @see AbstractExtensionContribution#onConfigure(ExtensionObjectFactory)
	 */
	@Override
	protected void onConfigure(F factory) {
		if (getExtension().isActive(factory)) {
			getExtension().deactivate(factory);
			getExtension().activate(factory);
		}
	}

	/**
	 * @see AbstractExtensionContribution#allowConfiguration()
	 */
	@Override
	protected boolean allowConfiguration() {
		for (F factory : getExtension().getFactories()) {
			if (factory.allowConfigure()) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @see AbstractExtensionContribution#createConfigurationAction()
	 */
	@Override
	protected IAction createConfigurationAction() {
		return new ConfigurationAction();
	}

	/**
	 * @see AbstractExtensionContribution#createFactoryAction(ExtensionObjectFactory)
	 */
	@Override
	protected IAction createFactoryAction(F factory) {
		IAction action = new SelectiveAction(factory);
		action.setChecked(getExtension().isActive(factory));
		return action;
	}

}
