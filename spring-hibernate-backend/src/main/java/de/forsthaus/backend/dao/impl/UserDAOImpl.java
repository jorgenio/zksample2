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
package de.forsthaus.backend.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.UserDAO;
import de.forsthaus.backend.model.SecUser;

/**
 * EN: DAO methods implementation for the <b>SecUser</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>SecUser</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class UserDAOImpl extends BasisDAO<SecUser> implements UserDAO {

	@Override
	public SecUser getNewSecUser() {
		return new SecUser();
	}

	@Override
	public List<SecUser> getAllUsers() {
		return getHibernateTemplate().loadAll(SecUser.class);
	}

	public SecUser getUserByID(final Long usr_id) {
		return get(SecUser.class, usr_id);
	}

	@SuppressWarnings("unchecked")
	public SecUser getUserByFiluserNr(String usr_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.eq("usrNr", usr_nr));

		return (SecUser) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	public SecUser getUserByLoginname(final String userName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.eq("usrLoginname", userName));
		return (SecUser) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	public SecUser getUserByNameAndPassword(final String userName, final String userPassword) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.eq("usrLoginname", userName));
		criteria.add(Restrictions.eq("usrPassword", userPassword));

		return (SecUser) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecUser> getUsersLikeLastname(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.like("usrLastname", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecUser> getUsersLikeLoginname(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.like("usrLoginname", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecUser> getUsersLikeEmail(String email) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.like("usrEmail", email, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecUser> getUsersByLoginname(String loginName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SecUser.class);
		criteria.add(Restrictions.like("usrLoginname", loginName));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllSecUser() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from SecUser"));
	}

}
