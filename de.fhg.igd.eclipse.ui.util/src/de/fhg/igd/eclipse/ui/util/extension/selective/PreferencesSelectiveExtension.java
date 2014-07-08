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

package de.fhg.igd.eclipse.ui.util.extension.selective;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.ObjectExtension;
import de.fhg.igd.eclipse.util.extension.selective.AbstractSelectiveExtension;
import de.fhg.igd.eclipse.util.extension.selective.SelectiveExtension;

/**
 * <p>Title: PreferencesSelectiveExtension</p>
 * <p>Description: {@link SelectiveExtension} that stores its settings
 * in an {@link IPreferenceStore}</p>
 * @param <T> the extension object type
 * @param <F> the factory type
 * @author Simon Templer
 */
public class PreferencesSelectiveExtension<T, F extends ExtensionObjectFactory<T>> extends
		AbstractSelectiveExtension<T, F> {
	
	/**
	 * Preference value for activating all extension objects on init
	 */
	public static final String PREFERENCE_ALL = 
		PreferencesSelectiveExtension.class.getName() + ".ALL"; //$NON-NLS-1$

	private final IPreferenceStore preferences;
	
	private final String preferenceKey;
	
	/**
	 * Constructor
	 * 
	 * @param extension the extension
	 * @param preferences the preference store
	 * @param preferenceKey the preference key
	 */
	public PreferencesSelectiveExtension(ObjectExtension<T, F> extension,
			IPreferenceStore preferences, String preferenceKey) {
		super(extension);
		
		this.preferences = preferences;
		this.preferenceKey = preferenceKey;
		
		addListener(new SelectiveExtensionListener<T, F>() {

			@Override
			public void activated(T object, F definition) {
				Set<String> ids = loadIdentifiers();
				
				ids.add(definition.getIdentifier());
				
				saveIdentifiers(ids);
			}

			@Override
			public void deactivated(T object, F definition) {
				Set<String> ids = loadIdentifiers();
				
				ids.remove(definition.getIdentifier());
				
				saveIdentifiers(ids);
			}
		});
	}

	/**
	 * @see AbstractSelectiveExtension#activateOnInit(ExtensionObjectFactory)
	 */
	@Override
	protected boolean activateOnInit(F factory) {
		Set<String> ids = loadIdentifiers();
		
		return ids.contains(factory.getIdentifier());
	}
	
	/**
	 * Load the identifiers
	 * 
	 * @return the set of identifiers
	 */
	private Set<String> loadIdentifiers() {
		String value = preferences.getString(preferenceKey);
		
		Set<String> result = new HashSet<String>();
		
		if (value.equals(PREFERENCE_ALL)) {
			for (F factory : getFactories()) {
				result.add(factory.getIdentifier());
			}
		}
		else {
			String[] split = value.split(","); //$NON-NLS-1$
			
			for (String id : split) {
				result.add(id);
			}
		}
		
		return result;
	}
	
	/**
	 * Save the given identifiers
	 * 
	 * @param identifiers the identifiers to save
	 */
	private void saveIdentifiers(Set<String> identifiers) {
		StringBuilder value = new StringBuilder();
		
		boolean init = true;
		for (String id : identifiers) {
			if (init) {
				init = false;
			}
			else {
				value.append(',');
			}
			value.append(id);
		}
		
		preferences.setValue(preferenceKey, value.toString());
	}

}
