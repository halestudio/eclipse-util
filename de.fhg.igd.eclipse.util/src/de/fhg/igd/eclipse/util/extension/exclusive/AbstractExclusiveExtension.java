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

package de.fhg.igd.eclipse.util.extension.exclusive;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectDefinition;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactoryCollection;
import de.fhg.igd.eclipse.util.extension.FactoryFilter;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * <p>Title: AbstractExclusiveExtension</p>
 * <p>Description: Implementation for {@link ExclusiveExtension}, wraps
 * an {@link ObjectExtension}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class AbstractExclusiveExtension<T, F extends ExtensionObjectFactory<T>> 
	implements ExclusiveExtension<T, F> {
	
	/**
	 * The log
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractExclusiveExtension.class);
	
	/**
	 * The internal extension
	 */
	private final ObjectExtension<T, F> extension;
	
	/**
	 * The type-safe list of listeners
	 */
	private final CopyOnWriteArraySet<ExclusiveExtensionListener<T, F>> listeners =
		new CopyOnWriteArraySet<ExclusiveExtensionListener<T, F>>();
	
	/**
	 * The current extension object
	 */
	private T current;
	
	/**
	 * The definition of the current extension object
	 */
	private F definition;
	
	/**
	 * The definition of the last extension object
	 */
	private F lastDefinition;
	
	/**
	 * If the extension has already been initialized
	 */
	private boolean initialized = false;
	
	/**
	 * If activating the current definition is allowed
	 */
	private boolean allowReactivation = false; 

	/**
	 * Constructor
	 * 
	 * @param extension the internal extension
	 */
	public AbstractExclusiveExtension(final ObjectExtension<T, F> extension) {
		super();
		
		this.extension = extension;
	}
	
	/**
	 * @return if activating the current definition is allowed
	 */
	public boolean isAllowReactivation() {
		return allowReactivation;
	}

	/**
	 * @param allowReactivation if activating the current definition is allowed
	 */
	public void setAllowReactivation(boolean allowReactivation) {
		this.allowReactivation = allowReactivation;
	}

	/**
	 * Initialize the current extension object
	 */
	protected void init() {
		if (initialized) {
			return;
		}
		
		if (current == null || definition == null) {
			// determine initial instance
			F factory = getInitialFactory();
			setCurrent(factory);
		}
		else {
			// inform any listeners already added
			for (ExclusiveExtensionListener<T, F> listener : listeners) {
				try {
					listener.currentObjectChanged(current, definition);
				} catch (Exception e) {
					log.error("Error notifiying listener of extension object change", e); //$NON-NLS-1$
				}
			}
		}
		
		initialized = true;
	}

	/**
	 * Get the initial extension object factory
	 * 
	 * @return the initial extension object factory
	 */
	protected abstract F getInitialFactory();

	/**
	 * @see ExclusiveExtension#removeCurrent()
	 */
	@Override
	public void removeCurrent() {
		F factory = getInitialFactory();
		setCurrent(factory);
	}

	/**
	 * @see ObjectExtension#getFactories()
	 */
	@Override
	public List<F> getFactories() {
		return extension.getFactories();
	}

	@Override
	public F getFactory(String id) {
		return extension.getFactory(id);
	}

	/**
	 * @see ObjectExtension#getFactories(FactoryFilter)
	 */
	@Override
	public List<F> getFactories(FactoryFilter<T, F> filter) {
		return extension.getFactories(filter);
	}

	/**
	 * @see ObjectExtension#getFactoryCollections()
	 */
	@Override
	public List<ExtensionObjectFactoryCollection<T, F>> getFactoryCollections() {
		return extension.getFactoryCollections();
	}
	
	/**
	 * @see ExclusiveExtension#addListener(ExclusiveExtensionListener)
	 */
	@Override
	public void addListener(ExclusiveExtensionListener<T, F> listener) {
		listeners.add(listener);
	}

	/**
	 * @see ExclusiveExtension#getCurrent()
	 */
	@Override
	public T getCurrent() {
		init();
		
		return current;
	}

	/**
	 * @see ExclusiveExtension#getCurrentDefinition()
	 */
	@Override
	public F getCurrentDefinition() {
		init();
			
		return definition;
	}

	/**
	 * @see ExclusiveExtension#getLastDefinition()
	 */
	@Override
	public F getLastDefinition() {
		return lastDefinition;
	}

	/**
	 * @see ExclusiveExtension#removeListener(ExclusiveExtensionListener)
	 */
	@Override
	public void removeListener(ExclusiveExtensionListener<T, F> listener) {
		listeners.remove(listener);
	}

	/**
	 * @see ExclusiveExtension#representsCurrent(ExtensionObjectDefinition)
	 */
	@Override
	public boolean representsCurrent(ExtensionObjectDefinition definition) {
		init();
		
		if (this.definition == null) {
			return definition == null;
		}
		else {
			return this.definition.equals(definition);
		}
	}

	/**
	 * @see ExclusiveExtension#setCurrent(ExtensionObjectFactory)
	 */
	@Override
	public boolean setCurrent(F factory) {
		if (!allowReactivation && factory.equals(definition)) {
			// no activation of the current definition allowed
			// keeping the current extension object
			return true;
		}
		
		T oldInstance = current;
		F oldFactory = definition;
		
		try {
			current = factory.createExtensionObject();
			definition = factory;
			// mark as initialized
			initialized = true;
		} catch (Exception e) {
			log.error("Error creating extension object instance", e); //$NON-NLS-1$
			return false;
		}
		
		for (ExclusiveExtensionListener<T, F> listener : listeners) {
			try {
				listener.currentObjectChanged(current, definition);
			} catch (Exception e) {
				log.error("Error notifiying listener of extension object change", e); //$NON-NLS-1$
			}
		}
		
		// remember old factory as last definition
		if (oldFactory != null) {
			lastDefinition = oldFactory;
		}
		
		// dispose old instance
		if (oldInstance != null && oldFactory != null) {
			oldFactory.dispose(oldInstance);
		}
		
		return true;
	}
	
	/**
	 * @see ExclusiveExtension#setCurrent(String)
	 */
	@Override
	public boolean setCurrent(String id) {
		if (id == null) {
			return false;
		}
		for (F f : getFactories()) {
			if (f.getIdentifier() != null && f.getIdentifier().equals(id)) {
				return setCurrent(f);
			}
		}
		return false;
	}

}
