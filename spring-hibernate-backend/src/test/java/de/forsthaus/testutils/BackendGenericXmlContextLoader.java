/**
 * Copyright 2011 the original author or authors.
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
package de.forsthaus.testutils;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;

/**
 * @author bbruhns
 *
 */
public class BackendGenericXmlContextLoader extends GenericXmlContextLoader {
	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory factory) {
		factory.registerScope("session", new SessionScope());
		factory.registerScope("request", new RequestScope());

		final MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attributes;
		attributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);

		super.customizeBeanFactory(factory);
	}
}