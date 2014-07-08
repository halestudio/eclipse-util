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

package de.fhg.igd.eclipse.ui.util;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * <p>Title: Activator</p>
 * <p>Description: The plugin activator</p>
 * @author Simon Templer
 */
public class Activator extends AbstractUIPlugin {
	
	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "de.fhg.igd.eclipse.ui.util"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static Activator _plugin;
	
	/**
	 * @see BundleActivator#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_plugin = this;
	}

	/**
	 * @see BundleActivator#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		_plugin = null;
		super.stop(context);
	}
	
	/**
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return _plugin;
	}

}
