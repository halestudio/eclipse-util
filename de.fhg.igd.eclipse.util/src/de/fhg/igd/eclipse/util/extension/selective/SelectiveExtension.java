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

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;

/**
 * <p>Title: ISelectiveExtension</p>
 * <p>Description: Interface for extension that allow multiple (or none or all)
 * extension objects to be active at once</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public interface SelectiveExtension<T, F extends ExtensionObjectFactory<T>> 
	extends ObjectExtension<T, F> {

	/**
	 * Interface for listener
	 * 
	 * @param <T> the extension object type
	 * @param <F> the factory type
	 * @author Simon Templer
	 */
	public static interface SelectiveExtensionListener<T, F extends ExtensionObjectFactory<T>> {

		/**
		 * Called when an extension object has been activated
		 * 
		 * @param object the activated object
		 * @param definition the object's definition
		 */
		public void activated(T object, F definition);
		
		/**
		 * Called when an extension object has been deactivated
		 * 
		 * @param object the deactivated object
		 * @param definition the object's definition
		 */
		public void deactivated(T object, F definition);
		
	}

	/**
	 * Get the active extension objects
	 * 
	 * @return the active extension objects
	 */
	public Iterable<T> getActiveObjects();
	
	/**
	 * Get the extension object definition of an active extension object
	 * 
	 * @param activeObject the extension object
	 * @return the extension object definition or <code>null</code>
	 */
	public F getDefinition(T activeObject);
	
	/**
	 * Activate an extension object
	 * 
	 * @param factory the extension object's factory
	 * @return if activating the extension object was successful
	 */
	public boolean activate(F factory);
	
	/**
	 * Deactivate an extension object
	 * 
	 * @param definition the extension object's definition
	 */
	public void deactivate(F definition);
	
	/**
	 * Determines if the given definition is active
	 * 
	 * @param definition the extension object definition
	 * @return if the definition is active
	 */
	public boolean isActive(F definition);
	
	/**
	 * Determines if the given object is an active extension object
	 * 
	 * @param object the extension object
	 * @return if the given object is active
	 */
	public boolean isActive(T object);
	
	/**
	 * Adds a listener
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(SelectiveExtensionListener<T, F> listener);
	
	/**
	 * Removes a listener
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(SelectiveExtensionListener<T, F> listener);
	
}
