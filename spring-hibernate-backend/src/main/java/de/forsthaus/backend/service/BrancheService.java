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
package de.forsthaus.backend.service;

import java.util.List;

import de.forsthaus.backend.bean.ResultObject;
import de.forsthaus.backend.model.Branche;

public interface BrancheService {

	public Branche getNewBranche();

	public int getCountAllBranch();

	List<Branche> getAlleBranche();

	Branche getBrancheById(long bra_id);

	void saveOrUpdate(Branche branche);

	void delete(Branche branche);

	public List<Branche> getBrancheLikeName(String value);

	public ResultObject getAllBranches(int start, int pageSize);

	public ResultObject getAllBranchesLikeText(String text, int start, int pageSize);
}
