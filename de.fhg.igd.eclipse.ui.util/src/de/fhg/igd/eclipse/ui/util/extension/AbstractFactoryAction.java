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

package de.fhg.igd.eclipse.ui.util.extension;

import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;

/**
 * <p>Title: AbstractFactoryAction</p>
 * <p>Description: Abstract action for {@link ExtensionObjectFactory}s</p>
 * @param <F> the factory type
 * @author Simon Templer
 */
public class AbstractFactoryAction<F extends ExtensionObjectFactory<?>> extends Action {

	private final F factory;

	/**
	 * Constructor
	 * 
	 * @param factory the extension object factory
	 * @param style the action style, e.g. {@link IAction#AS_CHECK_BOX}
	 */
	public AbstractFactoryAction(F factory, int style) {
		super(factory.getDisplayName(), style);
		
		URL iconURL = factory.getIconURL();
		if (iconURL != null) {
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(iconURL));
			} catch (Exception e) {
				// ignore
			}
		}
		
		this.factory = factory;
	}
	
	/**
	 * Get the factory
	 * 
	 * @return the factory
	 */
	public F getFactory() {
		return factory;
	}
	
}
