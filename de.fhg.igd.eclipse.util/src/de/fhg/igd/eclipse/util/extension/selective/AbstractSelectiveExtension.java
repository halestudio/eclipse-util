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

package de.fhg.igd.eclipse.util.extension.selective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactoryCollection;
import de.fhg.igd.eclipse.util.extension.FactoryFilter;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * <p>Title: SelectiveExtension</p>
 * <p>Description: Implementation of {@link SelectiveExtension}, wraps
 * an {@link ObjectExtension}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class AbstractSelectiveExtension<T, F extends ExtensionObjectFactory<T>>
	implements SelectiveExtension<T, F> {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractSelectiveExtension.class);
	
	private final Set<SelectiveExtensionListener<T, F>> listeners = new HashSet<SelectiveExtensionListener<T, F>>();
	
	private final ObjectExtension<T, F> extension;
	
	private final Map<F, T> activeDefinitions = new HashMap<F, T>();
	private final Map<T, F> activeObjects = new HashMap<T, F>();
	
	private boolean initialized = false;
	
	/**
	 * Constructor
	 * 
	 * @param extension the extension
	 */
	public AbstractSelectiveExtension(final ObjectExtension<T, F> extension) {
		this.extension = extension;
	}
	
	/**
	 * Initialize the active objects
	 */
	protected void init() {
		if (initialized) {
			return;
		}
		
		for (F factory : getFactories()) {
			if (activateOnInit(factory)) {
				activate(factory);
			}
		}
		
		initialized = true;
	}

	/**
	 * Determines if the given factory shall be activated when
	 * the {@link AbstractSelectiveExtension} is initialized
	 * 
	 * @param factory the factory
	 * @return if the factory shall be activated
	 */
	protected abstract boolean activateOnInit(F factory);

	/**
	 * @see SelectiveExtension#activate(ExtensionObjectFactory)
	 */
	@Override
	public boolean activate(F factory) {
		if (activeDefinitions.containsKey(factory)) {
			// already active
			return true;
		}
		else {
			try {
				// create and add object
				T object = factory.createExtensionObject();
				activeDefinitions.put(factory, object);
				activeObjects.put(object, factory);
				// mark as initialized
				initialized = true;
				
				// notify listeners
				for (SelectiveExtensionListener<T, F> listener : listeners) {
					try {
						listener.activated(object, factory);
					} catch (Exception e) {
						log.error("Error while notifying listener", e); //$NON-NLS-1$
					}
				}
				
				return true;
			} catch (Exception e) {
				log.error("Error activating extension object", e); //$NON-NLS-1$
				return false;
			}
		}
	}

	/**
	 * @see SelectiveExtension#addListener(SelectiveExtensionListener)
	 */
	@Override
	public void addListener(SelectiveExtensionListener<T, F> listener) {
		listeners.add(listener);
	}

	/**
	 * @see SelectiveExtension#deactivate(ExtensionObjectFactory)
	 */
	@Override
	public void deactivate(F definition) {
		T object = activeDefinitions.remove(definition);
		
		if (object != null) {
			F factory = activeObjects.remove(object);
			
			// notify listeners
			for (SelectiveExtensionListener<T, F> listener : listeners) {
				try {
					listener.deactivated(object, definition);
				} catch (Exception e) {
					log.error("Error while notifying listener", e); //$NON-NLS-1$
				}
			}
			
			factory.dispose(object);
		}
	}

	/**
	 * @see SelectiveExtension#getActiveObjects()
	 */
	@Override
	public Iterable<T> getActiveObjects() {
		init();
		
		return new ArrayList<T>(activeObjects.keySet());
	}

	/**
	 * @see SelectiveExtension#isActive(ExtensionObjectFactory)
	 */
	@Override
	public boolean isActive(F definition) {
		init();
		
		return activeDefinitions.containsKey(definition);
	}

	/**
	 * @see SelectiveExtension#isActive(Object)
	 */
	@Override
	public boolean isActive(T object) {
		init();
		
		return activeObjects.containsKey(object);
	}

	/**
	 * @see SelectiveExtension#getDefinition(Object)
	 */
	@Override
	public F getDefinition(T activeObject) {
		return activeObjects.get(activeObject);
	}

	/**
	 * @see SelectiveExtension#removeListener(SelectiveExtensionListener)
	 */
	@Override
	public void removeListener(SelectiveExtensionListener<T, F> listener) {
		listeners.remove(listener);
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

}
