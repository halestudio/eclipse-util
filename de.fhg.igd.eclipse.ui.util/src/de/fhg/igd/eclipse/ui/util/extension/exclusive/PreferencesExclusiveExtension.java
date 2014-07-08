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

package de.fhg.igd.eclipse.ui.util.extension.exclusive;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;
import de.fhg.igd.eclipse.util.extension.exclusive.AbstractExclusiveExtension;
import de.fhg.igd.eclipse.util.extension.exclusive.ExclusiveExtension;

/**
 * <p>Title: PreferencesExclusiveExtension</p>
 * <p>Description: {@link ExclusiveExtension} that saves/loads the
 * current extension object in a {@link IPreferenceStore}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public abstract class PreferencesExclusiveExtension<T, F extends ExtensionObjectFactory<T>> extends
		AbstractExclusiveExtension<T, F> {
	
	/**
	 * The preference store
	 */
	private final IPreferenceStore preferences;
	
	/**
	 * The preference key
	 */
	private final String preferenceKey;

	/**
	 * Constructor
	 * 
	 * @param extension the internal extension
	 * @param preferences the preference store
	 * @param preferenceKey the preference key
	 */
	public PreferencesExclusiveExtension(ObjectExtension<T, F> extension,
			IPreferenceStore preferences, String preferenceKey) {
		super(extension);
		
		this.preferences = preferences;
		this.preferenceKey = preferenceKey;
		
		addListener(new ExclusiveExtensionListener<T, F>() {

			@Override
			public void currentObjectChanged(T current,
					F definition) {
				if (isSaveAllowed(current, definition)) {
					PreferencesExclusiveExtension.this.preferences.setValue(
							PreferencesExclusiveExtension.this.preferenceKey, 
							definition.getIdentifier());
				}
			}
		});
	}
	
	/**
	 * Determines if saving the state to the preferences is allowed
	 * 
	 * @param current the extension object
	 * @param definition the extension object definition
	 * 
	 * @return if saving the state is allowed
	 */
	protected boolean isSaveAllowed(T current, F definition) {
		return true;
	}
	
	/**
	 * Determines if loading the given factory from the preferences is allowed
	 * 
	 * @param definition the extension object definition
	 * 
	 * @return if loading the factory is allowed
	 */
	protected boolean isLoadAllowed(F definition) {
		return true;
	}

	/**
	 * @see AbstractExclusiveExtension#getInitialFactory()
	 */
	@Override
	protected F getInitialFactory() {
		String identifier = preferences.getString(preferenceKey);
		
		List<F> factories = getFactories();
		
		// find preferred factory to load
		for (F factory : factories) {
			if (isLoadAllowed(factory) && factory.getIdentifier().equals(identifier)) {
				return factory;
			}
		}
		
		// return default factory found
		if (!factories.isEmpty()) {
			return getDefaultFactory(factories);
		}
		else {
			return getFallbackFactory();
		}
	}

	/**
	 * Get the default factory to use
	 * 
	 * @param factories the available factories (non-empty list)
	 * 
	 * @return the factory to set as initial factory
	 */
	protected F getDefaultFactory(List<F> factories) {
		for (F factory : factories) {
			if (isLoadAllowed(factory)) {
				return factory;
			}
		}
		
		return getFallbackFactory();
	}

	/**
	 * Get the factory to use when no extensions are configured
	 * 
	 * @return the factory to use when no extensions are configured
	 */
	protected abstract F getFallbackFactory();

}
