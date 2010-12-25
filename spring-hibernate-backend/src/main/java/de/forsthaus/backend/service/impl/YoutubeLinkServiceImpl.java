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

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.dao.YoutubeLinkDAO;
import de.forsthaus.backend.model.YoutubeLink;
import de.forsthaus.backend.service.YoutubeLinkService;

/**
 * EN: Service implementation for methods that depends on <b>YoutubeLink</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>YoutubeLink</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class YoutubeLinkServiceImpl implements YoutubeLinkService {

	private YoutubeLinkDAO youtubeLinkDAO;

	public YoutubeLinkDAO getYoutubeLinkDAO() {
		return youtubeLinkDAO;
	}

	public void setYoutubeLinkDAO(YoutubeLinkDAO youtubeLinkDAO) {
		this.youtubeLinkDAO = youtubeLinkDAO;
	}

	@Override
	public void delete(YoutubeLink entity) {
		getYoutubeLinkDAO().delete(entity);
	}

	@Override
	public List<YoutubeLink> getAllYoutubeLinks() {
		return getYoutubeLinkDAO().getAllYoutubeLinks();
	}

	@Override
	public ResultObject getAllYoutubeLinks(int start, int pageSize) {
		return getYoutubeLinkDAO().getAllYoutubeLinks(start, pageSize);
	}

	@Override
	public int getCountAllYoutubeLinks() {
		return getYoutubeLinkDAO().getCountAllYoutubeLinks();
	}

	@Override
	public YoutubeLink getNewYoutubeLink() {
		return getYoutubeLinkDAO().getNewYoutubeLink();
	}

	@Override
	public YoutubeLink getYoutubeLinkByID(long id) {
		return getYoutubeLinkDAO().getYoutubeLinkByID(id);
	}

	@Override
	public void save(YoutubeLink entity) {
		getYoutubeLinkDAO().save(entity);
	}

	@Override
	public void saveOrUpdate(YoutubeLink entity) {
		getYoutubeLinkDAO().saveOrUpdate(entity);
	}

	@Override
	public YoutubeLink getRandomYoutubeLink() {
		return getYoutubeLinkDAO().getRandomYoutubeLink();
	}

}
