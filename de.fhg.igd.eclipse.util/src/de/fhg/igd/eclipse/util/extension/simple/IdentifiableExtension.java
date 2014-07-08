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

package de.fhg.igd.eclipse.util.extension.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * Extension for configuration elements representing a certain type that has
 * a String identifier.<br>
 * In contrast to an {@link ObjectExtension} the extension doesn't contain
 * factories but data objects corresponding to the configuration elements.
 * @param <T> the extension element type
 *  
 * @author Simon Templer
 */
public abstract class IdentifiableExtension<T extends IdentifiableExtension.Identifiable> {
	

	/**
	 * Interface for objects with a {@link String} identifier
	 */
	public interface Identifiable {
		
		/**
		 * Get the ID
		 * @return the ID string
		 */
		public String getId();

	}

	private final String extensionId;
	
	private final boolean cacheElements;
	
	private final boolean sortIfPossible;
	
	private final Map<String, T> identifierCache = new HashMap<String, T>();
	
	private Collection<T> cachedElements;
	
	/**
	 * Create the extension and bind it to the given extension ID.
	 * @param extensionId the extension ID
	 */
	public IdentifiableExtension(String extensionId) {
		this(extensionId, true, true);
	}
	
	/**
	 * Create the extension and bind it to the given extension ID.
	 * @param extensionId the extension ID
	 * @param cacheElements if the result of {@link #getElements()} should be
	 *   cached
	 * @param sortIfPossible if sorting for {@link #getElements()} should be
	 *   enabled, needs the elements implementing {@link Comparable}
	 */
	public IdentifiableExtension(String extensionId, boolean cacheElements,
			boolean sortIfPossible) {
		super();
		this.extensionId = extensionId;
		this.cacheElements = cacheElements;
		this.sortIfPossible = sortIfPossible;
	}
	
	/**
	 * Get the name of the ID attribute
	 * @return the name of the ID attribute
	 */
	protected abstract String getIdAttributeName();

	/**
	 * Get the element with the given ID
	 * @param id the element ID
	 * @return the element value or <code>null</code>
	 */
	public T get(String id) {
		// first try the cache
		T result = identifierCache.get(id);
		
		if (result == null) {
			// then search the configuration
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionId);
			
			for (IConfigurationElement element : elements) {
				String elementId = element.getAttribute(getIdAttributeName());
				if (id.equals(elementId)) {
					result = create(elementId, element);
					if (result != null) {
						identifierCache.put(result.getId(), result);
						return result;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Create an object for the given configuration element
	 * @param elementId the element ID
	 * @param element the configuration element
	 * @return the element object or <code>null</code>
	 */
	protected abstract T create(String elementId, IConfigurationElement element);

	/**
	 * Get all elements
	 * @return the elements
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<T> getElements() {
		if (cachedElements != null) {
			return cachedElements;
		}
		
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionId);
		
		List<T> result = new ArrayList<T>();
		
		for (IConfigurationElement element : elements) {
			String elementId = element.getAttribute(getIdAttributeName());
			T val = create(elementId, element);
			if (val != null) {
				identifierCache.put(val.getId(), val);
				result.add(val);
			}
		}
		
		if (sortIfPossible && !result.isEmpty()) {
			// test first element if it implements comparable
			if (result.get(0) instanceof Comparable<?>) {
				try {
					List<Comparable> compList = new ArrayList<Comparable>();
					for (T element : result) {
						compList.add((Comparable) element);
					}
					
					Collections.sort(compList);
					
					List<T> sortedResult = new ArrayList<T>();
					for (Comparable element : compList) {
						sortedResult.add((T) element);
					}
					result = sortedResult;
				} catch (Exception e) {
					// sorting failed, ignore
				}
			}
		}
		
		if (cacheElements) {
			cachedElements = result;
		}
		
		return result;
	}

}
