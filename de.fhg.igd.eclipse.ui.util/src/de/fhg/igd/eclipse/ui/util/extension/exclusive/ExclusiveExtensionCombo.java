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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.fhg.igd.eclipse.util.extension.ExtensionObjectFactory;
import de.fhg.igd.eclipse.util.extension.exclusive.ExclusiveExtension;

/**
 * Combo box for selecting an extension object
 * @author Simon Templer
 * @param <T> the extension object type
 * @param <F> the extension object factory type
 */
public class ExclusiveExtensionCombo<T, F extends ExtensionObjectFactory<T>> {
	
	private final ExclusiveExtension<T, F> extension;
	
	private final ComboViewer viewer;

	/**
	 * Constructor
	 * 
	 * @param extension the extension
	 * @param parent the parent composite for the combo
	 */
	public ExclusiveExtensionCombo(ExclusiveExtension<T, F> extension,
			Composite parent) {
		super();
		
		this.extension = extension;
		
		this.viewer = new ComboViewer(parent);
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public Image getImage(Object element) {
				return ExclusiveExtensionCombo.this.getImage((F) element);
			}

			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				return ExclusiveExtensionCombo.this.getText((F) element);
			}
			
		});
		
		viewer.setInput(extension.getFactories());
		viewer.setSelection(new StructuredSelection(extension.getCurrentDefinition()));
	}

	/**
	 * Get the text representation for the given factory
	 * 
	 * @param definition the extension object factory
	 * 
	 * @return the factory's text representation
	 */
	protected String getText(F definition) {
		return definition.getDisplayName();
	}

	/**
	 * Get the image representation for the given factory
	 * 
	 * @param definition the extension object factory
	 * 
	 * @return the factory's image representation
	 */
	protected Image getImage(F definition) {
		//TODO use getIconUrl - needs disposing created images
		return null;
	}

	/**
	 * Get the control
	 * 
	 * @return the control
	 */
	public Control getControl() {
		return viewer.getControl();
	}
	
	/**
	 * Apply the selected factory to the extension. If the setting is saved
	 * depends on the exclusive extension implementation. 
	 */
	public void apply() {
		ISelection sel = viewer.getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			extension.setCurrent((F) ((IStructuredSelection) sel).getFirstElement());
		}
	}
}
