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
package de.forsthaus.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
This class represents the types of securing. <br>
 <br>
This domain model have no corresponding table in a database and has a fixed
 length of records that should see as the types of what to secure. <br>
 It's only for a better overview in the security managing tools. <br>
<p>
 <table border=0 cellspacing=3 cellpadding=0>

     <tr bgcolor="#ccccff">
         <th align=left>Int
         <th align=left>Type
     <tr>
         <td><code>0</code>
         <td>Page
     <tr bgcolor="#eeeeff">
         <td><code>1</code>
         <td>Menu Category
     <tr>
         <td><code>2</code>
         <td>Menu Item
     <tr bgcolor="#eeeeff">
         <td><code>3</code>
         <td>Method/Event
     <tr>
         <td><code>4</code>
         <td>DomainObject/Property
     <tr bgcolor="#eeeeff">

         <td><code>5</code>
         <td>Tab
     <tr>
         <td><code>6</code>
         <td>Component
 </table>
 *
 * @author bbruhns
 * @author sgerth
 */
final public class SecTyp implements Serializable {
	private static final long serialVersionUID = 5129871978459891412L;
	final public static List<SecTyp> ALLTYPES;
	final private static Map<Integer, SecTyp> STDID_MAP;
	final public static SecTyp EMPTY_SECTYP = new SecTyp(-1, "");

	static {
		List<SecTyp> result = new ArrayList<SecTyp>();

		result.add(new SecTyp(0, "Page"));
		result.add(new SecTyp(1, "Menu Category"));
		result.add(new SecTyp(2, "Menu Item"));
		result.add(new SecTyp(3, "Method"));
		result.add(new SecTyp(4, "DomainObject/Property"));
		result.add(new SecTyp(5, "Tab"));
		result.add(new SecTyp(6, "Component"));

		ALLTYPES = Collections.unmodifiableList(result);
		STDID_MAP = new HashMap<Integer, SecTyp>(result.size());

		for (SecTyp secTyp : result) {
			STDID_MAP.put(Integer.valueOf(secTyp.stpId), secTyp);
		}
	}

	public static SecTyp getTypById(int typ_id) {
		return STDID_MAP.get(Integer.valueOf(typ_id));
	}

	final private int stpId;
	final private String stpTypname;

	private SecTyp(int stpId, String stp_typname) {
		this.stpId = stpId;
		this.stpTypname = stp_typname;

	}

	public int getStpId() {
		return stpId;
	}

	public String getStpTypname() {
		return stpTypname;
	}

	static public List<SecTyp> getAllTypes() {
		return ALLTYPES;
	}

	@Override
	public int hashCode() {
		return stpId;
	}

	public boolean equals(SecTyp secTyp) {
		return getStpId() == secTyp.getStpId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SecTyp) {
			SecTyp secTyp = (SecTyp) obj;
			return equals(secTyp);
		}

		return false;
	}
}
