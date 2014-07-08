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

package de.fhg.igd.eclipse.util.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: AbstractExtension</p>
 * <p>Description: Utilities for an extension point</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class AbstractExtension<T, F extends ExtensionObjectFactory<T>> implements ObjectExtension<T, F> {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractExtension.class);
	
	private final String extensionPointID;
	
	/**
	 * Caches IDs and associated factories.
	 */
	private final Map<String, F> factoryIdCache = new HashMap<String, F>();
	
	/**
	 * Constructor
	 * 
	 * @param extensionPointID the extension point id
	 */
	public AbstractExtension(final String extensionPointID) {
		this.extensionPointID = extensionPointID;
	}
	
	@Override
	public F getFactory(String id) {
		F factory = factoryIdCache.get(id);
		
		if (factory == null) {
			getFactories(); // ensure that all factories are in the cache
			factory = factoryIdCache.get(id);
		}
		
		return factory;
	}

	/**
	 * @see ObjectExtension#getFactories(FactoryFilter)
	 */
	@Override
	public List<F> getFactories(FactoryFilter<T, F> filter) {
		IConfigurationElement[] confArray = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionPointID);
		
		List<F> result = new ArrayList<F>();
		
		for (IConfigurationElement conf : confArray) {
			// factory
			List<F> factories = getFactories(conf, filter);
			if (factories != null) {
				result.addAll(factories);
			}
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	/**
	 * @see ObjectExtension#getFactories()
	 */
	@Override
	public List<F> getFactories() {
		return getFactories(null);
	}

	/**
	 * Create factories for the given configuration element
	 * 
	 * @param conf the configuration element
	 * @param filter the filter to apply, may be <code>null</code>
	 *  
	 * @return the created factories list or <code>null</code>
	 */
	protected List<F> getFactories(IConfigurationElement conf, FactoryFilter<T, F> filter) {
		try {
			List<F> result = new ArrayList<F>();
			
			// factory
			F factory = createFactory(conf);
			if (factory != null 
					&& (filter == null || filter.acceptFactory(factory))) {
				result.add(factory);
				factoryIdCache.put(factory.getIdentifier(), factory);
			}
			
			// collection
			ExtensionObjectFactoryCollection<T, F> collection = createCollection(conf);
			if (collection != null
					&& (filter == null || filter.acceptCollection(collection))) {
				for (F collectionFactory : collection.getFactories()) {
					if (filter == null || filter.acceptFactory(collectionFactory)) { 
						result.add(collectionFactory);
						factoryIdCache.put(collectionFactory.getIdentifier(), collectionFactory);
					}
				}
			}
			
			return result;
		} catch (Exception e) {
			log.error("Error creating extension object factory", e); //$NON-NLS-1$
		}
		
		return null;
	}

	/**
	 * @see ObjectExtension#getFactoryCollections()
	 */
	@Override
	public List<ExtensionObjectFactoryCollection<T, F>> getFactoryCollections() {
		IConfigurationElement[] confArray = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionPointID);
		
		List<ExtensionObjectFactoryCollection<T, F>> result = new ArrayList<ExtensionObjectFactoryCollection<T, F>>();
		
		for (IConfigurationElement conf : confArray) {
			ExtensionObjectFactoryCollection<T, F> collection;
			try {
				collection = createCollection(conf);
			} catch (Exception e) {
				log.error("Error creating factory collection", e); //$NON-NLS-1$
				collection = null;
			}
			if (collection != null) {
				result.add(collection);
			}
		}
		
		return result;
	}

	/**
	 * Create the factory collection for the given configuration element.
	 * Override this method if your extension supports factory collections.
	 * 
	 * @param conf the configuration element
	 * @return the created collection or <code>null</code>
	 * @throws Exception if creating the collection failed
	 */
	protected ExtensionObjectFactoryCollection<T, F> createCollection(
			IConfigurationElement conf) throws Exception {
		return null;
	}

	/**
	 * Create factory for the given configuration element
	 * 
	 * @param conf the configuration element
	 * @return the created factory or <code>null</code>
	 * @throws Exception if creating the factory failed
	 */
	protected abstract F createFactory(IConfigurationElement conf) throws Exception;

}
