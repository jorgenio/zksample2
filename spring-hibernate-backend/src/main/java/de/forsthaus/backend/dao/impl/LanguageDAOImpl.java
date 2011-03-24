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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import de.forsthaus.backend.dao.LanguageDAO;
import de.forsthaus.backend.model.Language;

/**
 * EN: DAO methods implementation for the <b>Language</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Language</b> Model Klasse.<br>
 * 
 * NOT USED AT TIME !!!<br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class LanguageDAOImpl implements LanguageDAO {
	private static final List<Language> LANGUAGES;
	static {
		List<Language> languages = new ArrayList<Language>(3);
		languages.add(new Language(0, "", ""));
		languages.add(new Language(1, "de_DE", "german"));
		languages.add(new Language(2, "en_EN", "english"));

		LANGUAGES = Collections.unmodifiableList(languages);

	}

	@Override
	public List<Language> getAllLanguages() {
		return LANGUAGES;
	}

	@Override
	public Language getLanguageById(final int lan_id) {
		return (Language) CollectionUtils.find(LANGUAGES, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return lan_id == ((Language) object).getId();
			}
		});
	}

	@Override
	public Language getLanguageByLocale(final String lanLocale) {
		return (Language) CollectionUtils.find(LANGUAGES, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return StringUtils.equals(lanLocale, ((Language) object).getLanLocale());
			}
		});
	}
}
