/**
 * 
 */
package de.forsthaus.backend.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.testutils.BasisHibernateTest;

/**
 * @author bbruhns
 * 
 */
public class TestSecLoginlogDAO extends BasisHibernateTest {

	@Autowired
	private SecLoginlogDAO secLoginlogDAO;

	public SecLoginlogDAO getSecLoginlogDAO() {
		return secLoginlogDAO;
	}

	public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
		this.secLoginlogDAO = secLoginlogDAO;
	}

	@Test
	public void testGetTotalCountByCountries() {
		List<DummyBean> list = secLoginlogDAO.getTotalCountByCountries();
		System.out.println("count of countries : " + list.size());
		Assert.assertTrue("Cannot be less than 0", (list.size() > -1));
	}
}
