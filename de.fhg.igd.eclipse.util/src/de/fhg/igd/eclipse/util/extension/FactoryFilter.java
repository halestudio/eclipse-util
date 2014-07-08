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

/**
 * <p>Title: FactoryFilter</p>
 * <p>Description: </p>
 * @author Simon Templer
 * @param <T> the extension object type
 * @param <F> the extension object factory type
 */
public interface FactoryFilter<T, F extends ExtensionObjectFactory<T>> {
	
	/**
	 * Accept the given factory
	 * 
	 * @param factory the extension object factory
	 * 
	 * @return if the filter accepts the factory
	 */
	public boolean acceptFactory(F factory);
	
	/**
	 * Accept the given factory collection. Only if this method returns <code>true</code>
	 * the factories managed by the collection may be accepted
	 * 
	 * @param collection the extension object factory collection
	 * 
	 * @return if the filter accepts the factory collection
	 */
	public boolean acceptCollection(ExtensionObjectFactoryCollection<T, F> collection);

}
