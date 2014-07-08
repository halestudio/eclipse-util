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

import org.eclipse.core.runtime.IConfigurationElement;

import de.fhg.igd.osgi.util.OsgiUtils;

/**
 * Utility methods for extension points
 * @author Simon Templer
 */
public abstract class ExtensionUtil {
	
	/**
	 * Load a class contained in an {@link IConfigurationElement} as attribute
	 * @param conf the configuration element
	 * @param classAttributeName the name of the attribute containing the class name
	 * @return the loaded class or <code>null</code> if it could not be found
	 */
	public static Class<?> loadClass(IConfigurationElement conf, String classAttributeName) { 
		String bundleName = conf.getContributor().getName();
		return OsgiUtils.loadClass(conf.getAttribute(classAttributeName), bundleName);
	}

}
