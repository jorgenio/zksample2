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
package de.forsthaus.util;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;

/**
 * Helper class for showing the zkoss component tree in the console for a root
 * component.
 * 
 * Call it with ZkossComponentTreeUtil.getZulTree(aComponent)
 * 
 * @author bbruhns
 * 
 */
public class ZkossComponentTreeUtil {
	static public CharSequence getZulTree(Component component) {
		return new ZkossComponentTreeUtil().getZulTreeImpl(component);
	}

	private ZkossComponentTreeUtil() {
		super();
	}

	private CharSequence getZulTreeImpl(Component component) {
		return getZulTreeImpl(component, new StringBuilder(6000), -1);
	}

	@SuppressWarnings("unchecked")
	private StringBuilder getZulTreeImpl(Component component, StringBuilder result, int tiefe) {
		++tiefe;
		result.append(StringUtils.leftPad("", tiefe << 2) + "-> " + component + "\n");
		if (component.getChildren() != null) {
			for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
				getZulTreeImpl((Component) iterator.next(), result, tiefe);
			}
		}

		result.append(StringUtils.leftPad("", tiefe << 2) + "<- " + component + "\n");
		return result;
	}
}
