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
 * <p>Title: ExtensionObjectFactory</p>
 * <p>Description: Interface for extension object factories</p>
 * @param <T> the extension type
 * @author Simon Templer
 */
public interface ExtensionObjectFactory<T> extends ExtensionObjectDefinition {

	/**
	 * Create an extension object
	 * 
	 * @return the extension object
	 * @throws Exception if the creation failed
	 */
	public T createExtensionObject() throws Exception;
	
	/**
	 * Get if a call to {@link #configure()} is allowed
	 * 
	 * @return if a call to {@link #configure()} is allowed
	 */
	public boolean allowConfigure();
	
	/**
	 * Configure the extension object creation
	 * 
	 * @return if the extension object has to be recreated
	 */
	public boolean configure();
	
	/**
	 * Dispose an extension object instance
	 * 
	 * @param instance the extension object
	 */
	public void dispose(T instance);

}
