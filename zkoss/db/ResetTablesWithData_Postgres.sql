/* 
 * Reset of the data in our ONLINE APPLICATION after to much modifications through tests.
 */


/* Delete all basic stuff, but NOT the guestbook and login log data */
DELETE  FROM filiale;
DELETE  FROM kunde;
DELETE  FROM artikel;
DELETE  FROM auftrag;
DELETE  FROM auftragposition;
DELETE  FROM branche;


DELETE  FROM sec_userrole;
DELETE  FROM sec_user;
DELETE  FROM sec_rolegroup;
DELETE  FROM sec_role;
DELETE  FROM sec_groupright;
DELETE  FROM sec_group;
DELETE  FROM sec_right;
DELETE  FROM sys_ip4country;

DELETE FROM youtube_link;

DELETE  FROM  hibernate_entity_statistics cascade;
DELETE  FROM  hibernate_statistics cascade;

/* 
 * commit;
 */ 

/* not deleted tables for holding the history of the sample data access */
/*
 ipc_ip2country=can be updated from CSV over webservice.
 DROP TABLE IF EXISTS sys_countrycode;
 DROP TABLE IF EXISTS ipc_ip2country cascade;
 
 DROP TABLE IF EXISTS sec_loginlog cascade;
 DROP TABLE IF EXISTS log_ip2country cascade;
 DROP TABLE IF EXISTS guestbook cascade;
 DROP TABLE IF EXISTS calendar_event cascade;
 */

    
      
/******************** TEST DATA ********************/


/******************** Filiale Daten ********************/
INSERT INTO filiale (FIL_ID, FIL_NR, FIL_BEZEICHNUNG,FIL_NAME1,FIL_NAME2,FIL_ORT,VERSION) values
(1,'0001','Filiale Muenchen','Hoermann Gmbh','Personaldienstleistungen','Muenchen',0),
(2,'0002','Filiale Berlin',  'Hoermann Gmbh','Personaldienstleistungen','Berlin',0);

/******************** Security: USERS ********************/  
INSERT INTO sec_user (USR_ID, USR_LOGINNAME, USR_PASSWORD, USR_LASTNAME, USR_FIRSTNAME, USR_EMAIL, USR_LOCALE, USR_ENABLED, USR_ACCOUNTNONEXPIRED, USR_CREDENTIALSNONEXPIRED, USR_ACCOUNTNONLOCKED, USR_TOKEN,  VERSION) values 
(10, 'guest', 'guest', 'guestFirstname', 'guestlastname', 'guest@web.de', NULL, true, true, true, true, null, 0),
(11, 'admin', 'admin', 'Visor', 'Super', 'admin@web.de', NULL, true, true, true, true, null, 0),
(12, 'user1', 'user1', 'Kingsley', 'Ben', 'B.Kingsley@web.de', NULL, true, true, true, true, null, 0),
(13, 'headoffice', 'headoffice', 'Willis', 'Bruce', 'B.Willis@web.de', NULL, true, true, true, true, null, 0),
(14, 'user2', 'user2', 'Kingdom', 'Marta', 'M.Kingdom@web.de', NULL, true, true, true, true, null, 0);

/******************** Security: ROLES ********************/  
INSERT INTO sec_role (ROL_ID, ROL_SHORTDESCRIPTION, ROL_LONGDESCRIPTION, VERSION) values
(10000,'ROLE_ADMIN','Administrator Role', 0),
(10002,'ROLE_OFFICE_ALL_RIGHTS','Office User role with all rights granted.', 0),
(10003,'ROLE_GUEST','Guest Role', 0),
(10004,'ROLE_OFFICE_ONLY_VIEW','Office user with rights that granted only view of data.', 0),
(10005,'ROLE_HEADOFFICE_USER','Headoffice User Role', 0);


/******************** Security: USER-ROLES ********************/  
/* Guest account authorities */
INSERT INTO sec_userrole (URR_ID, USR_ID, ROL_ID, VERSION) values
(11000, 10, 10003, 0),
/* User1 Usr-Id: 12 */
(11001, 12, 10002, 0),
/* Headoffice user account authorities */
(11010, 13, 10005, 0);

/* Admin Usr-ID: 11 */
INSERT INTO sec_userrole (URR_ID, USR_ID, ROL_ID, VERSION) values
(11003, 11, 10000, 0),
(11005, 11, 10002, 0),
(11006, 11, 10003, 0),
(11008, 11, 10004, 0),
(11009, 11, 10005, 0),
/* User2 Usr-ID: 14 */
(11007, 14, 10004, 0);

/******************** Security: SEC_GROUPS ********************/  
INSERT INTO sec_group (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) values
(13001, 'Headoffice Supervisor Group', 'kjhf ff hgfd', 0),
(13002, 'Admin Group - user accounts', 'create/modify user accounts', 0),
(13003, 'Guest Group', 'Minimal Rights for the guests', 0),
(13004, 'Admin Group - user rights', 'edit/modify user rights', 0),
/* Customers */
(13000, 'Customers_View', 'Allow to  view customers data', 0),
(13008, 'Customers_New', 'Allow create new customers', 0),
(13006, 'Customers_Edit', 'Allow editing of customers', 0),
(13007, 'Customers_Delete', 'Allow deleting of customers', 0),
/* Orders */
(13010, 'Orders_View', 'Allow to view orders data', 0),
(13011, 'Orders_New', 'Allow create new orders', 0),
(13012, 'Orders_Edit', 'Allow editing of orders', 0),
(13013, 'Orders_Delete', 'Allow deleting of orders', 0),
/* Branches */
(13020, 'Branch_View', 'Allow to view branches data', 0),
(13021, 'Branch_New', 'Allow create new branches', 0),
(13022, 'Branch_Edit', 'Allow editing of branches', 0),
(13023, 'Branch_Delete', 'Allow deleting of branches', 0),
/* Articles */
(13030, 'Articles_View', 'Allow to view articles data', 0),
(13031, 'Articles_New', 'Allow create new articles', 0),
(13032, 'Articles_Edit', 'Allow editing of articles', 0),
(13033, 'Articles_Delete', 'Allow deleting of articles', 0),
/* Offices */
(13040, 'Offices_View', 'Allow to view offices data', 0),
(13041, 'Offices_New', 'Allow create new offices', 0),
(13042, 'Offices_Edit', 'Allow editing of offices', 0),
(13043, 'Offices_Delete', 'Allow deleting of offices', 0),
/* Users */
(13060, 'User_View_UsersOnly', 'Allow to view own user data.', 0),
(13061, 'User_Edit_UsersOnly', 'Allow to edit own user data.', 0),
(13062, 'Users_View', 'Allow to view all users data.', 0),
(13063, 'Users_New', 'Allow create new users', 0),
(13064, 'Users_Edit', 'Allow editing of users', 0),
(13065, 'Users_Delete', 'Allow deleting of users', 0),
(13066, 'Users_Search', 'Allow searching of users', 0),
/* secGroup */
(13070, 'Security_Groups', 'Allow to view the securityGroups Dialog', 0),
/* secRole */
(13071, 'Security_Roles', 'Allow to view the securityRoles Dialog', 0),
/* secRight */
(13072, 'Security_Rights', 'Allow to view the securityRights Dialog', 0);


/******************** Security: SEC_ROLE-GROUPS ********************/  
/* ROLE_OFFICE_ALL_RIGHTS */
/* Group: Customers_View */
INSERT INTO sec_rolegroup (RLG_ID, GRP_ID, ROL_ID, VERSION) values 
(12000, 13000, 10002, 0),
/* Group: Customers_New */
(12001, 13008, 10002, 0),
/*  Group: Customers_Edit */
(12002, 13006, 10002, 0),
/*  Group: Customers_Delete */
(12003, 13007, 10002, 0),
/*  Group: Orders_View */
(12004, 13010, 10002, 0),
/*  Group: Orders_New */
(12005, 13011, 10002, 0),
/*  Group: Orders_Edit */
(12006, 13012, 10002, 0),
/*  Group: Orders_Delete */
(12007, 13013, 10002, 0),
/*  Group: User_View_UsersOnly */
(12008, 13060, 10002, 0),
/*  Group: User_Edit_UsersOnly */
(12009, 13061, 10002, 0),
/* ROLE_OFFICE_ONLY_VIEW */
/* Group: Customers_View */
(12010, 13000, 10004, 0),
/*  Group: Orders_View */
(12011, 13010, 10004, 0),
/*  Group: User_View_UsersOnly */
(12012, 13060, 10004, 0),
/* ROLE_GUEST */
(12020, 13003, 10003, 0),
/* ROLE_ADMIN */
(12050, 13002, 10000, 0),
(12051, 13000, 10000, 0),
(12052, 13001, 10000, 0),
(12053, 13003, 10000, 0),
(12054, 13004, 10000, 0),
(12055, 13006, 10000, 0),
(12056, 13007, 10000, 0),
(12057, 13008, 10000, 0),
(12058, 13010, 10000, 0),
(12059, 13011, 10000, 0),
(12060, 13012, 10000, 0),
(12061, 13013, 10000, 0),
(12062, 13020, 10000, 0),
(12063, 13021, 10000, 0),
(12064, 13022, 10000, 0),
(12065, 13023, 10000, 0),
(12066, 13030, 10000, 0),
(12067, 13031, 10000, 0),
(12068, 13032, 10000, 0),
(12069, 13033, 10000, 0),
(12070, 13040, 10000, 0),
(12071, 13041, 10000, 0),
(12072, 13042, 10000, 0),
(12073, 13043, 10000, 0),
/* Group: Users_View */
(12074, 13062, 10000, 0),
/* Group: Users_New */
(12075, 13063, 10000, 0),
/* Group: Users_Edit */
(12076, 13064, 10000, 0),
/* Group: Users_Delete */
(12077, 13065, 10000, 0),
/* Group: Users_Search */
(12078, 13066, 10000, 0),

/* ROLE_HEADOFFICE_USER */
/* Group: Branch_View */
(12100, 13020, 10005, 0),
/* Group: Branch_New */
(12101, 13021, 10005, 0),
/* Group: Branch_Edit */
(12102, 13022, 10005, 0),
/* Group: Branch_Delete */
(12103, 13023, 10005, 0),
/* Group: Articles_View */
(12104, 13030, 10005, 0),
/* Group: Articles_New */
(12105, 13031, 10005, 0),
/* Group: Articles_Edit */
(12106, 13032, 10005, 0),
/* Group: Articles_Delete */
(12107, 13033, 10005, 0),
/* Group: Offices_View */
(12108, 13040, 10005, 0),
/* Group: Offices_New */
(12109, 13041, 10005, 0),
/* Group: Offices_Edit */
(12110, 13042, 10005, 0),
/* Group: Offices_Delete */
(12111, 13043, 10005, 0),
/*  Group: User_View_UsersOnly */
(12115, 13060, 10005, 0),
/*  Group: User_Edit_UsersOnly */
(12116, 13061, 10005, 0),
     
/* ROLE_ADMIN */
/* Group: Security_Groups */
(12117, 13070, 10000, 0),
/* Group: Security_Roles */
(12118, 13071, 10000, 0),
/* Group: Security_Rights */
(12119, 13072, 10000, 0);


/******************** Security: SEC_RIGHTS ********************/  
INSERT INTO sec_right (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) values
(15000, 1, 'menuCat_OfficeData', 0),
(15001, 2, 'menuItem_OfficeData_Customers', 0),
(15002, 2, 'menuItem_OfficeData_Orders', 0),
(15003, 1, 'menuCat_MainData', 0),
(15004, 2, 'menuItem_MainData_ArticleItems', 0),
(15005, 2, 'menuItem_MainData_Branch', 0),
(15006, 2, 'menuItem_MainData_Office', 0),
(15007, 1, 'menuCat_Administration', 0),
(15008, 2, 'menuItem_Administration_Users', 0),
(15009, 2, 'menuItem_Administration_UserRoles', 0),
(15010, 2, 'menuItem_Administration_Roles', 0),
(15011, 2, 'menuItem_Administration_RoleGroups', 0),
(15012, 2, 'menuItem_Administration_Groups', 0),
(15013, 2, 'menuItem_Administration_GroupRights', 0),
(15014, 2, 'menuItem_Administration_Rights', 0),
(15015, 1, 'menuCat_UserRights', 0),
(15016, 2, 'menuItem_Administration_LoginsLog', 0),
(15017, 2, 'menuItem_Administration_HibernateStats', 0),
(15018, 2, 'menu_Item_Calendar', 0),

/* Pages = Type(0) */
/* --> Page Customer */
(15100, 0, 'window_customerList', 0),
(15101, 0, 'window_customerDialog', 0),
/* --> Page Orders */
(15102, 0, 'orderListWindow', 0),
(15103, 0, 'orderDialogWindow', 0),
/* --> Page Articles */
(15104, 0, 'windowArticlesList', 0),
(15105, 0, 'window_ArticlesDialog', 0),
/* --> Page Branches */
(15106, 0, 'window_BranchesList', 0),
(15107, 0, 'window_BranchesDialog', 0),
/* --> Page Offices */
(15108, 0, 'window_OfficeList', 0),
(15109, 0, 'window_OfficeDialog', 0),
/* --> Page Admin - Users */
(15110, 0, 'page_Admin_UserList', 0),
(15111, 0, 'page_Admin_UserDialog', 0),
/* --> Page Admin - UserRoles */
(15112, 0, 'page_Security_UserRolesList', 0),
(15113, 0, 'page_Security_RolesList', 0),
/* --> Page Admin - Roles */
(15114, 0, 'page_Security_RolesDialog', 0),
/* --> Page Admin - RoleGroups */
(15115, 0, 'page_Security_RoleGroupsList', 0),
/* --> Page Admin - Groups */
(15116, 0, 'page_Security_GroupsList', 0),
(15117, 0, 'page_Security_GroupsDialog', 0),
/* --> Page Admin - GroupRights */
(15118, 0, 'page_Security_GroupRightsList', 0),
/* --> Page Admin - Rights */
(15119, 0, 'page_Security_RightsList', 0),
(15120, 0, 'page_Security_RightsDialog', 0),
/* --> Page Login Log */
(15121, 0, 'page_Admin_LoginLogList', 0),
/* --> Nachtrag Page Orders */
(15122, 0, 'orderPositionDialogWindow', 0),

/* Tabs = Type(5) */
(15200, 5, 'tab_CustomerDialog_Address', 0),
(15201, 5, 'tab_CustomerDialog_Chart', 0),
(15202, 5, 'tab_CustomerDialog_Orders', 0),
(15203, 5, 'tab_CustomerDialog_Memos', 0),

/* Components = Type(6) */
/* --> CustomerList BUTTON */
(15300, 6, 'button_CustomerList_btnHelp', 0),
(15301, 6, 'button_CustomerList_NewCustomer', 0),
(15302, 6, 'button_CustomerList_CustomerFindDialog', 0),
(15303, 6, 'button_CustomerList_PrintList', 0),
/* --> CustomerDialog BUTTON */
(15305, 6, 'button_CustomerDialog_btnHelp', 0),
(15306, 6, 'button_CustomerDialog_btnNew', 0),
(15307, 6, 'button_CustomerDialog_btnEdit', 0),
(15308, 6, 'button_CustomerDialog_btnDelete', 0),
(15309, 6, 'button_CustomerDialog_btnSave', 0),
(15310, 6, 'button_CustomerDialog_btnClose', 0),
/* --> OrderList BUTTON */
(15400, 6, 'button_OrderList_btnHelp', 0),
(15401, 6, 'button_OrderList_NewOrder', 0),
/* --> OrderDialog BUTTON */
(15410, 6, 'button_OrderDialog_btnHelp', 0),
(15411, 6, 'button_OrderDialog_btnNew', 0),
(15412, 6, 'button_OrderDialog_btnEdit', 0),
(15413, 6, 'button_OrderDialog_btnDelete', 0),
(15414, 6, 'button_OrderDialog_btnSave', 0),
(15415, 6, 'button_OrderDialog_btnClose', 0),
(15416, 6, 'button_OrderDialog_PrintOrder', 0),
(15417, 6, 'button_OrderDialog_NewOrderPosition', 0),
/* --> OrderPositionDialog BUTTON */
(15430, 6, 'button_OrderPositionDialog_btnHelp', 0),
(15431, 6, 'button_OrderPositionDialog_PrintOrderPositions', 0),
(15432, 6, 'button_OrderPositionDialog_btnNew', 0),
(15433, 6, 'button_OrderPositionDialog_btnEdit', 0),
(15434, 6, 'button_OrderPositionDialog_btnDelete', 0),
(15435, 6, 'button_OrderPositionDialog_btnSave', 0),
(15436, 6, 'button_OrderPositionDialog_btnClose', 0),
/* USERS */
/* --> userListWindow */
(15470, 0, 'userListWindow', 0),
/* --> userListWindow BUTTONS*/
(15471, 6, 'button_UserList_btnHelp', 0),
(15472, 6, 'button_UserList_NewUser', 0),
(15473, 6, 'button_UserList_PrintUserList', 0),
(15474, 6, 'button_UserList_SearchLoginname', 0),
(15475, 6, 'button_UserList_SearchLastname', 0),
(15476, 6, 'button_UserList_SearchEmail', 0),
(15477, 6, 'checkbox_UserList_ShowAll', 0),
/* --> Mehode onDoubleClick Listbox */
(15778, 3, 'UserList_listBoxUser.onDoubleClick', 0),
/* --> userDialogWindow */
(15480, 0, 'userDialogWindow', 0),
/* --> userDialogWindow BUTTONS*/
(15481, 6, 'button_UserDialog_btnHelp', 0),
(15482, 6, 'button_UserDialog_btnNew', 0),
(15483, 6, 'button_UserDialog_btnEdit', 0),
(15484, 6, 'button_UserDialog_btnDelete', 0),
(15485, 6, 'button_UserDialog_btnSave', 0),
(15486, 6, 'button_UserDialog_btnClose', 0),
/* --> userDialogWindow Special Admin Panels */
(15487, 6, 'panel_UserDialog_Status', 0),
(15488, 6, 'panel_UserDialog_SecurityToken', 0),
/* --> userListWindow Search panel */
(15489, 6, 'hbox_UserList_SearchUsers', 0),
/* Tab Details */
(15490, 6, 'tab_UserDialog_Details', 0),
(15491, 3, 'data_SeeAllUserData', 0),
/* BRANCHES */
/* branchListWindow Buttons*/
/* --> button_BranchList_btnHelp */
(15502, 0, 'button_BranchMain_PrintBranches', 0),
(15503, 0, 'button_BranchMain_Search_BranchName', 0),
/* branchDialogWindow BUTTONS */
(15510, 6, 'button_BranchMain_btnHelp', 0),
(15511, 6, 'button_BranchMain_btnNew', 0),
(15512, 6, 'button_BranchMain_btnEdit', 0),
(15513, 6, 'button_BranchMain_btnDelete', 0),
(15514, 6, 'button_BranchMain_btnSave', 0),
(15515, 6, 'button_BranchMain_btnClose', 0),
/* ARTICLES */
/* window_ArticlesList Buttons*/
(15530, 6, 'button_ArticlesList_btnHelp', 0),
(15531, 6, 'button_ArticleList_NewArticle', 0),
(15532, 6, 'button_ArticleList_PrintList', 0),
(15533, 6, 'button_ArticleList_SearchArticleID', 0),
(15534, 6, 'button_ArticleList_SearchName', 0),
/* window_ArticlesDialog Buttons*/
(15540, 6, 'button_ArticlesDialog_btnHelp', 0),
(15541, 6, 'button_ArticlesDialog_btnNew', 0),
(15542, 6, 'button_ArticlesDialog_btnEdit', 0),
(15543, 6, 'button_ArticlesDialog_btnDelete', 0),
(15544, 6, 'button_ArticlesDialog_btnSave', 0),
(15545, 6, 'button_ArticlesDialog_btnClose', 0),
/* OFFICES */
/* window_OfficeList Buttons*/
/* --> button_BranchList_btnHelp */
(15602, 6, 'button_OfficeList_PrintList', 0),
(15603, 6, 'button_OfficeList_SearchNo', 0),
(15604, 6, 'button_OfficeList_SearchName', 0),
(15605, 6, 'button_OfficeList_SearchCity', 0),
/* window_OfficeDialog BUTTONS */
(15611, 6, 'button_OfficeMain_btnHelp', 0),
(15612, 6, 'button_OfficeMain_btnNew', 0),
(15613, 6, 'button_OfficeMain_btnEdit', 0),
(15614, 6, 'button_OfficeMain_btnDelete', 0),
(15615, 6, 'button_OfficeMain_btnSave', 0),
(15616, 6, 'button_OfficeMain_btnClose', 0),

/* Method/Event = Type(3) */
/* --> CustomerList BUTTON */
(15700, 3, 'CustomerList_listBoxCustomer.onDoubleClick', 0),
/* --> secRoleDialogWindow */
(15750, 0, 'secRoleDialogWindow', 0),
/* --> secRoleDialogWindow BUTTONS*/
(15751, 6, 'button_SecRoleDialog_btnHelp', 0),
(15752, 6, 'button_SecRoleDialog_btnNew', 0),
(15753, 6, 'button_SecRoleDialog_btnEdit', 0),
(15754, 6, 'button_SecRoleDialog_btnDelete', 0),
(15755, 6, 'button_SecRoleDialog_btnSave', 0),
(15756, 6, 'button_SecRoleDialog_btnClose', 0),
/* --> secGroupDialogWindow */
(15760, 0, 'secGroupDialogWindow', 0),
/* --> secGroupDialogWindow BUTTONS*/
(15761, 6, 'button_SecGroupDialog_btnHelp', 0),
(15762, 6, 'button_SecGroupDialog_btnNew', 0),
(15763, 6, 'button_SecGroupDialog_btnEdit', 0),
(15764, 6, 'button_SecGroupDialog_btnDelete', 0),
(15765, 6, 'button_SecGroupDialog_btnSave', 0),
(15766, 6, 'button_SecGroupDialog_btnClose', 0),
/* --> secRightDialogWindow */
(15770, 0, 'secRightDialogWindow', 0),
/* --> secRightDialogWindow BUTTONS*/
(15771, 6, 'button_SecRightDialog_btnHelp', 0),
(15772, 6, 'button_SecRightDialog_btnNew', 0),
(15773, 6, 'button_SecRightDialog_btnEdit', 0),
(15774, 6, 'button_SecRightDialog_btnDelete', 0),
(15775, 6, 'button_SecRightDialog_btnSave', 0),
(15776, 6, 'button_SecRightDialog_btnClose', 0);

/******************** Security: SEC_GROUP-RIGHTS ********************/  
/* Headoffice Supervisor Group*/
INSERT INTO sec_groupright (GRI_ID, GRP_ID, RIG_ID, VERSION) values 
(14003, 13001, 15003, 0),
(14004, 13001, 15004, 0),
(14005, 13001, 15005, 0),
(14006, 13001, 15006, 0),
/* Administration Group*/
(14007, 13002, 15007, 0),
(14008, 13002, 15008, 0),
(14009, 13002, 15009, 0),
(14010, 13002, 15010, 0),
(14011, 13002, 15011, 0),
(14012, 13002, 15012, 0),
(14013, 13002, 15013, 0),
(14014, 13002, 15014, 0),
(14015, 13002, 15015, 0),
(14016, 13002, 15016, 0),
(14017, 13002, 15017, 0),

/* New */
/* Group: Customers_View */
/* Right: menuCat_OfficeData */
(14200, 13000, 15000, 0),
/* Right: menuItem_OfficeData_Customers */
(14201, 13000, 15001, 0),
/* Right: customerListWindow */
(14202, 13000, 15100, 0),
/* Right: button_CustomerList_Help */
(14203, 13000, 15305, 0),
/* Right: CustomerList_listBoxCustomer.onDoubleClick */
(14204, 13000, 15700, 0),

/* Right: customerDialogWindow */
(14205, 13000, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14206, 13000, 15310, 0),
/* Right: button_CustomerList_Help */
(14207, 13000, 15300, 0),
/* Right: button_CustomerList_CustomerFindDialog */
(14208, 13000, 15302, 0),
/* Right: button_CustomerList_PrintList */
(14209, 13000, 15303, 0),

/* Tab tab_CustomerDialog_Address */
(14210, 13000, 15200, 0),
/* Tab tab_CustomerDialog_Addition */
(14211, 13000, 15201, 0),
/* Tab tab_CustomerDialog_Orders */
(14212, 13000, 15202, 0),
/* Tab tab_CustomerDialog_Memos */
(14213, 13000, 15203, 0),

/* Group: Customers_New */
/* Right: customerListWindow */
(14230, 13008, 15100, 0),
/* Right: button_CustomerList_NewCustomer */
(14231, 13008, 15301, 0),
/* Right: customerDialogWindow */
(14232, 13008, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14233, 13008, 15310, 0),
/* Right: button_CustomerDialog_btnNew */
(14234, 13008, 15306, 0),
/* Right: button_CustomerDialog_btnEdit */
(14235, 13008, 15307, 0),
/* Right: button_CustomerDialog_btnSave */
(14236, 13008, 15309, 0),

/* Group: Customers_Edit */
/* Right: customerListWindow */
(14240, 13006, 15100, 0),

/* Right: customerDialogWindow */
(14241, 13006, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14242, 13006, 15310, 0),
/* Right: button_CustomerDialog_btnEdit */
(14243, 13006, 15307, 0),
/* Right: button_CustomerDialog_btnSave */
(14244, 13006, 15309, 0),

/* Group: Customers_Delete */
/* Right: customerListWindow */
(14250, 13007, 15100, 0),
/* Right: customerDialogWindow */
(14251, 13007, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14252, 13007, 15310, 0),
/* Right: button_CustomerDialog_btnDelete */
(14253, 13007, 15308, 0),

/*----------------------------------------------------------*/
/* Group: Orders_View */
/* Right: menuCat_OfficeData */
(14300, 13010, 15000, 0),
/* Right: menuItem_OfficeData_Orders */
(14301, 13010, 15002, 0),
/* Right: orderListWindow */
(14302, 13010, 15102, 0),
/* Right: button_OrderList_btnHelp */
(14303, 13010, 15400, 0),
/* Right: CustomerList_listBoxCustomer.onDoubleClick */
(14304, 13010, 15700, 0),
/* Right: orderDialogWindow */
(14305, 13010, 15103, 0),
/* Right: button_OrderDialog_btnClose */
(14306, 13010, 15415, 0),
/* Right: button_OrderDialog_btnHelp */
(14307, 13010, 15410, 0),
/* Right: button_OrderDialog_PrintOrder */
(14308, 13010, 15416, 0),
/* Right: orderPositionDialogWindow */
(14309, 13010, 15122, 0),
/* Right: button_OrderPositionDialog_btnClose */
(14310, 13010, 15436, 0),
/* Right: button_OrderPositionDialog_btnHelp */
(14311, 13010, 15430, 0),
/* Right: button_OrderPositionDialog_PrintOrder */
(14312, 13010, 15431, 0),

/* Group: Orders_New */
/* Right: button_OrderList_NewOrder */
(14320, 13011, 15401, 0),
/* Right: button_OrderDialog_btnClose */
(14321, 13011, 15415, 0),
/* Right: button_OrderDialog_btnNew */
(14322, 13011, 15411, 0),
/* Right: button_OrderDialog_btnEdit */
(14323, 13011, 15412, 0),
/* Right: button_CustomerDialog_btnSave */
(14324, 13011, 15414, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14325, 13011, 15417, 0),
/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14326, 13011, 15436, 0),
/* Right: button_OrderPositionDialog_btnNew */
(14327, 13011, 15432, 0),
/* Right: button_OrderPositionDialog_btnEdit */
(14328, 13011, 15433, 0),
/* Right: button_OrderPositionDialog_btnSave */
(14329, 13011, 15435, 0),

/* Group: Orders_Edit */
/* Right: button_OrderDialog_btnClose */
(14330, 13012, 15415, 0),
/* Right: button_OrderDialog_btnEdit */
(14331, 13012, 15412, 0),
/* Right: button_CustomerDialog_btnSave */
(14332, 13012, 15414, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14333, 13012, 15417, 0),

/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14334, 13012, 15436, 0),
/* Right: button_OrderPositionDialog_btnEdit */
(14335, 13012, 15433, 0),
/* Right: button_OrderPositionDialog_btnSave */
(14336, 13012, 15435, 0),

/* Group: Orders_Delete */
/* Right: button_OrderDialog_btnClose */
(14340, 13013, 15415, 0),
/* Right: button_OrderDialog_btnDelete */
(14341, 13013, 15413, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14342, 13013, 15417, 0),

/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14343, 13013, 15436, 0),
/* Right: button_OrderPositionDialog_btnDelete */
(14344, 13013, 15434, 0),

/* USERS ----------------- */
/* Group: User_View_UsersOnly */
/* Right: menuCat_Administration */
(14360, 13060, 15007, 0),
/* Right: menuItem_Administration_Users */
(14361, 13060, 15008, 0),
/* Right: userListWindow */
(14362, 13060, 15470, 0),
/* Right: button_UserList_btnHelp */
(14363, 13060, 15471, 0),
/* Right: UserList_listBoxUser.onDoubleClick */
(14364, 13060, 15778, 0),
/* Right: userDialogWindow */
(14365, 13060, 15480, 0),
/* Right: tab_UserDialog_Details */
(14366, 13060, 15490, 0),
/* Right: button_UserDialog_btnHelp */
(14367, 13060, 15481, 0),
/* Right: button_Dialog_btnClose */
(14368, 13060, 15486, 0),

/* Group: User_Edit_UsersOnly */
/* Right: button_UserDialog_btnEdit */
(14370, 13061, 15483, 0),
/* Right: button_Dialog_btnSave */
(14371, 13061, 15485, 0),

/* Group: Users_View */
/* Right: menuCat_Administration */
(14380, 13062, 15007, 0),
/* Right: menuItem_Administration_Users */
(14381, 13062, 15008, 0),
/* Right: userListWindow */
(14382, 13062, 15470, 0),
/* Right: button_UserList_btnHelp */
(14383, 13062, 15471, 0),
/* Right: button_UserList_PrintUserList */
(14384, 13062, 15473, 0),
/* Right: UserList_listBoxUser.onDoubleClick */
(14385, 13062, 15778, 0),
/* Right: userDialogWindow */
(14386, 13062, 15480, 0),
/* Right: tab_UserDialog_Details */
(14387, 13062, 15490, 0),
/* Right: button_UserDialog_btnHelp */
(14388, 13062, 15481, 0),
/* Right: button_UserDialog_btnClose */
(14389, 13062, 15486, 0),
/* Right: panel_UserDialog_Status */
(14390, 13062, 15487, 0),
/* Right: panel_UserDialog_SecurityToken */
(14391, 13062, 15488, 0),
/* Right: data_SeeAllUserData */
(14392, 13062, 15491, 0),

/* Group: Users_New */
/* Right: button_UserList_NewUser */
(14395, 13063, 15472, 0),
/* Right: button_UserDialog_btnNew */
(14396, 13063, 15482, 0),
/* Right: button_UserDialog_btnEdit */
(14397, 13063, 15483, 0),
/* Right: button_UserDialog_btnSave */
(14398, 13063, 15485, 0),

/* Group: Users_Edit */
/* Right: button_UserDialog_btnEdit */
(14400, 13064, 15483, 0),
/* Right: button_UserDialog_btnSave */
(14401, 13064, 15485, 0),

/* Group: Users_Delete */
/* Right: button_UserDialog_btnDelete */
(14410, 13065, 15484, 0),

/* Group: Users_Search */
/* Right: hbox_UserList_SearchUsers */
(14420, 13066, 15489, 0),

/* B r a n c h e s */
/* Group: Branch_View */
/* Right: menuCat_MainData */
(14500, 13020, 15003, 0),
/* Right: menuItem_MainData_Branch */
(14501, 13020, 15005, 0),
/* Right: page_BranchesList */
(14502, 13020, 15106, 0),
/* Right: button_BranchList_PrintBranches */
(14504, 13020, 15502, 0),
/* Right: button_BranchList_Search_BranchName */
(14505, 13020, 15503, 0),
/* Right: page_BranchesDialog */
(14507, 13020, 15107, 0),
/* Right: button_BranchDialog_btnHelp */
(14508, 13020, 15510, 0),
/* Right: button_BranchDialog_btnClose */
(14509, 13020, 15515, 0),

/* Group: Branch_New */
/* Right: button_BranchDialog_btnNew */
(14511, 13021, 15511, 0),
/* Right: button_BranchDialog_btnSave */
(14512, 13021, 15514, 0),

/* Group: Branch_Edit */
/* Right: button_BranchDialog_btnEdit */
(14520, 13022, 15512, 0),
/* Right: button_BranchDialog_btnSave */
(14521, 13022, 15514, 0),

/* Group: Branch_Delete */
/* Right: button_BranchDialog_btnDelete */
(14530, 13023, 15513, 0),

/* A r t i c l e s */
/* Group: Articles_View */
/* Right: menuCat_MainData */
(14540, 13030, 15003, 0),
/* Right: menuItem_MainData_ArticleItems */
(14541, 13030, 15004, 0),
/* Right: window_ArticlesList */
(14542, 13030, 15104, 0),
/* Right: button_ArticlesList_btnHelp */
(14543, 13030, 15530, 0),
/* Right: button_ArticleList_PrintList */
(14544, 13030, 15532, 0),
/* Right: window_ArticlesDialog */
(14545, 13030, 15105, 0),
/* Right: button_ArticlesDialog_btnHelp */
(14546, 13030, 15540, 0),
/* Right: button_ArticlesDialog_btnClose */
(14547, 13030, 15545, 0),
/* Right: button_ArticleList_SearchArticleID */
(14548, 13030, 15533, 0),
/* Right: button_ArticleList_SearchName */
(14549, 13030, 15534, 0),



/* Group: Articles_New */
/* Right: button_ArticleList_NewArticle */
(14550, 13031, 15531, 0),
/* Right: button_ArticlesDialog_btnNew */
(14551, 13031, 15541, 0),
/* Right: button_ArticlesDialog_btnSave */
(14552, 13031, 15544, 0),

/* Group: Articles_Edit */
/* Right: button_ArticlesDialog_btnEdit */
(14555, 13032, 15542, 0),
/* Right: button_ArticlesDialog_btnSave */
(14556, 13032, 15544, 0),

/* Group: Articles_Delete */
/* Right: button_ArticlesDialog_btnDelete */
(14560, 13033, 15543, 0),

/* O F F I C E S */
/* Group: Offices_View */
/* Right: menuCat_MainData */
(14570, 13040, 15003, 0),
/* Right: menuItem_MainData_Office */
(14571, 13040, 15006, 0),
/* Right: window_OfficesList */
(14572, 13040, 15108, 0),
/* Right: button_OfficeList_PrintList */
(14574, 13040, 15602, 0),
/* Right: button_OfficeList_SearchNo */
(14575, 13040, 15603, 0),
/* Right: button_OfficeList_SearchName */
(14576, 13040, 15604, 0),
/* Right: button_OfficeList_SearchCity */
(14577, 13040, 15605, 0),
/* Right: window_OfficesDialog */
(14578, 13040, 15109, 0),
/* Right: button_OfficeDialog_btnHelp */
(14579, 13040, 15611, 0),
/* Right: button_OfficeDialog_btnClose */
(14580, 13040, 15616, 0),

/* Group: Offices_New */
/* Right: button_OfficeDialog_btnNew */
(14586, 13041, 15612, 0),
/* Right: button_OfficeDialog_btnSave */
(14587, 13041, 15615, 0),

/* Group: Offices_Edit */
/* Right: button_OfficeDialog_btnEdit */
(14590, 13042, 15613, 0),
/* Right: button_OfficeDialog_btnSave */
(14591, 13042, 15615, 0),

/* Group: Offices_Delete */
/* Right: button_OfficeDialog_btnDelete */
(14595, 13043, 15614, 0),

/* Group: Security_Groups */
/* Right: secGroupDialogWindow */
(14600, 13070, 15760, 0),
/* Right: button_SecGroupDialog_btnHelp */
(14601, 13070, 15761, 0),
/* Right: button_SecGroupDialog_btnNew */
(14602, 13070, 15762, 0),
/* Right: button_SecGroupDialog_btnEdit */
(14603, 13070, 15763, 0),
/* Right: button_SecGroupDialog_btnDelete */
(14604, 13070, 15764, 0),
/* Right: buton_SecGroupDialog_btnSave */
(14605, 13070, 15765, 0),
/* Right: button_SecGroupDialog_btnClose */
(14606, 13070, 15766, 0),

/* Group: Security_Roles */
/* Right: secRoleDialogWindow */
(14610, 13071, 15750, 0),
/* Right: button_SecRoleDialog_btnHelp */
(14611, 13071, 15751, 0),
/* Right: button_SecRoleDialog_btnNew */
(14612, 13071, 15752, 0),
/* Right: button_SecRoleDialog_btnEdit */
(14613, 13071, 15753, 0),
/* Right: button_SecRoleDialog_btnDelete */
(14614, 13071, 15754, 0),
/* Right: button_SecRoleDialog_btnSave */
(14615, 13071, 15755, 0),
/* Right: button_SecRoleDialog_btnClose */
(14616, 13071, 15756, 0),

/* Group: Security_Rights */
/* Right: secRightDialogWindow */
(14620, 13072, 15770, 0),
/* Right: button_SecRightDialog_btnHelp */
(14621, 13072, 15771, 0),
/* Right: button_SecRightDialog_btnNew */
(14622, 13072, 15772, 0),
/* Right: button_SecRightDialog_btnEdit */
(14623, 13072, 15773, 0),
/* Right: button_SecRightDialog_btnDelete */
(14624, 13072, 15774, 0),
/* Right: button_SecRightDialog_btnSave */
(14625, 13072, 15775, 0),

/* Right: button_SecRightDialog_btnClose */
(14626, 13072, 15776, 0);

/******************** Branche Daten ********************/
INSERT INTO branche (BRA_ID,BRA_BEZEICHNUNG, VERSION) VALUES
(1000,  'Elektro',0),
(1001,  'Maler',0),
(1002,  'Holzverabeitung',0),
(1003,  'Kaufmaennisch',0),
(1004,  'Versicherung',0),
(1005,  'Mess- und Regeltechnik',0),
(1006,  'Industriemontagen',0),
(1007,  'KFZ',0),
(1008,  'Banken',0),
(1009,  'Grosshandel',0),
(1010,  'Einzelhandel',0),
(1011,  'Werbung',0),
(1012,  'Gastronomie',0),
(1014,  'Pflegedienste',0),
(1015,  'Transportwesen',0),
(1016,  'Metallverarbeitung',0),
(1017,  'Schlosserei',0),
(1018,  'Sanitaer',0),
(1019,  'Heizungsbau',0),
(1020,  'Wasserwirtschaft',0),
(1021,  'Schiffsbau',0),
(1022,  'Laermschutz',0),
(1023,  'Geruestbau',0),
(1024,  'Fassadenbau',0),
(1025,  'Farbherstellung',0),
(1026,  'Kieswerk',0),
(1027,  'Blechnerei',0),
(1028,  'Geruestverleih',0),
(1029,  'Pflasterarbeiten',0),
(1030,  'Trockenbau',0),
(1031,  'Trockenbau- und Sanierung',0),
(1032,  'Huehnerfarm',0),
(1033,  '.',0),
(1034,  'Transportwesen allgemein',0),
(1035,  'Schwertransport',0),
(1036,  'Gefahrgut Transport',0),
(1037,  'Spedition',0);

/******************** Kunden Daten ********************/
INSERT INTO kunde (KUN_ID,KUN_FIL_ID,KUN_BRA_ID, KUN_NR, KUN_MATCHCODE,KUN_NAME1,KUN_NAME2,KUN_ORT,KUN_MAHNSPERRE,VERSION) VALUES 
(20,1,1000, '20', 'MUELLER','--> Mueller Demo-Data','Elektroinstallationen','Freiburg',true,0),
(21,1,1000, '21', 'HUBER','--> Huber Demo-Data','Elektroinstallationen','Oberursel',true,0),
(22,1,1000, '22', 'SIEMENS','Siemens AG','Elektroinstallationen','Braunschweig',false,0),
(23,1,1000, '23', 'AEG','AEG','Elektroinstallationen','Stuttgart',false,0),
(24,1,1019, '24', 'BUDERUS','Buderus Heizungsbau GmbH','Elektroinstallationen','Rastatt',true,0),
(25,1,1000, '25', 'MEILER','Elektro Meiler','Inhaber W. Erler','Karlsruhe',true,0),
(26,1,1000, '26', 'BADER','Bader GmbH','Elektroinstallationen','Berlin',false,0),
(27,1,1000, '27', 'HESKENS','Heskens GmbH','Elektroinstallationen','Badenweiler',false,0),
(28,1,1000, '28', 'MAIER','Maier GmbH','Elektroinstallationen','Friedberg',false,0),
(29,1,1000, '29', 'SCHULZE','Schulze GmbH','Elektroinstallationen','Freiburg',true,0),
(30,1,1004, '30', 'SCHMIERFINK','Schmierfink AG','Versicherungen','Freiburg',true,0),
(31,1,1005, '31', 'SCHULZE','Schulze Ltd.','Anlagenbau','Buxtehude',true,0),
(32,1,1005, '32', 'SCHREINER','Schreiner','SPS-Steuerungsbau','Hamburg',true,0),
(33,1,1004, '33', 'GUTE RUHE','Gute Ruhe AG','Versicherungen','Berlin',true,0),
(34,1,1003, '34', 'FREIBERGER','Freiberger GmbH','In- und Export','Aachen',true,0),
(35,1,1002, '35', 'BERGMANN','Saegewerk Bergmann','Holzverarbeitung','Neustadt',true,0),
(2000,1,1002, '2000', 'SEILER','Saegewerk Seiler','Holzverarbeitung','Freiburg',true,0),
(2001,1,1002, '2001', 'BAUER','Hermann Bauer','Sgerwerk','Titisee-Neustadt',true,0),
(2002,1,1000, '2002', 'BEINHARD','Gebrueder Beinhard GbR','Elektroinstallationen','Muenchen',true,0),
(2003,1,1000, '2003', 'ADLER','Reiner Adler','Elektro Montagen','Dreieich',true,0),
(2004,1,1000, '2004', 'FINK','Hartmut Fink GmbH','Elektro- und Industriemontagen','Stuttgart',true,0),
(2005,1,1000, '2005', 'GERHARD','Huber u. Gerhard GbR','Elektroinstallationen','Stuttgart',true,0),
(2006,1,1004, '2006', 'BELIANZ','BELIANZ','Versicherungs AG','Berlin',true,0),
(2007,1,1004, '2007', 'KINTERTHUR','Kinterthur','Versicherungs AG','Rastatt',true,0),
(2008,1,1004, '2008', 'WOLFRAM','Peter Wolfram','Freier Versicherungsvertreter','Norderstedt',true,0),
(2009,1,1000, '2009', 'HESSING','Mario Hessing GmbH','Elektroinstallationen','Rheinweiler',false,0),
(2010,1,1000, '2010', 'FREIBERG','Werner Freiberg GmbH','Elektroinstallationen','Rheinstetten',false,0);

/*************** Order data / Auftrag Daten ********************/
INSERT INTO auftrag (AUF_ID,AUF_KUN_ID, AUF_NR, AUF_BEZEICHNUNG, VERSION) VALUES
(40, 20, 'AUF-40', 'EGRH Modul1',0),
(41, 20, 'AUF-41', 'OMEGA Modul2',0),
(42, 20, 'AUF-42', 'Keller',0),
(43, 20, 'AUF-43', 'Schlossallee 23',0),
(44, 21, 'AUF-44', 'Renovierung Keller',0),
(45, 21, 'AUF-45', 'Renovierung Hochhaus Ilsestrasse 5',0),
(46, 21, 'AUF-46', 'Verteilerschrank Umbau; Fa. Kloeckner EHG',0);

/*************** Article data / Artikel Daten ********************/
INSERT INTO artikel (ART_ID,ART_KURZBEZEICHNUNG,ART_LANGBEZEICHNUNG,ART_NR,ART_PREIS,VERSION) VALUES 
(3000,'Kabelverschraubung DN 27','Kabelverschraubung Messing verchromt DN 27','KS3000',4.23,0),
(3001,'3-adriges Kabel 1,5mm ','mehradriges Kabel, 3 Adern, Farbe=grau, Querschnitt= 1,5 mm','MK3001',0.12,0),
(3002,'Luesterklemmen 10-fach, bis 1.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER1002',0.78,0),
(3003,'Euro-Platine','Euro-Platine fuer Versuchsaufbauten','PLAT3003',2.34,0),
(3004,'Leuchtmittel 22 Watt','Leuchtmittel 22 Watt Sparlampe weiss, mittlere Lebensdauer ca. 6000 Std.','SPARLA3004',2.84,0),
(3005,'Leuchte einzel bis 100 Watt','Haengeleuchte einzel, Farbe=grau, bis 100 Watt','LEU3005',32.00,0),
(3006,'5-adriges Kabel 1,5mm','mehradriges Kabel, 5 Adern, Farbe=grau, Querschnitt= 1,5 mm','MK3006',0.22, 0),
(3007,'Kabelbinder 12cm','Kabelbinder, Menge: 100 Stk. Lnge: 12 cm, Farbe: weiss','KB3007',1.34, 0),
(3008,'Kabelverschraubung DN 17','Kabelverschraubung Messing verchromt DN 17','KS3008',2.90,0),
(3009,'Kabelverschraubung DN 18','Kabelverschraubung Messing verchromt DN 18','KS3009',3.20,0),
(3010,'Kabelverschraubung DN 22','Kabelverschraubung Messing verchromt DN 22','KS3010',3.40,0),
(3011,'Luesterklemmen 10-fach, bis 2.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER3011',1.68,0),
(3012,'Luesterklemmen 10-fach, bis 4.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER3012',3.10,0);



/***********Order Positions / Auftrag Positionen Daten ********************/
/* Auftrag: 40 */
INSERT INTO auftragposition (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES 
(60,40, 3000, 1, 240, 1.20, 288.00, 0),
(61,40, 3001, 2, 1200, 0.45, 540.00, 0),
(62,40, 3002, 3, 40, 0.20, 8.00, 0),
(63,40, 3003, 4, 15, 4.20, 63.00, 0),
(64,40, 3004, 5, 20, 4.30, 86.00, 0),
(65,40, 3005, 6, 15, 40.00, 600.00, 0);
/* Auftrag: 41 */
INSERT INTO auftragposition (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES
(66,41, 3005, 1, 18, 40.00, 720.00, 0),
(67,41, 3006, 2, 800 , 0.45, 360.00, 0),
(68,41, 3002, 3, 40, 0.20, 8.00, 0),
(69,44, 3007, 1, 3, 2.10, 6.30, 0),
(70,44, 3001, 2, 1200, 0.45, 540.00, 0),
(71,44, 3002, 3, 40, 0.20, 8.00, 0),
(72,44, 3003, 4, 15, 4.20, 63.00, 0),
(73,44, 3004, 5, 20, 4.30, 86.00, 0),
(74,44, 3005, 6, 15, 40.00, 600.00, 0),
(75,45, 3008, 0, 240, 3.20, 768.00, 0),
(76,45, 3009, 1, 444, 3.80, 1687.20, 0),
(77,45, 3010, 2, 240, 4.10, 984.00, 0),
(78,46, 3011, 0, 40, 2.20, 88.00, 0),
(79,46, 3012, 1, 80, 3.60, 288.00, 0),
(80,46, 3002, 2, 90, 1.50, 135.00, 0),
(81,46, 3010, 3, 100, 4.10, 410.00, 0),
(82,46, 3011, 4, 400, 2.20, 880.00, 0),
(83,46, 3006, 5, 60.00, 0.45, 27.00, 0);


/******************** YouTube Music Links ********************/
INSERT INTO youtube_link(ytb_id, ytb_interpret, ytb_title, ytb_url, version) VALUES 
(  1, 'Loquat',                                   'Swing Set Chain',                 'http://www.youtube.com/embed/51G24IVfcaI',   0),
(  2, 'Empire of the Sun',                        'We Are The People',               'http://www.youtube.com/embed/1uPL5twyQOw',   0),
(  3, 'Loquat',                                   'Harder Hit',                      'http://www.youtube.com/watch?v=aoHUb2r8q-g', 0),
(  4, 'THIN LIZZY',                               'Still in Love With You',          'http://www.youtube.com/embed/oHUWXjNU0aM',   0),
(  5, 'THIN LIZZY',                               'Whiskey in the jar (1973)',       'http://www.youtube.com/embed/-M2jSzLBzK4',   0),
(  6, 'Gary Moore with Phil Lynnot',              'Parisienne Walkways (live)',      'http://www.youtube.com/embed/18FgnFVm5k0',   0),
(  7, 'Talking Heads',                            'This must be the place',          'http://www.youtube.com/embed/TTPqPZzH-LA',   0),
(  8, 'John Cale and Brian Eno',                  'Spinning away',                   'http://www.youtube.com/embed/-INeMspNSQ0',   0),
(  9, 'Metric',                                   'Joyride',                         'http://www.youtube.com/embed/F0ZL5YWP5I8',   0),
( 10, 'Medina',                                   'Kun For Mig + Ensome',            'http://www.youtube.com/embed/5Gf004et0SI',   0),
( 11, 'Paris',                                    'Captain Morgan',                  'http://www.youtube.com/embed/o6Eq1bH-qA0',   0),
( 12, 'Al Corley',                                'Square Rooms',                    'http://www.youtube.com/embed/6VgR8XT1w8I',   0),
( 13, 'Al Corley',                                'Cold Dresses',                    'http://www.youtube.com/embed/lY3prM3e4xk',   0),
( 14, 'Victoria S',                               'One in a Million',                'http://www.youtube.com/embed/3YdGVDvrmQ0',   0),
( 15, 'Unknown Cases',                            'MaSimBaBelle (Final Mix)',        'http://www.youtube.com/embed/WhXnEWNEflA',   0),
( 16, 'Heli Deinboek',                            'Oh Suzy du! ',                    'http://www.youtube.com/embed/qBzTINSsj_Q',   0),
( 17, 'Stefanie Heinzmann',                       'Unforgiven ',                     'http://www.youtube.com/embed/AOQG5CyiOkg',   0),
( 18, 'DJ Tiesto feat. Andain',                   'Beautiful Things',                'http://www.youtube.com/embed/5OhaQ2ej63Q',   0),
( 19, 'DJ Tiesto feat. Kane',                     'Rain Down On Me',                 'http://www.youtube.com/embed/wZHCocBkZFo',   0),
( 20, 'DJ Tiesto',                                'Tiesto Power Mix',                'http://www.youtube.com/embed/BZmE3fUKU5U',   0),
( 21, 'Dj Rui Da Silva vs. Dj Tiesto',            'Touch Me',                        'http://www.youtube.com/embed/bjla29Y1I5g',   0),
( 22, 'DJ Tiesto feat. Calvin Harris',            'Century',                         'http://www.youtube.com/embed/XdRk_lbR5fk',   0),
( 23, 'Supergrass',                               'Alright',                         'http://www.youtube.com/embed/h9nY9axjaWo',   0),
( 24, 'Mott the Hoople, David Bowie (Live)',      'All the Young Dudes',             'http://www.youtube.com/embed/N-9F_z0B2TA',   0),
( 25, 'Vanilla Fudge',                            'You Keep Me Hangin On',           'http://www.youtube.com/embed/ifpmXmsecrU',   0),
( 26, 'Procol Harum',                             'A Whiter Shade Of Pale',          'http://www.youtube.com/embed/Mb3iPP-tHdA',   0),
( 27, 'Joe Cocker  (Live Dortmund `92)',          'Nightcalls ',                     'http://www.youtube.com/embed/q8RC01FsDtg',   0),
( 28, 'September',                                'Cry for you',                     'http://www.youtube.com/embed/pxu6iQ28arw',   0),
( 29, 'Depeche Mode (LIVE - Paris 2001)',         'Enjoy The Silence',               'http://www.youtube.com/embed/GnFWXPM7Rno',   0),
( 30, 'DJ Tiesto feat. Cary Brothers',            'Here on earth',                   'http://www.youtube.com/embed/nlh72zBy5Nw',   0),
( 31, 'DJ Tiesto feat. Emily Haines',             'Knock you out',                   'http://www.youtube.com/embed/fM8zIyspZ24',   0),
( 32, 'DJ Tiesto feat. Christian Burns',          'In the dark',                     'http://www.youtube.com/embed/McWPLj6h4Fk',   0),
( 33, 'Armin van Buuren feat. Christian Burns',   'This Light Between Us',           'http://www.youtube.com/embed/T8GHg9v5CeA',   0),
( 34, 'Armin van Buuren feat. Sharon den Adel',   'In and Out of Love',              'http://www.youtube.com/embed/TxvpctgU_s8',   0),
( 35, 'MIA (Live)',                               'Tanz der Moleküle',               'http://www.youtube.com/embed/1uBaPOoQc8Q',   0),
( 36, 'MIA (Live Austria 2008)',                  '100 Prozent',                     'http://www.youtube.com/embed/sbSF4GRv56o',   0),
( 37, 'MIA (Live Austria 2008)',                  'Hungriges Herz',                  'http://www.youtube.com/embed/EQX2g-XSy6I',   0),
( 38, 'Klee',                                     'Erinner Dich',                    'http://www.youtube.com/embed/2y_mmfxT6qI',   0),
( 39, 'Freur (Live 1983)',                        'Doot Doot',                       'http://www.youtube.com/embed/u5QRT1CbMN0',   0),
( 40, 'David Bowie (Live Paris 2002)',            'Ashes to Ashes',                  'http://www.youtube.com/embed/81_Jm-P83FA',   0),
( 41, 'David Bowie (Live Paris 2002)',            'China Girl',                      'http://www.youtube.com/embed/OW7DFXq-cdg',   0),
( 42, 'David Bowie (Live Paris 2002)',            'Heroes',                          'http://www.youtube.com/embed/dTOppbFKWko',   0),
( 43, 'David Bowie (Live Germany 1978)',          'Rebel Rebel',                     'http://www.youtube.com/embed/pUAAU5g_ZEc',   0),
( 44, 'Kate Havnevik',                            'Kaleidoscope',                    'http://www.youtube.com/embed/1C8GobS1WE8',   0),
( 45, 'Joe Satriani (Live San Franciso 2006)',    'Until we say goodbye',            'http://www.youtube.com/embed/hAY3WeZovGk',   0),
( 46, 'Joe Satriani (Live San Franciso 2006)',    'The Crush of Love',               'http://www.youtube.com/embed/dB1WsWi4EEY',   0),
( 47, 'French Kiss (Live aux Francofolies 2009)', 'Le Soupir',                       'http://www.youtube.com/embed/YziA45xILWU',   0),
( 48, 'CHAKA KHAN (Live)',                        'Ain''t nobody',                   'http://www.youtube.com/embed/6eDSIj_iozA',   0),
( 49, 'Madita',                                   'Ceylon',                          'http://www.youtube.com/embed/6qzMxqzS-bg',   0),
( 50, 'The Crystal Method',                       'Starting over. Play it loud :-)', 'http://www.youtube.com/embed/pMGJ3cVvIrY',   0),
( 51, 'Jessie J. feat. B.o.B.',                   'Price Tag',                       'http://www.wat.tv/video/jessie-price-tag-feat-o-3bosb_2zicp_.html',   0),
( 52, 'THE Heavy (Live 2009)',                    'Short Change HeroPrice Tag',      'http://www.youtube.com/embed/Kqxe31ICFZk',   0),
( 53, 'Grace Potter and Joe Satriani (Live)',     'Cortez the Killer',               'http://www.youtube.com/embed/paeNnR33i5Q',   0),
( 54, 'Edie Brickell (Live)',                     'what i am',                       'http://www.youtube.com/embed/uGjh6duUPXc',   0),
( 55, 'Dire Strait & Eric Clapton (Live Wembley 1988)', 'Brothers in arms',          'http://www.youtube.com/embed/kAl5jucOgro',   0),
( 56, 'Buckethead live',                          'Padmasana',                       'http://www.youtube.com/embed/C-2w9b8i7GU',   0),
( 57, 'Brian Ferry - Roxy Music',                 'More than this',                  'http://www.outube.com/embed/UrtRYmJ9u_8',    0),
( 58, 'Brian Ferry - Roxy Music live 1979',       'Dance away',                      'http://www.youtube.com/embed/OA99t2PZbxg',   0),
( 59, 'Brian Ferry - Roxy Music 1985',            'Slave to love',                   'http://www.youtube.com/embed/lkN6l764NT8',   0),
( 60, 'Neil Young  (Live Rust)',                  'Like A Hurricane',                'http://www.youtube.com/embed/7KxiEjPCXA8',   0),
( 61, 'Neil Young  (Live Rust)',                  'Cortez The Killer',               'http://www.youtube.com/embed/6GDIkb5CDUY',   0),
( 62, 'Neil Young  (Live Rust)',                  'Southern man',                    'http://www.youtube.com/embed/kVRxdPWV3RM',   0),
( 63, 'Nina Hagen (1979 - Cha Cha Soundtrack)',   'Herrman''s Door',                 'http://www.youtube.com/embed/tn5RVU8b7IE',   0),
( 64, 'Keith Richards',                           'Hate It When You Leave',          'http://www.youtube.com/embed/lAJUt1C923Q',   0),
( 65, 'Vargo',                                    'Talking one language',            'http://www.youtube.com/embed/fER_7R686Vc',   0),
( 66, 'Vargo',                                    'The Moment',                      'http://www.youtube.com/embed/fYqEn10U6vM',   0),
( 67, 'Vargo',                                    'Infinity',                        'http://www.youtube.com/embed/jJ2Yhqezmho',   0),
( 68, 'Frou Frou (is 100% better on CD)',         'Let go',                          'http://www.youtube.com/embed/MX8UYHGbndY',   0),
( 69, 'Frou Frou live (is 100% better on CD)',    'Breate in',                       'http://www.youtube.com/embed/F4vTjoPFGbA',   0),
( 70, 'Frou Frou live (is 100% better on CD)',    'It''s good to be in love',        'http://www.youtube.com/embed/xRwwlsevNLs',   0),
( 71, 'Frou Frou Cover  (please hear the original CD)', 'Hear me out',               'http://v.youku.com/v_show/id_XMjMxMzUzNzI4.html',   0),
( 72, 'Bullmeister',                              'Girls Beautiful',                 'http://www.youtube.com/embed/7JNSeDKQPQQ',   0),
( 73, 'Lady Gaga vs. Ne-Yo (Preliminary Mix) Mashup 2010',  'Beautiful Monster',     'http://www.youtube.com/embed/zSjclviKmK0',   0),
( 74, 'Ne-yo ft Lady Gaga (Craig Vanity FIXED Mash 2.0)',   'Beautiful Monster',     'http://www.youtube.com/embed/rzQj3NSGXT0',   0),
( 75, 'Andy Timmons)',                            'Cry for you',                     'http://www.youtube.com/embed/WMJD_c-WxPg',   0),
( 76, 'Old stars)',                               'While m guitar gently weeps',     'http://www.youtube.com/embed/ifp_SVrlurY',   0);




/* create the sequences */
ALTER SEQUENCE filiale_seq RESTART WITH 100000;
ALTER SEQUENCE kunde_seq RESTART WITH 100000;
ALTER SEQUENCE artikel_seq RESTART WITH 100000;
ALTER SEQUENCE auftrag_seq RESTART WITH 100000;
ALTER SEQUENCE auftragposition_seq RESTART WITH 100000;
ALTER SEQUENCE branche_seq RESTART WITH 100000;
ALTER SEQUENCE sec_user_seq RESTART WITH 100000;
ALTER SEQUENCE sec_userrole_seq RESTART WITH 100000;
ALTER SEQUENCE sec_role_seq RESTART WITH 100000;
ALTER SEQUENCE sec_rolegroup_seq RESTART WITH 100000;
ALTER SEQUENCE sec_group_seq RESTART WITH 100000;
ALTER SEQUENCE sec_groupright_seq RESTART WITH 100000;
ALTER SEQUENCE sec_right_seq RESTART WITH 100000;
ALTER SEQUENCE sys_countrycode_seq RESTART WITH 300;
ALTER SEQUENCE sys_ip4country_seq RESTART WITH 100000;
ALTER SEQUENCE youtube_link_seq RESTART WITH 100000;
/*ALTER SEQUENCE app_news_seq RESTART WITH 100000;*/
ALTER SEQUENCE hibernate_entity_statistics_seq RESTART WITH 100000;
ALTER SEQUENCE hibernate_statistics_seq RESTART WITH 100000;




