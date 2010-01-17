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
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.reflect.Pointcut;

/**
 * @author bbruhns
 * 
 */
public class UserCounterSpringSessionListenerImpl implements Serializable {

	private static final long serialVersionUID = 8979460663616009375L;
	private UserCounterImpl userCounter;
	private transient static final Logger logger = Logger.getLogger(UserCounterSpringSessionListenerImpl.class);

	public void startSession() {
		System.err.println("-----------------------------------------------");
		logger.warn("start");
	}

	public void disposeSession() {
		System.err.println("###############################################");
		logger.warn("ende");
	}

	public void registerNewSession(Pointcut pointcut) {
		// public void registerNewSession(Object returnValue, Method method,
		// Object[] args, Object target) throws Throwable {
		// final String session = pointcut. user1.toString();
		// final String user = session1;
		// System.err.println("###############################################");
		// System.err.println(session + " -> " + user);

		// return call.proceed();
	}

	public void removeSessionInformation(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		final String user = (String) args[0];
		System.err.println("-----------------------------------------");
		System.err.println(" -> " + user);

		// return call.proceed();
	}

	public UserCounterImpl getUserCounter() {
		return userCounter;
	}

	public void setUserCounter(UserCounterImpl userCounter) {
		this.userCounter = userCounter;
	}

}
