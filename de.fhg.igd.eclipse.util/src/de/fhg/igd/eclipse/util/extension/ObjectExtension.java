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
 * <p>Title: ObjectExtension</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public interface ObjectExtension<T, F extends ExtensionObjectFactory<T>> {

	/**
	 * Create factories for all configured extensions
	 * 
	 * @return the factories
	 */
	public abstract List<F> getFactories();
	
	/**
	 * Get the factory with the given ID
	 * @param id the factory ID
	 * @return the factory or <code>null</code> if none matching the ID is
	 *   available
	 */
	public F getFactory(String id);
	
	/**
	 * Create factories for the configured extensions and verify them against
	 * the given filter
	 * 
	 * @param filter the filter to apply
	 * 
	 * @return the factories that are accepted by the filter
	 */
	public abstract List<F> getFactories(FactoryFilter<T, F> filter);

	/**
	 * Get the factory collections
	 * 
	 * @return the factory collections
	 */
	public abstract List<ExtensionObjectFactoryCollection<T, F>> getFactoryCollections();

}