package de.forsthaus.backend.bean;

import java.io.Serializable;
import java.util.List;

/**
 * A helper class that van hold a generic list and an int value for the count of
 * the size of the list.<br>
 * Used by a paging Sample.<br>
 * 
 * @author sgerth
 * 
 */
public class ResultObject implements Serializable {

	private static final long serialVersionUID = 1L;

	// holds a generic List
	private List<?> list;

	// holds the total count of the list
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
