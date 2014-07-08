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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

/**
 * <p>Title: ContributionItemDecorator</p>
 * <p>Description: We may not subclass (e.g. ActionContributionItem), so we have 
 * to use a decorator</p>
 * @author Simon Templer
 */
public abstract class ContributionItemDecorator implements IContributionItem {
	
	private IContributionItem item;

	/**
	 * Constructor
	 * 
	 * @param item the internal contribution item
	 */
	protected ContributionItemDecorator(IContributionItem item) {
		super();
		this.item = item;
	}

	/**
	 * @see IContributionItem#dispose()
	 */
	@Override
	public void dispose() {
		item.dispose();
	}

	/**
	 * @see IContributionItem#fill(Composite)
	 */
	@Override
	public void fill(Composite parent) {
		item.fill(parent);
	}

	/**
	 * @see IContributionItem#fill(Menu, int)
	 */
	@Override
	public void fill(Menu parent, int index) {
		item.fill(parent, index);
	}

	/**
	 * @see IContributionItem#fill(ToolBar, int)
	 */
	@Override
	public void fill(ToolBar parent, int index) {
		item.fill(parent, index);
	}

	/**
	 * @see IContributionItem#fill(CoolBar, int)
	 */
	@Override
	public void fill(CoolBar parent, int index) {
		item.fill(parent, index);
	}

	/**
	 * @see IContributionItem#getId()
	 */
	@Override
	public String getId() {
		return item.getId();
	}

	/**
	 * @see IContributionItem#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return item.isDirty();
	}

	/**
	 * @see IContributionItem#isDynamic()
	 */
	@Override
	public boolean isDynamic() {
		return item.isDynamic();
	}

	/**
	 * @see IContributionItem#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return item.isEnabled();
	}

	/**
	 * @see IContributionItem#isGroupMarker()
	 */
	@Override
	public boolean isGroupMarker() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see IContributionItem#isSeparator()
	 */
	@Override
	public boolean isSeparator() {
		return item.isSeparator();
	}

	/**
	 * @see IContributionItem#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return item.isVisible();
	}

	/**
	 * @see IContributionItem#saveWidgetState()
	 */
	@Override
	public void saveWidgetState() {
		item.saveWidgetState();
	}

	/**
	 * @see IContributionItem#setParent(IContributionManager)
	 */
	@Override
	public void setParent(IContributionManager parent) {
		item.setParent(parent);
	}

	/**
	 * @see IContributionItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		item.setVisible(visible);
	}

	/**
	 * @see IContributionItem#update()
	 */
	@Override
	public void update() {
		item.update();
	}

	/**
	 * @see IContributionItem#update(String)
	 */
	@Override
	public void update(String id) {
		item.update(id);
	}

}
