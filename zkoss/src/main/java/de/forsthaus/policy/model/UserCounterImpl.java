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
package de.forsthaus.policy.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import de.forsthaus.policy.UserCounter;

/**
 * @author bbruhns
 * 
 */
public class UserCounterImpl implements UserCounter, Serializable {

	private static final long serialVersionUID = -2617461135135795178L;
	private transient static final Logger logger = Logger.getLogger(UserCounterImpl.class);

	public static class SessionListener {

		private UserCounterImpl userCounter;
		private final Log logger = LogFactory.getLog(getClass());

		public void startSession() {
			logger.warn("start");
		}

		public void disposeSession() {
			logger.warn("ende");
		}

		public UserCounterImpl getUserCounter() {
			return userCounter;
		}

		public void setUserCounter(UserCounterImpl userCounter) {
			this.userCounter = userCounter;
		}

	}

}
