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

/**
 * <p>Title: AbstractObjectInfo</p>
 * <p>Description: {@link ExtensionObjectDefinition} implementation that provides
 * {@link #equals(Object)} and {@link #hashCode()} implementations based
 * on {@link #getIdentifier()}.
 * The implementation of {@link #compareTo(ExtensionObjectDefinition)}
 * allows sorting by {@link #getPriority()}, @link #getDisplayName()} and
 * {@link #getIdentifier()} (ascending)</p>
 * @author Simon Templer
 */
public abstract class AbstractObjectDefinition implements ExtensionObjectDefinition, Prioritizable {
	
	/**
	 * @see Prioritizable#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}
	
	/**
	 * @see Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ExtensionObjectDefinition other) {
		if (this == other) return 0;
		
		if (other instanceof AbstractObjectDefinition) {
			int otherPriority = ((Prioritizable) other).getPriority();
			int priority = getPriority();
			
			if (priority < otherPriority) {
				return -1;
			}
			else if (priority > otherPriority) {
				return 1;
			}
		}
		
		if (getDisplayName().equals(other.getDisplayName())) {
			return getIdentifier().compareTo(other.getIdentifier());
		}
		else {
			return getDisplayName().compareTo(other.getDisplayName());
		}
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		else if (obj instanceof ExtensionObjectDefinition) {
			return getIdentifier().equals(((ExtensionObjectDefinition) obj).getIdentifier());
		}
		else {
			return false;
		}
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

}
