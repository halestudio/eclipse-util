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

import java.net.URL;

/**
 * <p>Title: AbstractObjectFactory</p>
 * <p>Description: Abstract extension object factory</p>
 * @param <T> the extension object type
 * @author Simon Templer
 */
public abstract class AbstractObjectFactory<T> extends AbstractObjectDefinition
	implements ExtensionObjectFactory<T>{

	/**
	 * Default implementation that returns <code>null</code>.
	 * Subclasses may override it to determine an icon URL
	 * 
	 * @see ExtensionObjectDefinition#getIconURL()
	 */
	@Override
	public URL getIconURL() {
		return null;
	}

	/**
	 * @see ExtensionObjectFactory#allowConfigure()
	 */
	@Override
	public boolean allowConfigure() {
		return false;
	}

	/**
	 * @see ExtensionObjectFactory#configure()
	 */
	@Override
	public boolean configure() {
		return false;
	}

}
