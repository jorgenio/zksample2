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
import de.forsthaus.backend.dao.BrancheDAO;
import de.forsthaus.backend.model.Branche;
import de.forsthaus.backend.service.BrancheService;

/**
 * EN: Service implementation for methods that depends on <b>Branches</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Branchen</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class BrancheServiceImpl implements BrancheService {

	private BrancheDAO brancheDAO;

	public BrancheDAO getBrancheDAO() {
		return brancheDAO;
	}

	public void setBrancheDAO(BrancheDAO brancheDAO) {
		this.brancheDAO = brancheDAO;
	}

	@Override
	public Branche getNewBranche() {
		return getBrancheDAO().getNewBranche();
	}

	@Override
	public void delete(Branche branche) {
		getBrancheDAO().delete(branche);
	}

	@Override
	public List<Branche> getAllBranches() {
		return getBrancheDAO().getAllBranches();
	}

	@Override
	public void saveOrUpdate(Branche branche) {
		getBrancheDAO().saveOrUpdate(branche);
	}

	@Override
	public Branche getBrancheById(long bra_id) {
		return getBrancheDAO().getBrancheByID(bra_id);
	}

	@Override
	public Branche getBrancheByName(String braBezeichnung) {
		return getBrancheDAO().getBrancheByName(braBezeichnung);
	}

	@Override
	public List<Branche> getBranchesLikeName(String string) {
		return getBrancheDAO().getBranchesLikeName(string);
	}

	@Override
	public int getCountAllBranches() {
		return getBrancheDAO().getCountAllBranches();
	}

	@Override
	public ResultObject getAllBranches(int start, int pageSize) {
		return getBrancheDAO().getAllBranches(start, pageSize);
	}

	@Override
	public ResultObject getAllBranchesLikeText(String text, int start, int pageSize) {
		return getBrancheDAO().getAllBranchesLikeText(text, start, pageSize);
	}

}
