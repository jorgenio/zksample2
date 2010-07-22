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

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.dao.BrancheDAO;
import de.forsthaus.backend.model.Branche;

/**
 * DAO methods implementation for the <b>Branche model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class BrancheDAOImpl extends BasisNextidDaoImpl<Branche> implements BrancheDAO {

	@Override
	public Branche getNewBranche() {
		return new Branche();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Branche> getAlleBranche() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.addOrder(Order.asc("braBezeichnung"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Branche getBrancheByID(long bra_id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(bra_id)));

		return (Branche) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Branche getBrancheByName(String braBezeichnung) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.add(Restrictions.eq("braBezeichnung", braBezeichnung));

		return (Branche) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Branche> getBrancheLikeName(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.add(Restrictions.ilike("braBezeichnung", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getBrancheSize() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public int getCountAllBranch() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Branche"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject getAllBranches(int start, int pageSize) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
		criteria.addOrder(Order.asc("braBezeichnung"));

		int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

		List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

		return new ResultObject(list, totalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject getAllBranchesLikeText(String text, int start, int pageSize) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);

		if (!StringUtils.isEmpty(text)) {
			criteria.add(Restrictions.ilike("braBezeichnung", text, MatchMode.ANYWHERE));
		}

		criteria.addOrder(Order.asc("braBezeichnung"));

		int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

		List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

		return new ResultObject(list, totalCount);
	}

}
