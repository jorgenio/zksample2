package de.forsthaus.backend.service.impl;

import java.util.HashMap;
import java.util.Map;

import de.forsthaus.backend.dao.ArticleDAO;
import de.forsthaus.backend.dao.BrancheDAO;
import de.forsthaus.backend.dao.CustomerDAO;
import de.forsthaus.backend.dao.GuestBookDAO;
import de.forsthaus.backend.dao.IpToCountryDAO;
import de.forsthaus.backend.dao.MyCalendarEventDAO;
import de.forsthaus.backend.dao.OfficeDAO;
import de.forsthaus.backend.dao.OrderDAO;
import de.forsthaus.backend.dao.OrderpositionDAO;
import de.forsthaus.backend.dao.SecGroupDAO;
import de.forsthaus.backend.dao.SecGrouprightDAO;
import de.forsthaus.backend.dao.SecLoginlogDAO;
import de.forsthaus.backend.dao.SecRightDAO;
import de.forsthaus.backend.dao.SecRoleDAO;
import de.forsthaus.backend.dao.SecRolegroupDAO;
import de.forsthaus.backend.dao.SecTypDAO;
import de.forsthaus.backend.dao.SecUserroleDAO;
import de.forsthaus.backend.dao.SysCountryCodeDAO;
import de.forsthaus.backend.dao.UserDAO;
import de.forsthaus.backend.dao.YoutubeLinkDAO;
import de.forsthaus.backend.service.CommonService;

/**
 * Service implementation for methods that depends on <b>all DAO methods</b>.<br>
 * Mainly used for get back the size for every table.
 * 
 * @author bbruhns
 * @author sgerth
 */
public class CommonServiceImpl implements CommonService {

	private ArticleDAO articleDAO;
	private BrancheDAO brancheDAO;
	private CustomerDAO customerDAO;
	private OfficeDAO officeDAO;
	private GuestBookDAO guestBookDAO;
	private IpToCountryDAO ipToCountryDAO;
	private OrderDAO orderDAO;
	private OrderpositionDAO orderpositionDAO;
	private UserDAO userDAO;
	private SecUserroleDAO secUserroleDAO;
	private SecRoleDAO secRoleDAO;
	private SecRolegroupDAO secRolegroupDAO;
	private SecGrouprightDAO secGrouprightDAO;
	private SecGroupDAO secGroupDAO;
	private SecRightDAO secRightDAO;
	private SecTypDAO secTypDAO;
	private SecLoginlogDAO secLoginlogDAO;
	private SysCountryCodeDAO sysCountryCodeDAO;
	private MyCalendarEventDAO calendarEventDAO;
	private YoutubeLinkDAO youtubeLinkDAO;

	public YoutubeLinkDAO getYoutubeLinkDAO() {
		return youtubeLinkDAO;
	}

	public void setYoutubeLinkDAO(YoutubeLinkDAO youtubeLinkDAO) {
		this.youtubeLinkDAO = youtubeLinkDAO;
	}

	public ArticleDAO getArticleDAO() {
		return articleDAO;
	}

	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	public BrancheDAO getBrancheDAO() {
		return brancheDAO;
	}

	public void setBrancheDAO(BrancheDAO brancheDAO) {
		this.brancheDAO = brancheDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public OfficeDAO getOfficeDAO() {
		return officeDAO;
	}

	public void setOfficeDAO(OfficeDAO officeDAO) {
		this.officeDAO = officeDAO;
	}

	public GuestBookDAO getGuestBookDAO() {
		return guestBookDAO;
	}

	public void setGuestBookDAO(GuestBookDAO guestBookDAO) {
		this.guestBookDAO = guestBookDAO;
	}

	public void setIpToCountryDAO(IpToCountryDAO ipToCountryDAO) {
		this.ipToCountryDAO = ipToCountryDAO;
	}

	public IpToCountryDAO getIpToCountryDAO() {
		return ipToCountryDAO;
	}

	public OrderDAO getOrderDAO() {
		return orderDAO;
	}

	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public OrderpositionDAO getOrderpositionDAO() {
		return orderpositionDAO;
	}

	public void setOrderpositionDAO(OrderpositionDAO orderpositionDAO) {
		this.orderpositionDAO = orderpositionDAO;
	}

	public SecTypDAO getSecTypDAO() {
		return secTypDAO;
	}

	public void setSecTypDAO(SecTypDAO secTypDAO) {
		this.secTypDAO = secTypDAO;
	}

	public SecRightDAO getSecRightDAO() {
		return secRightDAO;
	}

	public void setSecRightDAO(SecRightDAO secRightDAO) {
		this.secRightDAO = secRightDAO;
	}

	public SecGroupDAO getSecGroupDAO() {
		return secGroupDAO;
	}

	public void setSecGroupDAO(SecGroupDAO secGroupDAO) {
		this.secGroupDAO = secGroupDAO;
	}

	public SecGrouprightDAO getSecGrouprightDAO() {
		return secGrouprightDAO;
	}

	public void setSecGrouprightDAO(SecGrouprightDAO secGrouprightDAO) {
		this.secGrouprightDAO = secGrouprightDAO;
	}

	public SecRolegroupDAO getSecRolegroupDAO() {
		return secRolegroupDAO;
	}

	public void setSecRolegroupDAO(SecRolegroupDAO secRolegroupDAO) {
		this.secRolegroupDAO = secRolegroupDAO;
	}

	public SecUserroleDAO getSecUserroleDAO() {
		return secUserroleDAO;
	}

	public void setSecUserroleDAO(SecUserroleDAO secUserroleDAO) {
		this.secUserroleDAO = secUserroleDAO;
	}

	public SecRoleDAO getSecRoleDAO() {
		return secRoleDAO;
	}

	public void setSecRoleDAO(SecRoleDAO secRoleDAO) {
		this.secRoleDAO = secRoleDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
		this.secLoginlogDAO = secLoginlogDAO;
	}

	public SecLoginlogDAO getSecLoginlogDAO() {
		return secLoginlogDAO;
	}

	public void setSysCountryCodeDAO(SysCountryCodeDAO sysCountryCodeDAO) {
		this.sysCountryCodeDAO = sysCountryCodeDAO;
	}

	public SysCountryCodeDAO getSysCountryCodeDAO() {
		return sysCountryCodeDAO;
	}

	public void setCalendarEventDAO(MyCalendarEventDAO calendarEventDAO) {
		this.calendarEventDAO = calendarEventDAO;
	}

	public MyCalendarEventDAO getCalendarEventDAO() {
		return calendarEventDAO;
	}

	@Override
	public Map<String, Object> getAllTablesRecordCounts() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("Customer", getCustomerDAO().getCountAllCustomer());
		map.put("Offices", getOfficeDAO().getCountAllOffices());
		map.put("Branch", getBrancheDAO().getCountAllBranch());
		map.put("Article", getArticleDAO().getCountAllArticle());
		map.put("Order", getOrderDAO().getCountAllOrder());
		map.put("Orderposition", getOrderpositionDAO().getCountAllOrderposition());
		map.put("GuestBook", getGuestBookDAO().getCountAllGuestBook());
		map.put("SecGroup", getSecGroupDAO().getCountAllSecGroup());
		map.put("SecGroupright", getSecGrouprightDAO().getCountAllSecGroupright());
		map.put("SecRight", getSecRightDAO().getCountAllSecRights());
		map.put("SecRole", getSecRoleDAO().getCountAllSecRole());
		map.put("SecRolegroup", getSecRolegroupDAO().getCountAllSecRolegroup());
		map.put("SecUser", getUserDAO().getCountAllSecUser());
		map.put("SecUserrole", getSecUserroleDAO().getCountAllSecUserrole());
		map.put("SecLoginlog", getSecLoginlogDAO().getCountAllSecLoginlog());
		map.put("SysCountryCode", getSysCountryCodeDAO().getCountAllSysCountrycode());
		map.put("IpToCountry", getIpToCountryDAO().getCountAllIpToCountry());
		map.put("CalendarEvents", getCalendarEventDAO().getCountAllCalendarEvents());
		map.put("YouTubeLinks", getYoutubeLinkDAO().getCountAllYoutubeLinks());
		return map;
	}
}
