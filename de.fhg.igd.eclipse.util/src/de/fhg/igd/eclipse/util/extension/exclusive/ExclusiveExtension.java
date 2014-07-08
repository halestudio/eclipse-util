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

import de.fhg.igd.eclipse.util.extension.ExtensionObjectDefinition;
import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * <p>Title: ExclusiveExtension</p>
 * <p>Description: Interface for extensions that allow only
 * one extension object instance</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public interface ExclusiveExtension<T, F extends ExtensionObjectFactory<T>>
	extends ObjectExtension<T, F> {

	/**
	 * Interface for listeners
	 * 
	 * @param <T> the extension object type
	 * @param <F> the factory type
	 * @author Simon Templer
	 */
	public static interface ExclusiveExtensionListener<T, F extends ExtensionObjectFactory<T>> {
		
		/**
		 * Called when the current extension object has changed
		 * 
		 * @param current the current extension object
		 * @param definition the current extension object's definition
		 */
		public void currentObjectChanged(T current, F definition);

	}

	/**
	 * Get the current extension object instance
	 * 
	 * @return the current extension object instance
	 */
	public T getCurrent();
	
	/**
	 * Get the definition of the current extension object
	 * 
	 * @return the extension object information
	 */
	public F getCurrentDefinition();
	
	/**
	 * Get the definition of the last extension object
	 * 
	 * @return the definition of the last extension object
	 *   or <code>null</code> if the current extension object
	 *   is the first one
	 */
	public F getLastDefinition();
	
	/**
	 * Determine if the given definition represents the
	 *   current extension object instance
	 *   
	 * @param definition the extension object definition
	 * @return if the definition matches the current extension
	 *   object instance
	 */
	public boolean representsCurrent(ExtensionObjectDefinition definition);
	
	/**
	 * Sets the current extension object using the given factory
	 * 
	 * @param factory the extension object factory
	 * @return if setting the extension object was successful
	 */
	public boolean setCurrent(F factory);
	
	/**
	 * Sets the current extension object using the identifier of a factory
	 * 
	 * @param id the factory's identifier
	 * @return if setting the extension object was successful
	 */
	public boolean setCurrent(String id);
	
	/**
	 * Removes the current extension object
	 */
	public void removeCurrent();
	
	/**
	 * Adds a listener
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(ExclusiveExtensionListener<T, F> listener);
	
	/**
	 * Removes a listener
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(ExclusiveExtensionListener<T, F> listener);
	
}
