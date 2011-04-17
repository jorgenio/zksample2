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
import de.forsthaus.backend.dao.ApplicationNewsDAO;
import de.forsthaus.backend.dao.YoutubeLinkDAO;
import de.forsthaus.backend.model.ApplicationNews;
import de.forsthaus.backend.model.YoutubeLink;

/**
 * EN: DAO methods implementation for the <b>Application News</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Anwendnungs News</b> Model
 * Klasse.<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class ApplicationNewsDAOImpl extends BasisDAO<ApplicationNews> implements ApplicationNewsDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationNews> getAllApplicationNews() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ApplicationNews.class);
		criteria.addOrder(Order.desc("date"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllApplicationNews() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from ApplicationNews"));
	}

}
