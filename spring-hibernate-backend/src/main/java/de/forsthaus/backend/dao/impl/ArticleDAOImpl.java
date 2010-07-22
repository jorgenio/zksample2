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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;

import de.forsthaus.backend.dao.ArticleDAO;
import de.forsthaus.backend.model.Article;

/**
 * DAO methods implementation for the <b>Article model</b> class.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ArticleDAOImpl extends BasisNextidDaoImpl<Article> implements ArticleDAO {

	@Override
	public Article getNewArticle() {
		return new Article();
	}

	@Override
	public Article getArticleById(long art_id) {
		return get(Article.class, Long.valueOf(art_id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> getAllArticle() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.addOrder(Order.asc("artKurzbezeichnung"));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> getArticleLikeId(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.add(Restrictions.ilike("artNr", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> getArticleLikeName(String value) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Article.class);
		criteria.add(Restrictions.ilike("artKurzbezeichnung", value, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);

	}

	@Override
	public int getCountAllArticle() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Article"));
	}

	@Override
	public void save(Article entity) throws DataAccessException {
		super.save(entity);
	}

	@Override
	public void saveOrUpdate(Article article) throws DataAccessException {
		super.saveOrUpdate(article);
	}

	@Override
	public void delete(Article entity) throws DataAccessException {
		super.delete(entity);
	}

}
