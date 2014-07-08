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

import java.util.List;

/**
 * <p>Title: ExtensionObjectFactoryCollection</p>
 * <p>Description: A collection of {@link ExtensionObjectFactory}s</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public interface ExtensionObjectFactoryCollection<T, F extends ExtensionObjectFactory<T>> {
	
	/**
	 * Get the collection name
	 * 
	 * @return the collection name
	 */
	public String getName();
	
	/**
	 * Get if a call to {@link #remove(ExtensionObjectFactory)} is
	 * allowed
	 * 
	 * @return if a call to {@link #remove(ExtensionObjectFactory)} is
	 * allowed
	 */
	public boolean allowRemove();
	
	/**
	 * Get if a call to {@link #addNew()} is allowed
	 * 
	 * @return if a call to {@link #addNew()} is allowed
	 */
	public boolean allowAddNew();
	
	/**
	 * Create and add a new factory 
	 * 
	 * @return the created factory or <code>null</code>
	 */
	public F addNew();
	
	/**
	 * Remove the given factory
	 * 
	 * @param factory the factory
	 * @return if the factory was removed
	 */
	public boolean remove(F factory);
	
	/**
	 * Get the factories
	 * 
	 * @return the extension object factories
	 */
	public List<F> getFactories();

}
