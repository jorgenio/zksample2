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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import de.forsthaus.backend.model.SecRight;
import de.forsthaus.backend.model.SecUser;
import de.forsthaus.backend.service.UserService;

/**
 * This class implements the spring-security UserDetailService.<br>
 * It's been configured in the spring security xml contextfile.<br>
 * 
 * @author bbruhns
 * @see de.forsthaus.policy
 */
public class PolicyManager implements UserDetailsService, Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = Logger.getLogger(PolicyManager.class);

	private transient UserService userService;
	public UserDetails _userDetails;

	@Override
	public UserDetails loadUserByUsername(String userId) {

		SecUser user = null;
		GrantedAuthority[] grantedAuthorities = null;

		try {
			user = getUserByLoginname(userId);

			if (user == null) {
				throw new UsernameNotFoundException("Invalid User");
			}

			// TEST
			// String context = user.getUsrLocale(); // i.e. 'en_EN' or 'de_DE'
			// if (!StringUtils.isEmpty(context)) {
			// Labels.register(new GeneralLabelLocator(context));
			// }

			grantedAuthorities = getGrantedAuthority(user);

		} catch (NumberFormatException e) {
			throw new DataRetrievalFailureException("Cannot loadUserByUsername userId:" + userId + " Exception:" + e.getMessage(), e);
		}

		// Create the UserDetails object for a specified user with
		// their grantedAuthorities List.
		UserDetails userDetails = new UserImpl(user, grantedAuthorities);

		if (logger.isDebugEnabled()) {
			logger.debug("Rechte für '" + user.getUsrLoginname() + "' (ID: " + user.getId() + ") ermittelt. (" + Arrays.toString(grantedAuthorities)
					+ ") [" + this + "]");

			// for (GrantedAuthority grantedAuthority : grantedAuthorities) {
			// logger.debug(grantedAuthority.getAuthority());
			// }
		}

		// neu wegen clustering ?
		_userDetails = userDetails;

		return userDetails;

	}

	/**
	 * Gets the User object by his stored userName.<br>
	 * 
	 * @param userName
	 * @return
	 */
	public SecUser getUserByLoginname(final String userName) {
		return getUserService().getUserByLoginname(userName);
	}

	/**
	 * Fills the GrantedAuthorities List for a specified user.<br>
	 * 1. Gets a unique list of rights that a user have.<br>
	 * 2. Creates GrantedAuthority objects from all rights. <br>
	 * 3. Creates a GrantedAuthorities list from all GrantedAuthority objects.<br>
	 * 
	 * @param user
	 * @return
	 */
	private GrantedAuthority[] getGrantedAuthority(SecUser user) {

		// get the list of rights for a specified user.
		Collection<SecRight> rights = getUserService().getRightsByUser(user);

		ArrayList<GrantedAuthority> rechteGrantedAuthorities = new ArrayList<GrantedAuthority>(rights.size());

		// now create for all rights a GrantedAuthority
		// and fill the GrantedAuthority List with these authorities.
		for (SecRight right : rights) {
			rechteGrantedAuthorities.add(new GrantedAuthorityImpl(right.getRigName()));
		}
		GrantedAuthority[] grantedAuthorities = rechteGrantedAuthorities.toArray(new GrantedAuthority[rechteGrantedAuthorities.size()]);
		return grantedAuthorities;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void test() {
		System.out.println("PolicyManager.test() -> " + loadUserByUsername("user"));
	}
}
