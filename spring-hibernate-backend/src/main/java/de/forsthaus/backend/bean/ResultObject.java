package de.forsthaus.backend.bean;

import java.io.Serializable;
import java.util.List;

/**
 * EN: A helper class that can holds both, a generic list and an int value. Used
 * as the totalSize value of possible records for a DB paged record list.<br>
 * DE: Hilfsklasse, die eine generische Liste und einen int Wert haelt. Wird
 * benutzt um DB paged Datensaetze sowie die moegliche Gesamtzahl einer Abfrage
 * zu halten.<br>
 * 
 * @author sgerth
 * 
 */
public class ResultObject implements Serializable {

	private static final long serialVersionUID = 1L;

	// holds a generic List
	private List<?> list;

	// holds an int
	private int totalCount;

	public ResultObject() {
	}

	public ResultObject(List<?> list, int totalCount) {
		super();
		setList(list);
		setTotalCount(totalCount);
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
