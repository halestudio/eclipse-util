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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


/**
 * <p>Title: AbstractConfigurationFactory</p>
 * @param <T> the extension type
 * @author Simon Templer
 */
public abstract class AbstractConfigurationFactory<T> extends AbstractObjectFactory<T>  {
	
	/**
	 * The configuration element defining the object
	 */
	protected final IConfigurationElement conf;
	
	private final String classAttributeName;
	
	/**
	 * Create a object factory based on an {@link IConfigurationElement}
	 * 
	 * @param conf the configuration element defining the object
	 * @param classAttributeName the name of the attribute defining
	 *   the object type
	 */
	protected AbstractConfigurationFactory(final IConfigurationElement conf, 
			final String classAttributeName) {
		this.conf = conf;
		this.classAttributeName = classAttributeName;
	}
	
	/**
	 * @see ExtensionObjectDefinition#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return conf.getAttribute(classAttributeName);
	}
	
	/**
	 * @see ExtensionObjectFactory#createExtensionObject()
	 */
	@Override
	public T createExtensionObject() throws Exception {
		return (T) conf.createExecutableExtension(classAttributeName);
	}
	
	/**
	 * Utility method to get the URL of an icon defined in the
	 *   configuration element
	 *   
	 * @param iconAttribute the name of the icon attribute
	 * 
	 * @return the icon URL or <code>null</code> if none is defined
	 *   or the bundle is not found
	 */
	protected URL getIconURL(String iconAttribute) {
		String icon = conf.getAttribute(iconAttribute);
		if (icon != null && !icon.isEmpty()) {
			String contributor = conf.getDeclaringExtension().getContributor().getName();
			Bundle bundle = Platform.getBundle(contributor);
			
			if (bundle != null) {
				return bundle.getResource(icon);
			}
		}
		
		return null;
	}
	
}