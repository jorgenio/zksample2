/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.backend.util;

import java.lang.reflect.InvocationTargetException;

/**
 * EN: Utility class for beans.<br>
 * DE: Utility Klasse zum Manipulieren von Beans.<br>
 * 
 * @author Stephan Gerth
 */
public class ZksampleBeanUtils {

	/**
	 * EN: Clone a bean based on the available property getters and setters,
	 * even if the bean class itself does not implement Cloneable.<br>
	 * Used by closing a tab/dialog to compare the initialBean's properties with
	 * the currentBean's properties to make a decision if the changed values
	 * must be stored.<br>
	 * DE: Gibt ein geklontes Bean aufgrund seiner vorhandenen Getter/Setter
	 * Werte zurueck. <br>
	 * Wird beim schliessen eines Tabs/Dialogs benutzt um die OriginalBean mit
	 * der aktuellenBean zu vergleichen. Besteht ein Unterschied kann
	 * entschieden werden, ob abgespeichert werden muss.<br>
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object cloneBean(Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
	}

}
