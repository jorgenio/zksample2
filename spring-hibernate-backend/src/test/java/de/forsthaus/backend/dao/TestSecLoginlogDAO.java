/**
 * 
 */
package de.forsthaus.backend.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.testutils.BasisHibernateTest;

/**
 * @author bbruhns
 *
 */
public class TestSecLoginlogDAO  extends BasisHibernateTest {

	@Autowired
	private SecLoginlogDAO secLoginlogDAO;

	public SecLoginlogDAO getSecLoginlogDAO() {
		return secLoginlogDAO;
	}

	public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
		this.secLoginlogDAO = secLoginlogDAO;
	}
	
	@Test
	public void testGetTotalCountByCountries(){
		List<DummyBean> list = secLoginlogDAO.getTotalCountByCountries();
	}
}
