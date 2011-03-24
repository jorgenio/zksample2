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
import java.util.Random;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.dao.YoutubeLinkDAO;
import de.forsthaus.backend.model.YoutubeLink;

/**
 * EN: DAO methods implementation for the <b>YoutubeLink</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>YoutubeLink</b> Model Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class YoutubeLinkDAOImpl extends BasisDAO<YoutubeLink> implements YoutubeLinkDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<YoutubeLink> getAllYoutubeLinks() {
		DetachedCriteria criteria = DetachedCriteria.forClass(YoutubeLink.class);
		criteria.addOrder(Order.asc("interpret"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject getAllYoutubeLinks(int start, int pageSize) {
		DetachedCriteria criteria = DetachedCriteria.forClass(YoutubeLink.class);
		criteria.addOrder(Order.asc("interpret"));

		int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

		List<YoutubeLink> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

		return new ResultObject(list, totalCount);
	}

	@Override
	public int getCountAllYoutubeLinks() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from YoutubeLink"));
	}

	@Override
	public YoutubeLink getNewYoutubeLink() {
		return new YoutubeLink();
	}

	@SuppressWarnings("unchecked")
	@Override
	public YoutubeLink getYoutubeLinkByID(long id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(YoutubeLink.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(id)));

		return (YoutubeLink) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@SuppressWarnings("unchecked")
	@Override
	public YoutubeLink getRandomYoutubeLink() {
		DetachedCriteria criteria = DetachedCriteria.forClass(YoutubeLink.class);

		try {
			int recCount = getCountAllYoutubeLinks();
			if (recCount > 0) {
				Random random = new Random();
				int selRecord = random.nextInt(recCount);

				return (YoutubeLink) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria, selRecord, 1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
