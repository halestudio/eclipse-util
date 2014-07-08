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

package de.fhg.igd.eclipse.ui.util.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

import de.fhg.igd.eclipse.ui.util.Activator;
import de.fhg.igd.eclipse.ui.util.Messages;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactoryCollection;
import de.fhg.igd.eclipse.util.extension.FactoryFilter;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * <p>Title: AbstractExtensionContribution</p>
 * <p>Description: Abstract contribution for extensions, it adds an action
 * for each factory</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @param <E> the extension type
 * @author Simon Templer
 */
public abstract class AbstractExtensionContribution<T, F extends ExtensionObjectFactory<T>, E extends ObjectExtension<T, F>> extends ContributionItem {
	
	/**
	 * Configure a factory
	 */
	public class ConfigureFactoryAction extends AbstractFactoryAction<F> {

		/**
		 * Constructor
		 * 
		 * @param factory the factory
		 */
		public ConfigureFactoryAction(F factory) {
			super(factory, IAction.AS_PUSH_BUTTON);
		}

		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			if (getFactory().configure()) {
				onConfigure(getFactory());
			}
		}

	}

	/**
	 * Removes a factory from the collection
	 */
	public class RemoveFactoryAction extends Action {
		
		private final ExtensionObjectFactoryCollection<T, F> collection;
		
		private final F factory;

		/**
		 * Constructor
		 * 
		 * @param collection the collection
		 * @param factory the factory
		 */
		public RemoveFactoryAction(ExtensionObjectFactoryCollection<T, F> collection, F factory) {
			super(factory.getDisplayName(), IAction.AS_PUSH_BUTTON);
			
			this.factory = factory;
			this.collection = collection;
		}

		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			if (collection.remove(factory)) {
				onRemove(factory);
			}
		}

	}

	/**
	 * Remove action
	 */
	public class RemoveAction extends Action implements IMenuCreator {
		
		private final ExtensionObjectFactoryCollection<T, F> collection;
		
		private Menu menu = null;

		/**
		 * Constructor
		 * 
		 * @param collection the factory collection
		 */
		public RemoveAction(ExtensionObjectFactoryCollection<T, F> collection) {
			super(Messages.AbstractExtensionContribution_0, IAction.AS_DROP_DOWN_MENU);
			
			this.collection = collection;
			
			setMenuCreator(this);
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
		 * @see IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public Menu getMenu(Control parent) {
			dispose();
			
			menu = new Menu(parent);
			fillMenu(menu);
			
			return menu;
		}

		/**
		 * @see IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
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
			
			List<F> factories = new ArrayList<F>(collection.getFactories());
			Collections.sort(factories);
			
			for (F factory : factories) {
				IAction action = new RemoveFactoryAction(collection, factory);
				IContributionItem item = new ActionContributionItem(action);
				item.fill(menu, index++);
			}
		}

	}

	/**
	 * Collection action
	 */
	public class CollectionAction extends Action implements IMenuCreator {
		
		private final ExtensionObjectFactoryCollection<T, F> collection;
		
		private Menu menu = null;
		
		/**
		 * Constructor
		 * 
		 * @param collection the factory collection
		 */
		public CollectionAction(ExtensionObjectFactoryCollection<T, F> collection) {
			super(collection.getName(), IAction.AS_DROP_DOWN_MENU);
			
			this.collection = collection;
			
			setMenuCreator(this);
		}

		/**
		 * @see Action#run()
		 */
		@Override
		public void run() {
			//TODO run default action (add new) if available
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
			
			if (collection.allowAddNew()) {
				IAction addNewAction = new Action(Messages.AbstractExtensionContribution_1, IAction.AS_PUSH_BUTTON) {

					@Override
					public void run() {
						F factory = collection.addNew();
						if (factory != null) {
							onAdd(factory);
						}
					}
				
				};
				IContributionItem item = new ActionContributionItem(addNewAction);
				item.fill(menu, index++);
			}
			
			if (collection.allowRemove() && !collection.getFactories().isEmpty()) {
				IAction removeAction = new RemoveAction(collection);
				IContributionItem item = new ActionContributionItem(removeAction);
				item.fill(menu, index++);
			}
		}

	}
	
	private E extension = null;
	
	private FactoryFilter<T, F> filter = null;
	
	private final Map<String, IAction> createdActions = new HashMap<String, IAction>();
	
	@SuppressWarnings("unused")
	private boolean dirty = true;
	
	/**
	 * Get the extension
	 * 
	 * @return the extension
	 */
	public E getExtension() {
		return extension;
	}
	
	/**
	 * Called after a factory has been removed
	 * 
	 * @param factory the removed factory
	 */
	protected abstract void onRemove(F factory);
	
	/**
	 * Called after a factory has been reconfigured
	 * 
	 * @param factory the factory
	 */
	protected abstract void onConfigure(F factory);
	
	/**
	 * Called after a new factory has been added
	 * 
	 * @param factory the added factory
	 */
	protected abstract void onAdd(F factory);

	/**
	 * Initialize the extension
	 * 
	 * @return the extension
	 */
	protected abstract E initExtension();
	
	/**
	 * Create an action for the given factory
	 * 
	 * @param factory the factory
	 * @return the action
	 */
	protected abstract IAction createFactoryAction(F factory);
	
	/**
	 * Get a factory action after creating and remembering it in {@link #createdActions}
	 * 
	 * @param factory the factory
	 * 
	 * @return the action
	 */
	protected IAction getFactoryAction(F factory) {
		IAction action;
		String id = factory.getIdentifier();
		synchronized (createdActions) {
			if (!createdActions.containsKey(id)) {
				action = createFactoryAction(factory);
				createdActions.put(id, action);
			}
			else {
				action = createdActions.get(id);
			}
		}
		return action;
	}
	
	/**
	 * @return the filter
	 */
	public FactoryFilter<T, F> getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(FactoryFilter<T, F> filter) {
		this.filter = filter;
	}
	
	/**
	 * Get the (filtered) factories to display in the contribution
	 * 
	 * @return the factories
	 */
	protected List<F> getFactories() {
		if (filter == null) {
			return extension.getFactories();
		}
		else {
			return extension.getFactories(filter);
		}
	}

	/**
	 * @see ContributionItem#fill(Menu, int)
	 */
	@Override
	public void fill(Menu menu, int index) {
		if (extension == null) {
			extension = initExtension();
		}
		
		index = fillWithFactories(menu, getFactories(), index);
		
		if (allowConfiguration()) {
			// add a separator
			new Separator().fill(menu, index++);
			
			IAction action = createConfigurationAction();
			IContributionItem item = new ActionContributionItem(action);
			item.fill(menu, index++);
		}
		
		List<ExtensionObjectFactoryCollection<T, F>> collections = extension.getFactoryCollections();
		boolean sep = false;
		if (!collections.isEmpty()) {
			for (ExtensionObjectFactoryCollection<T, F> collection : collections) {
				if ((filter == null || filter.acceptCollection(collection))
						&& (collection.allowAddNew() || collection.allowRemove())) {
					if (!sep) {
						// add a separator
						new Separator().fill(menu, index++);
						sep = true;
					}
					IAction action = createCollectionAction(collection);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(menu, index++);
				}
			}
		}
		
		dirty = false;
	}
	
	/**
	 * Fill the menu with factory actions
	 * 
	 * @param parent the menu
	 * @param factories the available factories
	 * @param index the index to add the items at
	 * @return the index after the inserted items
	 */
	protected int fillWithFactories(Menu parent, List<F> factories, int index) {
		for (F factory : factories) {
			IAction action = getFactoryAction(factory);
			IContributionItem item = new ActionContributionItem(action);
			item.fill(parent, index++);
		}
		
		return index;
	}

	/**
	 * @see ContributionItem#fill(Composite)
	 */
	@Override
	public void fill(Composite parent) {
		if (extension == null) {
			extension = initExtension();
		}
		
		fillWithFactories(parent, getFactories());
		
		List<ExtensionObjectFactoryCollection<T, F>> collections = extension.getFactoryCollections();
		boolean sep = false;
		if (!collections.isEmpty()) {
			for (ExtensionObjectFactoryCollection<T, F> collection : collections) {
				if ((filter == null || filter.acceptCollection(collection))
						&& (collection.allowAddNew() || collection.allowRemove())) {
					if (!sep) {
						// add a separator
						new Separator().fill(parent);
						sep = true;
					}
					IAction action = createCollectionAction(collection);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(parent);
				}
			}
		}
		
		dirty = false;
	}

	/**
	 * Fill the composite with factory actions
	 * 
	 * @param parent the parent composite
	 * @param factories the available factories
	 */
	protected void fillWithFactories(Composite parent, List<F> factories) {
		for (F factory : factories) {
			IAction action = getFactoryAction(factory);
			IContributionItem item = new ActionContributionItem(action);
			item.fill(parent);
		}
	}

	/**
	 * @see ContributionItem#fill(CoolBar, int)
	 */
	@Override
	public void fill(CoolBar parent, int index) {
		if (extension == null) {
			extension = initExtension();
		}
		
		index = fillWithFactories(parent, getFactories(), index);
		
		List<ExtensionObjectFactoryCollection<T, F>> collections = extension.getFactoryCollections();
		boolean sep = false;
		if (!collections.isEmpty()) {
			for (ExtensionObjectFactoryCollection<T, F> collection : collections) {
				if ((filter == null || filter.acceptCollection(collection))
						&& (collection.allowAddNew() || collection.allowRemove())) {
					if (!sep) {
						// add a separator
						new Separator().fill(parent, index++);
						sep = true;
					}
					IAction action = createCollectionAction(collection);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(parent, index++);
				}
			}
		}
		
		dirty = false;
	}
	
	/**
	 * Fill the cool bar with factory actions
	 * 
	 * @param parent the cool bar
	 * @param factories the available factories
	 * @param index the index to add the items at
	 * @return the index after the inserted items
	 */
	protected int fillWithFactories(CoolBar parent, List<F> factories,
			int index) {
		for (F factory : factories) {
			IAction action = getFactoryAction(factory);
			IContributionItem item = new ActionContributionItem(action);
			item.fill(parent, index++);
		}
		
		return index;
	}

	/**
	 * @see ContributionItem#fill(ToolBar, int)
	 */
	@Override
	public void fill(ToolBar parent, int index) {
		if (extension == null) {
			extension = initExtension();
		}
		
		index = fillWithFactories(parent, getFactories(), index);
		
		List<ExtensionObjectFactoryCollection<T, F>> collections = extension.getFactoryCollections();
		boolean sep = false;
		if (!collections.isEmpty()) {
			for (ExtensionObjectFactoryCollection<T, F> collection : collections) {
				if ((filter == null || filter.acceptCollection(collection))
						&& (collection.allowAddNew() || collection.allowRemove())) {
					if (!sep) {
						// add a separator
						new Separator().fill(parent, index++);
						sep = true;
					}
					IAction action = createCollectionAction(collection);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(parent, index++);
				}
			}
		}
		
		dirty = false;
	}
	
	/**
	 * Fill the tool bar with factory actions
	 * 
	 * @param parent the tool bar
	 * @param factories the available factories
	 * @param index the index to add the items at
	 * @return the index after the inserted items
	 */
	protected int fillWithFactories(ToolBar parent, List<F> factories,
			int index) {
		for (F factory : factories) {
			IAction action = getFactoryAction(factory);
			IContributionItem item = new ActionContributionItem(action);
			item.fill(parent, index++);
		}
		
		return index;
	}

	/**
	 * Create an action for the given factory collection
	 * 
	 * @param collection the factory collection
	 * @return the action
	 */
	protected IAction createCollectionAction(
			ExtensionObjectFactoryCollection<T, F> collection) {
		return new CollectionAction(collection);
	}
	
	/**
	 * Create a configuration action for the given extension's factories
	 * 
	 * @return the configuration action
	 */
	protected abstract IAction createConfigurationAction();
	
	/**
	 * Get if a configuration action shall be displayed
	 * 
	 * @return if a configuration action shall be displayed
	 */
	protected abstract boolean allowConfiguration();

	/**
	 * @see ContributionItem#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return true; //dirty; - returning a value that is not true will result in menu items disappearing on perspective change
	}

	/**
	 * @see ContributionItem#isDynamic()
	 */
	@Override
	public boolean isDynamic() {
		return true;
	}

	/**
	 * Mark the item dirty
	 */
	protected void markDirty() {
		this.dirty = true;
	}
	
	/**
	 * Get the configuration image
	 * 
	 * @return the image descriptor of the configuration image
	 */
	public ImageDescriptor getConfigurationImage() {
		return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "images/configure.gif"); //$NON-NLS-1$
	}

	/**
	 * @see ContributionItem#dispose()
	 */
	@Override
	public void dispose() {
		Collection<IAction> actions;
		synchronized (createdActions) {
			actions = new ArrayList<IAction>(createdActions.values());
			createdActions.clear();
		}
		
		for (IAction action : actions) {
			disposeFactoryAction(action);
		}
		
		super.dispose();
	}

	/**
	 * Dispose a factory action when the contribution item is disposed
	 * 
	 * @param action the factory action
	 */
	protected void disposeFactoryAction(IAction action) {
		// override me
	}

}
