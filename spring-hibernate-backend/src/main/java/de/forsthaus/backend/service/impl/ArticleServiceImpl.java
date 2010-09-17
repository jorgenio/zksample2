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
package de.forsthaus.backend.service.impl;

import java.util.List;

import de.forsthaus.backend.dao.ArticleDAO;
import de.forsthaus.backend.model.Article;
import de.forsthaus.backend.service.ArticleService;

/**
 * Service implementation for methods that depends on <b>Orders</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class ArticleServiceImpl implements ArticleService {

	private ArticleDAO articleDAO;

	public ArticleDAO getArticleDAO() {
		return articleDAO;
	}

	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	@Override
	public Article getNewArticle() {
		return getArticleDAO().getNewArticle();
	}

	@Override
	public void delete(Article article) {
		getArticleDAO().delete(article);
	}

	@Override
	public List<Article> getAllArticles() {
		return getArticleDAO().getAllArticle();
	}

	@Override
	public Article getArticleById(long art_id) {
		return null;
	}

	@Override
	public void saveOrUpdate(Article article) {
		getArticleDAO().saveOrUpdate(article);
	}

	@Override
	public List<Article> getArticleLikeId(String value) {
		return getArticleDAO().getArticleLikeId(value);
	}

	@Override
	public List<Article> getArticleLikeName(String value) {
		return getArticleDAO().getArticleLikeName(value);
	}

	@Override
	public int getCountAllArticle() {
		return getArticleDAO().getCountAllArticle();
	}

}
