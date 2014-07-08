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

package de.fhg.igd.eclipse.ui.util.security;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

import de.fhg.igd.slf4jplus.ALogger;
import de.fhg.igd.slf4jplus.ALoggerFactory;

/**
 * Password field editor that uses {@link ISecurePreferences}
 * 
 * When using it be careful to also include the
 * org.eclipse.equinox.security.ui bundle or add you
 * own secure storage module through the extension point
 * 
 * @author Simon Templer
 */
public class PasswordFieldEditor extends FieldEditor {
	
	private static final ALogger log = ALoggerFactory.getLogger(PasswordFieldEditor.class);
	
	/**
	 * The label
	 */
	private Label label;
	
	/**
	 * The text field
	 */
	private Text text;
	
	private final String secureNodeName;
	
	private final String keyName;
	
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param secureNodeName the name of the node in the {@link ISecurePreferences}
	 * @param keyName the name of the key representing the password in that node
	 * @param name the field name (displayed in the label)
	 * @param fieldEditorParent the parent composite
	 */
	public PasswordFieldEditor(String secureNodeName,
			String keyName, String name,
			Composite fieldEditorParent) {
		this.keyName = keyName;
		this.secureNodeName = secureNodeName;
		this.name = name;
		
		Layout layout = fieldEditorParent.getLayout();
		doFillIntoGrid(fieldEditorParent, (layout instanceof GridLayout)?(((GridLayout) layout).numColumns):(2));
	}

	/**
	 * @see FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, numColumns - 1, 1));
	}

	/**
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		label = new Label(parent, SWT.NONE);
		label.setText(name);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		text = new Text(parent, SWT.PASSWORD | SWT.BORDER);

		adjustForNumColumns(numColumns);
	}

	/**
	 * @see FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		try {
			text.setText(SecurePreferencesFactory.getDefault().node(secureNodeName).get(keyName, ""));
		} catch (StorageException e) {
			text.setText("");
			log.warn("Can't access secure preferences", e);
		}
	}

	/**
	 * @see FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		text.setText("");
	}
	
	@Override
	public void store() {
		//override this method to fix NPE that happens when the preference
		//value should be restored to its default value. The NPE happens because
		//we don't pass the keyName to the super class
		doStore();
	}

	/**
	 * @see FieldEditor#doStore()
	 */
	@Override
	protected void doStore() {
		try {
			String password = text.getText();
			SecurePreferencesFactory.getDefault().node(secureNodeName).put(
					keyName, password, password != null && !password.isEmpty());
		} catch (StorageException e) {
			log.userError("Unable to save password to secure preferences", e);
		}
	}

	/**
	 * @see FieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

}
