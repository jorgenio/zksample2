/**
 * 
 */
package de.forsthaus.test.backend.service.LoginService;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.forsthaus.backend.bean.DummyBean;
import de.forsthaus.backend.dao.SecLoginlogDAO;
import de.forsthaus.testutils.BasisHibernateTest;

/**
 * @author sge
 * 
 */
public class TestLoginService extends BasisHibernateTest {

	@Autowired
	private SecLoginlogDAO secLoginlogDAO;

	@Test
	public void testABC() {

		List<DummyBean> transfer2Bean = secLoginlogDAO.getTotalCountByCountries();

		System.err.println(transfer2Bean);

	}

	public SecLoginlogDAO getSecLoginlogDAO() {
		return secLoginlogDAO;
	}

	public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
		this.secLoginlogDAO = secLoginlogDAO;
	}
}
