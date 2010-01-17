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
package org.myfirm.webui.reports;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationTrustResolver;
import org.springframework.security.AuthenticationTrustResolverImpl;
import org.springframework.security.context.HttpSessionContextIntegrationFilter;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

import de.forsthaus.backend.model.SecUser;

/**
 * UserCounterListener class used to count the current number of active users
 * for the applications. Does this by counting how many user objects are stuffed
 * into the session. It also grabs these users and exposes them in the servlet
 * context.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserCounterListener implements ServletContextListener, HttpSessionAttributeListener {
	/**
	 * Name of user counter variable
	 */
	public static final String COUNT_KEY = "userCounter";
	/**
	 * Name of users Set in the ServletContext
	 */
	public static final String USERS_KEY = "userNames";
	/**
	 * The default event we're looking to trap.
	 */
	public static final String EVENT_KEY = HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY;
	private transient ServletContext servletContext;
	private int counter;
	private Set<SecUser> users;

	/**
	 * Initialize the context
	 * 
	 * @param sce
	 *            the event
	 */
	public synchronized void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
	}

	/**
	 * Set the servletContext, users and counter to null
	 * 
	 * @param event
	 *            The servletContextEvent
	 */
	public synchronized void contextDestroyed(ServletContextEvent event) {
		servletContext = null;
		users = null;
		counter = 0;
	}

	synchronized void incrementUserCounter() {
		counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
		counter++;
		servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));

		System.out.println("user neu eingeloggt");
	}

	synchronized void decrementUserCounter() {
		int counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
		counter--;

		if (counter < 0) {
			counter = 0;
		}

		servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
	}

	@SuppressWarnings("unchecked")
	synchronized void addUsername(SecUser user) {
		users = (Set) servletContext.getAttribute(USERS_KEY);

		if (users == null) {
			users = new LinkedHashSet<SecUser>();
		}

		if (!users.contains(user)) {
			users.add(user);
			servletContext.setAttribute(USERS_KEY, users);
			incrementUserCounter();
		}
	}

	synchronized void removeUsername(SecUser user) {
		users = (Set) servletContext.getAttribute(USERS_KEY);

		if (users != null) {
			users.remove(user);
		}

		servletContext.setAttribute(USERS_KEY, users);
		decrementUserCounter();
	}

	/**
	 * This method is designed to catch when user's login and record their name
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 * @param event
	 *            the event to process
	 */
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			if (securityContext.getAuthentication().getPrincipal() instanceof SecUser) {
				SecUser user = (SecUser) securityContext.getAuthentication().getPrincipal();
				addUsername(user);
			}
		}
	}

	private boolean isAnonymous() {
		AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx != null) {
			Authentication auth = ctx.getAuthentication();
			return resolver.isAnonymous(auth);
		}
		return true;
	}

	/**
	 * When user's logout, remove their name from the hashMap
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 * @param event
	 *            the session binding event
	 */
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			Authentication auth = securityContext.getAuthentication();
			if (auth != null && (auth.getPrincipal() instanceof SecUser)) {
				SecUser user = (SecUser) auth.getPrincipal();
				removeUsername(user);
			}
		}
	}

	/**
	 * Needed for Acegi Security 1.0, as it adds an anonymous user to the
	 * session and then replaces it after authentication.
	 * http://forum.springframework.org/showthread.php?p=63593
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 * @param event
	 *            the session binding event
	 */
	public void attributeReplaced(HttpSessionBindingEvent event) {
		if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			if (securityContext.getAuthentication() != null) {
				if (securityContext.getAuthentication().getPrincipal() instanceof SecUser) {
					SecUser user = (SecUser) securityContext.getAuthentication().getPrincipal();
					addUsername(user);
				}
			}
		}
	}
}