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
package de.daibutsu.gui;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * 
 * @author bbruhns
 * 
 */
public class ActionUtil {
	final static Class<?>[] parameterType = new Class[] { ActionEvent.class };

	private static class MyActionListenerImpl extends AbstractAction {
		private static final long serialVersionUID = 3970198540624914641L;

		private final Object instance;

		private final String methodenname;

		/**
		 * @param instance
		 */
		MyActionListenerImpl(final Object instance, String name) {
			super(name);
			this.instance = instance;
			this.methodenname = "doClick" + name;
		}

		public void actionPerformed(ActionEvent e) {
			// final Logger logger = Logger.getLogger(instance.getClass());
			// if (logger.isInfoEnabled()) {
			// logger.info("instance: " + instance + ", " + "name: " +
			// methodenname);
			// }

			try {
				call(getInstance(), getMethodenname(), parameterType, new Object[] { e });
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}

		private Object getInstance() {
			return this.instance;
		}

		private String getMethodenname() {
			return this.methodenname;
		}
	}

	static Object call(Object instance, String name, Class<?>[] parameterTypes, Object[] parameterValues) throws IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		return instance.getClass().getMethod(name, parameterTypes).invoke(instance, parameterValues);
	}

	private ActionUtil() {
		super();
	}

	static public Action createAction(Object instance, String name) {
		return new MyActionListenerImpl(instance, name);
	}
}
