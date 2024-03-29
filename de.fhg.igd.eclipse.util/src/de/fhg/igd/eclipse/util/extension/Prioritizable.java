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
 * Interface for objects with a priority.
 * Lower values represent a higher priority. Negative values are allowed.
 * 
 * See also {@link AbstractObjectDefinition#compareTo(ExtensionObjectDefinition)}
 * 
 * @author Simon Templer
 */
public interface Prioritizable {

	/**
	 * The object's priority. May for instance be used for sorting.
	 * 
	 * @return the priority
	 */
	public abstract int getPriority();

}