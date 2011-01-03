/*==============================================================*/
/* DBMS name:      MySQL 5.1                                    */
/* Thanks to AndyX                                              */
/* Created on:     31.08.2010 13:07:33                          */
/*==============================================================*/
/*
drop database if exists test_db;
create database test_db;
*/
use test_db;

drop table if exists auftrag;
drop table if exists kunde;
drop table if exists branche;
drop table if exists artikel;
drop table if exists auftragposition;
drop table if exists filiale;
DROP TABLE IF EXISTS calendar_event;

drop table if exists sec_user;
drop table if exists sec_group;
drop table if exists sec_groupright;
drop table if exists sec_loginlog;
drop table if exists sec_right;
drop table if exists sec_role;
drop table if exists sec_rolegroup;
drop table if exists sec_userrole;

drop table if exists sys_countrycode;
drop table if exists log_ip2country;
drop table if exists ipc_ip2country;
drop table if exists guestbook;
drop table if exists ipc_ip4country;
drop table if exists hibernate_entity_statistics;
drop table if exists hibernate_statistics;

drop table if exists youtube_link;

drop table if exists sequenztable;
CREATE TABLE IF NOT EXISTS sequenztable (
  id int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM;


INSERT INTO sequenztable  VALUES (50000);


DELIMITER $$
DROP FUNCTION IF EXISTS get_nextid;
CREATE FUNCTION get_nextid()
RETURNS INT
BEGIN
DECLARE nextid INT;
update sequenztable set id = id + 1;
select id into nextid from sequenztable;
RETURN nextid;
END$$
DELIMITER ;

DROP VIEW IF EXISTS nextidview;  
CREATE VIEW nextidview 
AS 
SELECT get_nextid() as nextval ;



/*==============================================================*/
/* Table: youtube_link                                          */
/*==============================================================*/
create table youtube_link (
   ytb_id               bigint                 not null,
   ytb_title            varchar(100)           null,
   ytb_url              varchar(300)           not null,
   version              int                    not null default 0,
   primary key (ytb_id)
);



/*==============================================================*/
/* Table: Branche                                               */
/*==============================================================*/
create table Branche
(
   bra_id               bigint not null,
   bra_nr               varchar(20),
   bra_bezeichnung      varchar(30) not null,
   version              int not null default 0,
   primary key (bra_id)
);


/*==============================================================*/
/* Index: idx_bra_id                                            */
/*==============================================================*/
create unique index idx_bra_id on Branche
(
   bra_id
);

/*==============================================================*/
/* Index: idx_bra_bezeichnung                                   */
/*==============================================================*/
create unique index idx_bra_bezeichnung on Branche
(
   bra_bezeichnung
);

/*==============================================================*/
/* Table: artikel                                               */
/*==============================================================*/
create table artikel
(
   art_id               bigint not null,
   art_kurzbezeichnung  varchar(50) not null,
   art_langbezeichnung  text,
   art_nr               varchar(20) not null,
   art_preis            numeric(12,2) not null default 0.00,
   version              int not null default 0,
   primary key (art_id)
);

/*==============================================================*/
/* Index: idx_art_id                                            */
/*==============================================================*/
create unique index idx_art_id on artikel
(
   art_id
);

/*==============================================================*/
/* Index: idx_art_nr                                            */
/*==============================================================*/
create unique index idx_art_nr on artikel
(
   art_nr
);

/*==============================================================*/
/* Index: idx_art_bezeichnung                                   */
/*==============================================================*/
create index idx_art_bezeichnung on artikel
(
   art_kurzbezeichnung
);

/*==============================================================*/
/* Table: auftrag                                               */
/*==============================================================*/
create table auftrag
(
   auf_id               bigint not null,
   auf_kun_id           bigint not null,
   auf_nr               varchar(20) not null,
   auf_bezeichnung      varchar(50),
   version              int not null default 0,
   primary key (auf_id)
);

/*==============================================================*/
/* Index: ix_auf_id                                             */
/*==============================================================*/
create unique index ix_auf_id on auftrag
(
   auf_id
);

/*==============================================================*/
/* Index: ix_auf_kun_id                                         */
/*==============================================================*/
create index ix_auf_kun_id on auftrag
(
   auf_kun_id
);

/*==============================================================*/
/* Index: ix_auf_nr                                             */
/*==============================================================*/
create unique index ix_auf_nr on auftrag
(
   auf_nr
);

/*==============================================================*/
/* Table: auftragposition                                       */
/*==============================================================*/
create table auftragposition
(
   aup_id               bigint not null,
   aup_auf_id           bigint not null,
   art_id               bigint,
   aup_position         int,
   aup_menge            numeric(12,2),
   aup_einzelwert       numeric(12,2),
   aup_gesamtwert       numeric(12,2),
   version              int not null default 0,
   primary key (aup_id)
);

/*==============================================================*/
/* Index: ix_aup_auf_id                                         */
/*==============================================================*/
create index ix_aup_auf_id on auftragposition
(
   aup_auf_id
);

/*==============================================================*/
/* Index: ix_aup_id                                             */
/*==============================================================*/
create unique index ix_aup_id on auftragposition
(
   aup_id
);


/*==============================================================*/
/* Table: calendar_event                                        */
/*==============================================================*/
create table calendar_event (
   cle_id               bigint               not null,
   cle_title            VARCHAR(20)          null,
   cle_content          VARCHAR(300)         not null,
   cle_begin_date       TIMESTAMP            not null,
   cle_end_date         TIMESTAMP            not null,
   cle_header_color     VARCHAR(10)          null,
   cle_content_color    VARCHAR(10)          null,
   cle_usr_id           bigint               not null,
   cle_locked           BOOL                 null default false,
   version              int                  not null default 0,
   primary key (cle_id)
);

/*==============================================================*/
/* Index: idx_cle_id                                            */
/*==============================================================*/
create unique index idx_cle_id on calendar_event (
cle_id
);


/*==============================================================*/
/* Table: filiale                                               */
/*==============================================================*/
create table filiale
(
   fil_id               bigint not null,
   fil_nr               varchar(20) not null,
   fil_bezeichnung      varchar(50),
   fil_name1            varchar(50),
   fil_name2            varchar(50),
   fil_ort              varchar(50),
   version              int not null default 0,
   primary key (fil_id)
);

/*==============================================================*/
/* Index: ix_fil_bezeichnung                                    */
/*==============================================================*/
create index ix_fil_bezeichnung on filiale
(
   fil_bezeichnung
);

/*==============================================================*/
/* Index: ix_fil_nr                                             */
/*==============================================================*/
create unique index ix_fil_nr on filiale
(
   fil_nr
);

/*==============================================================*/
/* Table: kunde                                                 */
/*==============================================================*/
create table kunde
(
   kun_id               bigint not null,
   kun_fil_id           bigint not null,
   kun_bra_id           bigint,
   kun_nr               varchar(20) not null,
   kun_matchcode        varchar(20),
   kun_name1            varchar(50),
   kun_name2            varchar(50),
   kun_ort              varchar(50),
   kun_mahnsperre       bool,
   version              int not null default 0,
   primary key (kun_id)
);

/*==============================================================*/
/* Index: ix_kun_id                                             */
/*==============================================================*/
create unique index ix_kun_id on kunde
(
   kun_id
);

/*==============================================================*/
/* Index: ix_kun_nr                                             */
/*==============================================================*/
create unique index ix_kun_nr on kunde
(
   kun_nr
);

/*==============================================================*/
/* Index: ix_kun_name1                                          */
/*==============================================================*/
create index ix_kun_name1 on kunde
(
   kun_name1
);

/*==============================================================*/
/* Index: ix_kun_name2                                          */
/*==============================================================*/
create index ix_kun_name2 on kunde
(
   kun_name2
);

/*==============================================================*/
/* Index: ix_kun_matchcode                                      */
/*==============================================================*/
create index ix_kun_matchcode on kunde
(
   kun_matchcode
);

/*==============================================================*/
/* Index: ix_kun_ort                                            */
/*==============================================================*/
create index ix_kun_ort on kunde
(
   kun_ort
);

/*==============================================================*/
/* Table: sec_group                                             */
/*==============================================================*/
create table sec_group
(
   grp_id               bigint not null,
   grp_shortdescription varchar(100) not null,
   grp_longdescription  varchar(1000),
   version              int not null default 0,
   primary key (grp_id)
);

/*==============================================================*/
/* Index: idx_grp_id                                            */
/*==============================================================*/
create unique index idx_grp_id on sec_group
(
   grp_id
);

/*==============================================================*/
/* Index: idx_grp_shortdescription                              */
/*==============================================================*/
create unique index idx_grp_shortdescription on sec_group
(
   grp_shortdescription
);

/*==============================================================*/
/* Table: sec_groupright                                        */
/*==============================================================*/
create table sec_groupright
(
   gri_id               bigint not null,
   grp_id               bigint not null,
   rig_id               bigint not null,
   version              int not null default 0,
   primary key (gri_id)
);

/*==============================================================*/
/* Index: idx_gri_id                                            */
/*==============================================================*/
create unique index idx_gri_id on sec_groupright
(
   gri_id
);

/*==============================================================*/
/* Index: idx_gri_grprig                                        */
/*==============================================================*/
create unique index idx_gri_grprig on sec_groupright
(
   grp_id,
   rig_id
);

/*==============================================================*/
/* Table: sec_loginlog                                          */
/*==============================================================*/
create table sec_loginlog
(
   lgl_id               bigint not null,
   i2c_id               INT  ,
   lgl_loginname        varchar(50) not null,
   lgl_logtime          timestamp not null,
   lgl_ip               varchar(19),
   lgl_browsertype      VARCHAR(40)          null,
   lgl_status_id        int not null,
   lgl_sessionid        varchar(50),
   version              INT not null default 0,
   constraint PK_SEC_LOGINLOG primary key (lgl_id)
);

/*==============================================================*/
/* Index: idx_lgl_id                                            */
/*==============================================================*/
create unique index idx_lgl_id on sec_loginlog
(
   lgl_id
);

/*==============================================================*/
/* Index: idx_lgl_logtime                                       */
/*==============================================================*/
create index idx_lgl_logtime on sec_loginlog
(
   lgl_logtime
);

/*==============================================================*/
/* Table: sec_right                                             */
/*==============================================================*/
create table sec_right
(
   rig_id               bigint not null,
   rig_type             int default 1,
   rig_name             varchar(50) not null,
   version              int not null default 0,
   primary key (rig_id)
);

/*==============================================================*/
/* Index: idx_rig_id                                            */
/*==============================================================*/
create unique index idx_rig_id on sec_right
(
   rig_id
);

/*==============================================================*/
/* Index: idx_rig_type                                          */
/*==============================================================*/
create index idx_rig_type on sec_right
(
   rig_type
);

/*==============================================================*/
/* Index: idx_rig_name                                          */
/*==============================================================*/
create unique index idx_rig_name on sec_right
(
   rig_name
);

/*==============================================================*/
/* Table: sec_role                                              */
/*==============================================================*/
create table sec_role
(
   rol_id               bigint not null,
   rol_shortdescription varchar(100) not null,
   rol_longdescription  varchar(1000),
   version              int not null default 0,
   primary key (rol_id)
);

alter table sec_role comment 'Defines the roles that are used in the application.';

/*==============================================================*/
/* Index: idx_role_id                                           */
/*==============================================================*/
create unique index idx_role_id on sec_role
(
   rol_id
);

/*==============================================================*/
/* Index: idx_role_shortdescription                             */
/*==============================================================*/
create unique index idx_role_shortdescription on sec_role
(
   rol_shortdescription
);

/*==============================================================*/
/* Table: sec_rolegroup                                         */
/*==============================================================*/
create table sec_rolegroup
(
   rlg_id               bigint not null,
   grp_id               bigint not null,
   rol_id               bigint not null,
   version              int not null default 0,
   primary key (rlg_id)
);

/*==============================================================*/
/* Index: idx_rlg_id                                            */
/*==============================================================*/
create unique index idx_rlg_id on sec_rolegroup
(
   rlg_id
);

/*==============================================================*/
/* Index: idx_rlg_grprol                                        */
/*==============================================================*/
create unique index idx_rlg_grprol on sec_rolegroup
(
   grp_id,
   rol_id
);

/*==============================================================*/
/* Table: sec_user                                              */
/*==============================================================*/
create table sec_user
(
   usr_id               bigint not null,
   usr_loginname        varchar(50) not null,
   usr_password         varchar(50) not null,
   usr_lastname         varchar(50),
   usr_firstname        varchar(50),
   usr_email            varchar(200),
   usr_locale           varchar(5),
   usr_enabled          bool not null default FALSE,
   usr_accountNonExpired bool not null default TRUE,
   usr_credentialsNonExpired bool not null default TRUE,
   usr_accountNonLocked bool not null default TRUE,
   usr_token            varchar(20),
   version              int not null default 0,
   primary key (usr_id)
);

/*==============================================================*/
/* Index: idx_usr_id                                            */
/*==============================================================*/
create unique index idx_usr_id on sec_user
(
   usr_id
);

/*==============================================================*/
/* Index: idx_usr_loginname                                     */
/*==============================================================*/
create unique index idx_usr_loginname on sec_user
(
   usr_loginname
);

/*==============================================================*/
/* Table: sec_userrole                                          */
/*==============================================================*/
create table sec_userrole
(
   urr_id               bigint not null,
   usr_id               bigint not null,
   rol_id               bigint not null,
   version              int not null default 0,
   primary key (urr_id)
);

alter table sec_userrole comment 'Holdes the Roles that a user have.';

/*==============================================================*/
/* Index: idx_urr_id                                            */
/*==============================================================*/
create unique index idx_urr_id on sec_userrole
(
   urr_id
);

/*==============================================================*/
/* Index: idx_urr_usrrol                                        */
/*==============================================================*/
create unique index idx_urr_usrrol on sec_userrole
(
   usr_id,
   rol_id
);


/*==============================================================*/
/* Table: sys_countrycode                                       */
/*==============================================================*/
create table sys_countrycode (
   ccd_id               INT                 not null,
   ccd_name             VARCHAR(48)          null,
   ccd_code2            VARCHAR(2)           not null,
   version              INT                 null default 0,
   constraint PK_SYS_COUNTRYCODE primary key (ccd_id)
);

create table log_ip2country (
   i2c_id               INT                 not null,
   ccd_id               INT                ,
   i2c_city             VARCHAR(50)         ,
   i2c_latitude         FLOAT               ,
   i2c_longitude        FLOAT               ,
   version              INT                 default 0,
   constraint PK_LOG_IP2COUNTRY primary key (i2c_id)
);

/*==============================================================*/
/* Table: guestbook                                             */
/*==============================================================*/
create table guestbook (
   gub_id               INT                 not null,
   gub_subject          VARCHAR(40)          not null,
   gub_date             TIMESTAMP            not null,
   gub_usr_name         VARCHAR(40)          not null,
   gub_text             TEXT                 null,
   version              INT                 not null default 0,
   constraint PK_GUESTBOOK primary key (gub_id)
);

/*==============================================================*/
/* Table: ipc_ip4country                                        */
/*==============================================================*/
create table ipc_ip4country (
   ipc_id               INT                 not null,
   ipc_ip_from          INT                 ,
   ipc_ip_to            INT                 ,
   ipc_country_code2    VARCHAR(2)          ,
   ipc_country_code3    VARCHAR(3)          ,
   ipc_country_name     VARCHAR(50)         ,
   version              INT                 not null default 0,
   constraint PK_ipc_ip4country primary key (ipc_id)
);


/*==============================================================*/
/* Table: ipc_ip2country                                        */
/*==============================================================*/
create table ipc_ip2country (
   ipc_id               INT                 not null,
   ipc_ip_from          INT                 null,
   ipc_ip_to            INT                 null,
   ipc_country_code2    VARCHAR(2)           null,
   ipc_country_code3    VARCHAR(3)           null,
   ipc_country_name     VARCHAR(50)          null,
   version              INT4                 not null default 0,
   constraint PK_IPC_IP2COUNTRY primary key (ipc_id)
);


/*==============================================================*/
/* Index: idx_ipc_id                                            */
/*==============================================================*/
create unique index idx_ipc_id on ipc_ip2country (
ipc_id
);

/*==============================================================*/
/* Index: idx_ipc_ip_from                                       */
/*==============================================================*/
create  index idx_ipc_ip_from on ipc_ip2country (
ipc_ip_from
);

/*==============================================================*/
/* Index: idx_ipc_ip_to                                         */
/*==============================================================*/
create  index idx_ipc_ip_to on ipc_ip2country (
ipc_ip_to
);

/*==============================================================*/
/* Index: idx_ipc_country_code2                                 */
/*==============================================================*/
create  index idx_ipc_country_code2 on ipc_ip2country (
ipc_country_code2
);

/*==============================================================*/
/* Index: idx_ipc_country_code3                                 */
/*==============================================================*/
create  index idx_ipc_country_code3 on ipc_ip2country (
ipc_country_code3
);

/*==============================================================*/
/* Index: idx_ipc_country_name                                  */
/*==============================================================*/
create  index idx_ipc_country_name on ipc_ip2country (
ipc_country_name
);



alter table auftrag add constraint ref_auf_to_kun foreign key (auf_kun_id)
      references kunde (kun_id) on delete cascade on update cascade;

alter table auftragposition add constraint ref_aup_to_art foreign key (art_id)
      references artikel (art_id) on delete restrict on update restrict;

alter table auftragposition add constraint ref_aup_to_auf foreign key (aup_auf_id)
      references auftrag (auf_id) on delete cascade on update cascade;

alter table kunde add constraint ref_kun_to_bra foreign key (kun_bra_id)
      references Branche (bra_id) on delete restrict on update restrict;

alter table kunde add constraint ref_kun_to_fil foreign key (kun_fil_id)
      references filiale (fil_id) on delete cascade on update cascade;

alter table sec_groupright add constraint ref_gri_to_grp foreign key (grp_id)
      references sec_group (grp_id) on delete restrict on update restrict;

alter table sec_groupright add constraint ref_gri_to_rig foreign key (rig_id)
      references sec_right (rig_id) on delete restrict on update restrict;

alter table sec_rolegroup add constraint ref_rlg_to_grp foreign key (grp_id)
      references sec_group (grp_id) on delete restrict on update restrict;

alter table sec_rolegroup add constraint ref_rlg_to_rol foreign key (rol_id)
      references sec_role (rol_id) on delete restrict on update restrict;

alter table sec_userrole add constraint ref_aut_to_rol foreign key (rol_id)
      references sec_role (rol_id) on delete restrict on update restrict;

alter table sec_userrole add constraint ref_aut_to_usr foreign key (usr_id)
      references sec_user (usr_id) on delete restrict on update restrict;


      
      
      
/********** Hibernate DB Performance Logging ****************/
/**** SEQUENCE FOR Hibernate DB performance logging****/
/*
      DROP TABLE IF EXISTS hibernate_statistic_key;
CREATE TABLE IF NOT EXISTS hibernate_statistic_key(
  id int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM;

INSERT INTO hibernate_statistic_key  VALUES (1);

DROP FUNCTION IF EXISTS hibernate_statistic_sequence;

DELIMITER $$

CREATE FUNCTION hibernate_statistic_sequence()
RETURNS INT
BEGIN
DECLARE nextid INT;
update hibernate_statistic_sequence set id = id + 1;
select id into nextid from hibernate_statistic_key;
RETURN nextid;
END$$
DELIMITER ;

DROP VIEW IF EXISTS hibernate_statistic_sequence;  
CREATE VIEW hibernate_statistic_sequence 
AS 
SELECT hibernate_statistic_sequence() ;
*/
      
/*==============================================================*/
/* Table: Hibernate_Statistics                                  */
/*==============================================================*/
CREATE TABLE hibernate_statistics
(
  id INT NOT NULL auto_increment,
  flushcount integer NOT NULL,
  preparestatementcount integer NOT NULL,
  entityloadcount integer NOT NULL,
  entityupdatecount integer NOT NULL,
  entityinsertcount integer NOT NULL,
  entitydeletecount integer NOT NULL,
  entityfetchcount integer NOT NULL,
  collectionloadcount integer NOT NULL,
  collectionupdatecount integer NOT NULL,
  collectionremovecount integer NOT NULL,
  collectionrecreatecount integer NOT NULL,
  collectionfetchcount integer NOT NULL,
  queryexecutioncount integer NOT NULL,
  queryexecutionmaxtime integer NOT NULL,
  optimisticfailurecount integer NOT NULL,
  queryexecutionmaxtimequerystring text,
  callmethod text NOT NULL,
  javafinishms bigint NOT NULL,
  finishtime TIMESTAMP NOT NULL,
  CONSTRAINT hibernatestatistics_pkey PRIMARY KEY (id)
);

/*==============================================================*/
/* Table: Hibernate_                                               */
/*==============================================================*/
CREATE TABLE hibernate_entity_statistics
(
  id INT NOT NULL auto_increment,
  hibernateentitystatisticsid bigint NOT NULL,
  entityname text NOT NULL,
  loadcount integer NOT NULL,
  updatecount integer NOT NULL,
  insertcount integer NOT NULL,
  deletecount integer NOT NULL,
  fetchcount integer NOT NULL,
  optimisticfailurecount integer NOT NULL,
  CONSTRAINT hibernateentitystatistics_pkey PRIMARY KEY (id)
);

CREATE INDEX fki_
  ON hibernate_entity_statistics (
  hibernateentitystatisticsid
  );
  
/*************End Hibernate Statistics **********************/
      
      

/******************** TEST DATA ********************/


/******************** Filiale Daten ********************/
INSERT INTO FILIALE (FIL_ID, FIL_NR, FIL_BEZEICHNUNG,FIL_NAME1,FIL_NAME2,FIL_ORT,VERSION)
values (1,'0001','Filiale Berlin','Hï¿½rmann Gmbh','Personaldienstleistungen','Berlin',0);

/******************** Security: USERS ********************/  
INSERT INTO SEC_USER (USR_ID, USR_LOGINNAME, USR_PASSWORD, USR_LASTNAME, USR_FIRSTNAME, USR_EMAIL, USR_LOCALE, USR_ENABLED, USR_ACCOUNTNONEXPIRED, USR_CREDENTIALSNONEXPIRED, USR_ACCOUNTNONLOCKED, USR_TOKEN,  VERSION) values 
(10, 'guest', 'guest', 'guestFirstname', 'guestlastname', 'guest@web.de', NULL, true, true, true, true, null, 0),
(11, 'admin', 'admin', 'Visor', 'Super', 'admin@web.de', NULL, true, true, true, true, null, 0),
(12, 'user1', 'user1', 'Kingsley', 'Ben', 'B.Kingsley@web.de', NULL, true, true, true, true, null, 0),
(13, 'headoffice', 'headoffice', 'Willis', 'Bruce', 'B.Willis@web.de', NULL, true, true, true, true, null, 0),
(14, 'user2', 'user2', 'Kingdom', 'Marta', 'M.Kingdom@web.de', NULL, true, true, true, true, null, 0);

/******************** Security: ROLES ********************/  
INSERT INTO SEC_ROLE (ROL_ID, ROL_SHORTDESCRIPTION, ROL_LONGDESCRIPTION, VERSION) values
(10000,'ROLE_ADMIN','Administrator Role', 0),
(10002,'ROLE_OFFICE_ALL_RIGHTS','Office User role with all rights granted.', 0),
(10003,'ROLE_GUEST','Guest Role', 0),
(10004,'ROLE_OFFICE_ONLY_VIEW','Office user with rights that granted only view of data.', 0),
(10005,'ROLE_HEADOFFICE_USER','Headoffice User Role', 0);


/******************** Security: USER-ROLES ********************/  
/* Guest account authorities */
INSERT INTO SEC_USERROLE (URR_ID, USR_ID, ROL_ID, VERSION) values
(11000, 10, 10003, 0),
/* User1 Usr-Id: 12 */
(11001, 12, 10002, 0),
/* Headoffice user account authorities */
(11010, 13, 10005, 0);

/* Admin Usr-ID: 11 */
INSERT INTO SEC_USERROLE (URR_ID, USR_ID, ROL_ID, VERSION) values
(11003, 11, 10000, 0),
(11005, 11, 10002, 0),
(11006, 11, 10003, 0),
(11008, 11, 10004, 0),
(11009, 11, 10005, 0),
/* User2 Usr-ID: 14 */
(11007, 14, 10004, 0);

/******************** Security: SEC_GROUPS ********************/  
INSERT INTO SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) values
(13001, 'Headoffice Supervisor Group', 'kjhf ff hgfd', 0),
(13002, 'common Admin Group / user accounts', 'create/modify user accounts', 0),
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
INSERT INTO SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) values 
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
INSERT INTO SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) values
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

/* Pages = Type(0) */
/* --> Page Customer */
(15100, 0, 'window_customerList', 0),
(15101, 0, 'window_customerDialog', 0),
/* --> Page Orders */
(15102, 0, 'orderListWindow', 0),
(15103, 0, 'orderDialogWindow', 0),
/* --> Page Articles */
(15104, 0, 'window_ArticlesList', 0),
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
(15500, 0, 'button_BranchList_btnHelp', 0),
(15501, 0, 'button_BranchList_NewBranch', 0),
(15502, 0, 'button_BranchList_PrintBranches', 0),
(15503, 0, 'button_BranchList_Search_BranchName', 0),
/* branchDialogWindow BUTTONS */
(15510, 6, 'button_BranchDialog_btnHelp', 0),
(15511, 6, 'button_BranchDialog_btnNew', 0),
(15512, 6, 'button_BranchDialog_btnEdit', 0),
(15513, 6, 'button_BranchDialog_btnDelete', 0),
(15514, 6, 'button_BranchDialog_btnSave', 0),
(15515, 6, 'button_BranchDialog_btnClose', 0),
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
(15600, 6, 'button_OfficeList_btnHelp', 0),
(15601, 6, 'button_OfficeList_NewOffice', 0),
(15602, 6, 'button_OfficeList_PrintList', 0),
(15603, 6, 'button_OfficeList_SearchNo', 0),
(15604, 6, 'button_OfficeList_SearchName', 0),
(15605, 6, 'button_OfficeList_SearchCity', 0),
/* window_OfficeDialog BUTTONS */
(15610, 6, 'button_OfficeDialog_PrintOffice', 0),
(15611, 6, 'button_OfficeDialog_btnHelp', 0),
(15612, 6, 'button_OfficeDialog_btnNew', 0),
(15613, 6, 'button_OfficeDialog_btnEdit', 0),
(15614, 6, 'button_OfficeDialog_btnDelete', 0),
(15615, 6, 'button_OfficeDialog_btnSave', 0),
(15616, 6, 'button_OfficeDialog_btnClose', 0),

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
INSERT INTO SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) values 
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
/* Hibernate Statistic */
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
/* Right: button_BranchList_btnHelp */
(14503, 13020, 15500, 0),
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
/* Right: button_BranchList_NewBranch */
(14510, 13021, 15501, 0),
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
/* Right: button_OfficeList_btnHelp */
(14573, 13040, 15600, 0),
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
/* Right: button_OfficeDialog_PrintOffice */
(14581, 13040, 15610, 0),

/* Group: Offices_New */
/* Right: button_OfficeList_NewOffice */
(14585, 13041, 15601, 0),
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
INSERT INTO BRANCHE (BRA_ID, BRA_NR, BRA_BEZEICHNUNG, VERSION) VALUES
(1000, '100', 'Elektro',0),
(1001, '101', 'Maler',0),
(1002, '102', 'Holzverabeitung',0),
(1003, '103', 'Kaufmaennisch',0),
(1004, '104', 'Versicherung',0),
(1005, '105', 'Mess- und Regeltechnik',0),
(1006, '106', 'Industriemontagen',0),
(1007, '107', 'KFZ',0),
(1008, '108', 'Banken',0),
(1009, '109', 'Grosshandel',0),
(1010, '110', 'Einzelhandel',0),
(1011, '111', 'Werbung',0),
(1012, '112', 'Gastronomie',0),
(1014, '114', 'Pflegedienste',0),
(1015, '115', 'Transportwesen',0),
(1016, '116', 'Metallverarbeitung',0),
(1017, '117', 'Schlosserei',0),
(1018, '118', 'Sanitaer',0),
(1019, '119', 'Heizungsbau',0),
(1020, '120', 'Wasserwirtschaft',0),
(1021, '121', 'Schiffsbau',0),
(1022, '122', 'Laermschutz',0),
(1023, '123', 'Geruestbau',0),
(1024, '124', 'Fassadenbau',0),
(1025, '125', 'Farbherstellung',0),
(1026, '126', 'Kieswerk',0),
(1027, '127', 'Blechnerei',0),
(1028, '128', 'Geruestverleih',0),
(1029, '129', 'Pflasterarbeiten',0),
(1030, '130', 'Trockenbau',0),
(1031, '131', 'Trockenbau- und Sanierung',0),
(1032, '132', 'Huehnerfarm',0),
(1033, '000', '',0),
(1034, '134', 'Transportwesen allgemein',0),
(1035, '135', 'Schwertransport',0),
(1036, '136', 'Gefahrgut Transport',0),
(1037, '137', 'Spedition',0);

/******************** Kunden Daten ********************/
INSERT INTO KUNDE (KUN_ID,KUN_FIL_ID,KUN_BRA_ID, KUN_NR, KUN_MATCHCODE,KUN_NAME1,KUN_NAME2,KUN_ORT,KUN_MAHNSPERRE,VERSION) VALUES 
(20,1,1000, '20', 'MUELLER','--> Mueller','Elektroinstallationen','Freiburg',true,0),
(21,1,1000, '21', 'HUBER','--> Huber','Elektroinstallationen','Oberursel',true,0),
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

/******************** Auftrag Daten ********************/
INSERT INTO AUFTRAG (AUF_ID,AUF_KUN_ID, AUF_NR, AUF_BEZEICHNUNG, VERSION) VALUES
(40, 20, 'AUF-40', 'EGRH Modul1',0),
(41, 20, 'AUF-41', 'OMEGA Modul2',0),
(42, 20, 'AUF-42', 'Keller',0),
(43, 20, 'AUF-43', 'Schlossallee 23',0),
(44, 21, 'AUF-44', 'Renovierung Keller',0),
(45, 21, 'AUF-45', 'Renovierung Hochhaus Ilsestrasse 5',0),
(46, 21, 'AUF-46', 'Verteilerschrank Umbau; Fa. Kloeckner EHG',0);

/******************** Artikel Daten ********************/
INSERT INTO ARTIKEL (ART_ID,ART_KURZBEZEICHNUNG,ART_LANGBEZEICHNUNG,ART_NR,ART_PREIS,VERSION) VALUES 
(3000,'Kabelverschraubung DN 27','Kabelverschraubung Messing verchromt DN 27','KS3000',4.23,0),
(3001,'3-adriges Kabel 1,5mm ','mehradriges Kabel, 3 Adern, Farbe=grau, Querschnitt= 1,5 mm','MK3001',0.12,0),
(3002,'Luesterklemmen 10-fach, bis 1.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER1002',0.78,0),
(3003,'Euro-Platine','Euro-Platine fuer Versuchsaufbauten','PLAT3003',2.34,0),
(3004,'Leuchtmittel 22 Watt','Leuchtmittel 22 Watt Sparlampe weiss, mittlere Lebensdauer ca. 6000 Std.','SPARLA3004',2.84,0),
(3005,'Leuchte einzel bis 100 Watt','Hngeleuchte einzel, Farbe=grau, bis 100 Watt','LEU3005',32.00,0),
(3006,'5-adriges Kabel 1,5mm','mehradriges Kabel, 5 Adern, Farbe=grau, Querschnitt= 1,5 mm','MK3006',0.22, 0),
(3007,'Kabelbinder 12cm','Kabelbinder, Menge: 100 Stk. Lnge: 12 cm, Farbe: weiss','KB3007',1.34, 0),
(3008,'Kabelverschraubung DN 17','Kabelverschraubung Messing verchromt DN 17','KS3008',2.90,0),
(3009,'Kabelverschraubung DN 18','Kabelverschraubung Messing verchromt DN 18','KS3009',3.20,0),
(3010,'Kabelverschraubung DN 22','Kabelverschraubung Messing verchromt DN 22','KS3010',3.40,0),
(3011,'Luesterklemmen 10-fach, bis 2.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER3011',1.68,0),
(3012,'Luesterklemmen 10-fach, bis 4.5mm','Luesterklemmen, grau, bis Querschnitt 1.5mm','LUESTER3012',3.10,0);



/******************** Auftrag Positionen Daten ********************/
/* Auftrag: 40 */
INSERT INTO AUFTRAGPOSITION (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES 
(60,40, 3000, 1, 240, 1.20, 288.00, 0),
(61,40, 3001, 2, 1200, 0.45, 540.00, 0),
(62,40, 3002, 3, 40, 0.20, 8.00, 0),
(63,40, 3003, 4, 15, 4.20, 63.00, 0),
(64,40, 3004, 5, 20, 4.30, 86.00, 0),
(65,40, 3005, 6, 15, 40.00, 600.00, 0);
/* Auftrag: 41 */
INSERT INTO AUFTRAGPOSITION (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES
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


 /* fill the countrycodes */
INSERT INTO sys_countrycode(CCD_ID, CCD_NAME, CCD_CODE2, VERSION) VALUES 
(-1,'UNROUTABLE ADDRESS','xx', 0),
(1, 'AFGHANISTAN','AF', 0),
(2, 'ALBANIA','AL', 0),
(3, 'ALGERIA','DZ', 0),
(4, 'AMERICAN SAMOA','AS', 0),
(5, 'ANDORRA','AD', 0),
(6, 'ANGOLA','AO', 0),
(7, 'ANGUILLA','AI', 0),
(8, 'ANTARCTICA','AQ', 0),
(9, 'ANTIGUA AND BARBUDA','AG', 0),
(10,'ARGENTINA','AR', 0),
(11,'ARMENIA','AM', 0),
(12,'ARUBA','AW', 0),
(13,'AUSTRALIA','AU', 0),
(14,'AUSTRIA','AT', 0),
(15,'AZERBAIJAN','AZ', 0),
(16,'BAHAMAS','BS', 0),
(17,'BAHRAIN','BH', 0),
(18,'BANGLADESH','BD', 0),
(19,'BARBADOS','BB', 0),
(20,'BELARUS','BY', 0),
(21,'BELGIUM','BE', 0),
(22,'BELIZE','BZ', 0),
(23,'BENIN','BJ', 0),
(24,'BERMUDA','BM', 0),
(25,'BHUTAN','BT', 0),
(26,'BOLIVIA','BO', 0),
(27,'BOSNIA AND HERZEGOVINA','BA', 0),
(28,'BOTSWANA','BW', 0),
(29,'BOUVET ISLAND','BV', 0),
(30,'BRAZIL','BR', 0),
(31,'BRITISH INDIAN OCEAN TERRITORY','IO', 0),
(32,'BRUNEI DARUSSALAM','BN', 0),
(33,'BULGARIA','BG', 0),
(34,'BURKINA FASO','BF', 0),
(35,'BURUNDI','BI', 0),
(36,'CAMBODIA','KH', 0),
(37,'CAMEROON','CM', 0),
(38,'CANADA','CA', 0),
(39,'CAPE VERDE','CV', 0),
(40,'CAYMAN ISLANDS','KY', 0),
(41,'CENTRAL AFRICAN REPUBLIC','CF', 0),
(42,'CHAD','TD', 0),
(43,'CHILE','CL', 0),
(44,'CHINA','CN', 0),
(45,'CHRISTMAS ISLAND','CX', 0),
(46,'COCOS (KEELING) ISLANDS','CC', 0),
(47,'COLOMBIA','CO', 0),
(48,'COMOROS','KM', 0),
(49,'CONGO','CG', 0),
(50,'CONGO, THE DEMOCRATIC REPUBLIC OF THE','CD', 0),
(51,'COOK ISLANDS','CK', 0),
(52,'COSTA RICA','CR', 0),
(53,'COTE D IVOIRE','CI', 0),
(54,'CROATIA','HR', 0),
(55,'CUBA','CU', 0),
(56,'CYPRUS','CY', 0),
(57,'CZECH REPUBLIC','CZ', 0),
(58,'DENMARK','DK', 0),
(59,'DJIBOUTI','DJ', 0),
(60,'DOMINICA','DM', 0),
(61,'DOMINICAN REPUBLIC','DO', 0),
(62,'ECUADOR','EC', 0),
(63,'EGYPT','EG', 0),
(64,'EL SALVADOR','SV', 0),
(65,'EQUATORIAL GUINEA','GQ', 0),
(66,'ERITREA','ER', 0),
(67,'ESTONIA','EE', 0),
(68,'ETHIOPIA','ET', 0),
(69,'FALKLAND ISLANDS','FK', 0),
(70,'FAROE ISLANDS','FO', 0),
(71,'FIJI','FJ', 0),
(72,'FINLAND','FI', 0),
(73,'FRANCE','FR', 0),
(74,'FRENCH GUIANA','GF', 0),
(75,'FRENCH POLYNESIA','PF', 0),
(76,'FRENCH SOUTHERN TERRITORIES','TF', 0),
(77,'GABON','GA', 0),
(78,'GAMBIA','GM', 0),
(79,'GEORGIA','GE', 0),
(80,'GERMANY','DE', 0),
(81,'GHANA','GH', 0),
(82,'GIBRALTAR','GI', 0),
(83,'GREECE','GR', 0),
(84,'GREENLAND','GL', 0),
(85,'GRENADA','GD', 0),
(86,'GUADELOUPE','GP', 0),
(87,'GUAM','GU', 0),
(88,'GUATEMALA','GT', 0),
(89,'GUINEA','GN', 0),
(90,'GUINEA-BISSAU','GW', 0),
(91,'GUYANA','GY', 0),
(92,'HAITI','HT', 0),
(93,'HEARD ISLAND AND MCDONALD ISLANDS','HM', 0),
(94,'HOLY SEE (VATICAN CITY STATE)','VA', 0),
(95,'HONDURAS','HN', 0),
(96,'HONG KONG','HK', 0),
(97,'HUNGARY','HU', 0),
(98,'ICELAND','IS', 0),
(99,'INDIA','IN', 0),
(100,'INDONESIA','ID', 0),
(101,'IRAN, ISLAMIC REPUBLIC OF','IR', 0),
(102,'IRAQ','IQ', 0),
(103,'IRELAND','IE', 0),
(104,'ISRAEL','IL', 0),
(105,'ITALY','IT', 0),
(106,'JAMAICA','JM', 0),
(107,'JAPAN','JP', 0),
(108,'JORDAN','JO', 0),
(109,'KAZAKHSTAN','KZ', 0),
(110,'KENYA','KE', 0),
(111,'KIRIBATI','KI', 0),
(112,'KOREA, DEMOCRATIC REPUBLIC OF','KP', 0),
(113,'KOREA, REPUBLIC OF','KR', 0),
(114,'KUWAIT','KW', 0),
(115,'KYRGYZSTAN','KG', 0),
(116,'LAO DEMOCRATIC REPUBLIC','LA', 0),
(117,'LATVIA','LV', 0),
(118,'LEBANON','LB', 0),
(119,'LESOTHO','LS', 0),
(120,'LIBERIA','LR', 0),
(121,'LIBYAN ARAB JAMAHIRIYA','LY', 0),
(122,'LIECHTENSTEIN','LI', 0),
(123,'LITHUANIA','LT', 0),
(124,'LUXEMBOURG','LU', 0),
(125,'MACAO','MO', 0),
(126,'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF','MK', 0),
(127,'MADAGASCAR','MG', 0),
(128,'MALAWI','MW', 0),
(129,'MALAYSIA','MY', 0),
(130,'MALDIVES','MV', 0),
(131,'MALI','ML', 0),
(132,'MALTA','MT', 0),
(133,'MARSHALL ISLANDS','MH', 0),
(134,'MARTINIQUE','MQ', 0),
(135,'MAURITANIA','MR', 0),
(136,'MAURITIUS','MU', 0),
(137,'MAYOTTE','YT', 0),
(138,'MEXICO','MX', 0),
(139,'MICRONESIA, FEDERATED STATES OF','FM', 0),
(140,'MOLDOVA, REPUBLIC OF','MD', 0),
(141,'MONACO','MC', 0),
(142,'MONGOLIA','MN', 0),
(143,'MONTSERRAT','MS', 0),
(144,'MOROCCO','MA', 0),
(145,'MOZAMBIQUE','MZ', 0),
(146,'MYANMAR','MM', 0),
(147,'NAMIBIA','NA', 0),
(148,'NAURU','NR', 0),
(149,'NEPAL','NP', 0),
(150,'NETHERLANDS','NL', 0),
(151,'NETHERLANDS ANTILLES','AN', 0),
(152,'NEW CALEDONIA','NC', 0),
(153,'NEW ZEALAND','NZ', 0),
(154,'NICARAGUA','NI', 0),
(155,'NIGER','NE', 0),
(156,'NIGERIA','NG', 0),
(157,'NIUE','NU', 0),
(158,'NORFOLK ISLAND','NF', 0),
(159,'NORTHERN MARIANA ISLANDS','MP', 0),
(160,'NORWAY','NO', 0),
(161,'OMAN','OM', 0),
(162,'PAKISTAN','PK', 0),
(163,'PALAU','PW', 0),
(164,'PALESTINIAN TERRITORY, OCCUPIED','PS', 0),
(165,'PANAMA','PA', 0),
(166,'PAPUA NEW GUINEA','PG', 0),
(167,'PARAGUAY','PY', 0),
(168,'PERU','PE', 0),
(169,'PHILIPPINES','PH', 0),
(170,'PITCAIRN','PN', 0),
(171,'POLAND','PL', 0),
(172,'PORTUGAL','PT', 0),
(173,'PUERTO RICO','PR', 0),
(174,'QATAR','QA', 0),
(175,'REUNION','RE', 0),
(176,'ROMANIA','RO', 0),
(177,'RUSSIAN FEDERATION','RU', 0),
(178,'RWANDA','RW', 0),
(179,'SAINT HELENA','SH', 0),
(180,'SAINT KITTS AND NEVIS','KN', 0),
(181,'SAINT LUCIA','LC', 0),
(182,'SAINT PIERRE AND MIQUELON','PM', 0),
(183,'SAINT VINCENT AND THE GRENADINES','VC', 0),
(184,'SAMOA','WS', 0),
(185,'SAN MARINO','SM', 0),
(186,'SAO TOME AND PRINCIPE','ST', 0),
(187,'SAUDI ARABIA','SA', 0),
(188,'SENEGAL','SN', 0),
(189,'SERBIA','RS', 0),
(190,'SEYCHELLES','SC', 0),
(191,'SIERRA LEONE','SL', 0),
(192,'SINGAPORE','SG', 0),
(193,'SLOVAKIA','SK', 0),
(194,'SLOVENIA','SI', 0),
(195,'SOLOMON ISLANDS','SB', 0),
(196,'SOMALIA','SO', 0),
(197,'SOUTH AFRICA','ZA', 0),
(198,'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS','GS', 0),
(199,'SPAIN','ES', 0),
(200,'SRI LANKA','LK', 0),
(201,'SUDAN','SD', 0),
(202,'SURINAME','SR', 0),
(203,'SVALBARD AND JAN MAYEN','SJ', 0),
(204,'SWAZILAND','SZ', 0),
(205,'SWEDEN','SE', 0),
(206,'SWITZERLAND','CH', 0),
(207,'SYRIAN ARAB REPUBLIC','SY', 0),
(208,'TAIWAN','TW', 0),
(209,'TAJIKISTAN','TJ', 0),
(210,'TANZANIA, UNITED REPUBLIC OF','TZ', 0),
(211,'THAILAND','TH', 0),
(212,'TIMOR-LESTE','TL', 0),
(213,'TOGO','TG', 0),
(214,'TOKELAU','TK', 0),
(215,'TONGA','TO', 0),
(216,'TRINIDAD AND TOBAGO','TT', 0),
(217,'TUNISIA','TN', 0),
(218,'TURKEY','TR', 0),
(219,'TURKMENISTAN','TM', 0),
(220,'TURKS AND CAICOS ISLANDS','TC', 0),
(221,'TUVALU','TV', 0),
(222,'UGANDA','UG', 0),
(223,'UKRAINE','UA', 0),
(224,'UNITED ARAB EMIRATES','AE', 0),
(225,'UNITED KINGDOM','GB', 0),
(226,'UNITED STATES','US', 0),
(227,'UNITED STATES MINOR OUTLYING ISLANDS','UM', 0),
(228,'URUGUAY','UY', 0),
(229,'UZBEKISTAN','UZ', 0),
(230,'VANUATU','VU', 0),
(231,'VENEZUELA','VE', 0),
(232,'VIET NAM','VN', 0),
(233,'VIRGIN ISLANDS, BRITISH','VG', 0),
(234,'VIRGIN ISLANDS, U.S.','VI', 0),
(235,'WALLIS AND FUTUNA','WF', 0),
(236,'WESTERN SAHARA','EH', 0),
(237,'YEMEN','YE', 0),
(238,'ZAMBIA','ZM', 0),
(239,'ZIMBABWE','ZW', 0),
(240,'UNITED KINGDOM','UK', 0),
(241,'EUROPEAN UNION','EU', 0),
(242,'YUGOSLAVIA','YU', 0),
(244,'ARIPO','AP', 0),
(245,'ASCENSION ISLAND','AC', 0),
(246,'GUERNSEY','GG', 0),
(247,'ISLE OF MAN','IM', 0),
(248,'JERSEY','JE', 0),
(249,'EAST TIMOR','TP', 0),
(250,'MONTENEGRO','ME', 0);

/******************** YouTube Music Links ********************/
INSERT INTO youtube_link (ytb_id, ytb_title, ytb_url, version) VALUES 
(  1, 'Loquat - Swing Set Chain',                                 'http://www.youtube.com/embed/51G24IVfcaI', 0),
(  2, 'Empire of the Sun - We Are The People',                    'http://www.youtube.com/embed/Tj_Nlm0871E', 0),
(  3, 'Loquat - Harder Hit',                                      'http://www.youtube.com/watch?v=aoHUb2r8q-g&feature=rec-LGOUT-exp_fresh+div-1r-3-HM', 0),
(  4, 'THIN LIZZY - Still in Love With You',                      'http://www.youtube.com/watch?v=oHUWXjNU0aM', 0),
(  5, 'Gary Moore with Phil Lynnot - Parisienne Walkways (live)', 'http://www.youtube.com/embed/18FgnFVm5k0', 0),
(  6, 'Talking Heads - This must be the place',                   'http://www.youtube.com/embed/TTPqPZzH-LA', 0),
(  7, 'John Cale and Brian Eno - Spinning away',                  'http://www.youtube.com/embed/-INeMspNSQ0', 0),
(  8, 'Metric - Joyride',                                         'http://www.youtube.com/embed/F0ZL5YWP5I8', 0),
(  9, 'Medina - Kun For Mig + Ensome',                            'http://www.youtube.com/embed/5Gf004et0SI', 0),
( 10, 'Paris - Captain Morgan',                                   'http://www.youtube.com/embed/o6Eq1bH-qA0', 0);


 /* fill sample logins */
INSERT INTO sec_loginlog(lgl_id, i2c_id, lgl_loginname,lgl_logtime, lgl_ip, lgl_status_id,lgl_sessionid, VERSION) VALUES 
( 1, NULL, 'admin', '2009-01-01 13:52:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 2, NULL, 'user1', '2009-01-01 10:12:33', '203.237.141.216', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 3, NULL, 'admin', '2009-01-01 11:12:33', '202.96.188.101', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 4, NULL, 'aaaa', '2009-01-01 12:22:33', '84.234.27.179', 0, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 5, NULL, 'admin', '2009-01-01 12:32:33', '84.139.11.102', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 6, NULL, 'user2', '2009-01-01 13:52:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 7, NULL, 'admin', '2009-01-01 14:45:33', '212.227.148.189', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 8, NULL, 'admin', '2009-01-01 15:33:33', '84.185.153.21', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 9, NULL, 'admin', '2009-01-01 17:22:33', '212.156.5.254', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(10, NULL, 'user1', '2009-01-01 17:22:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(11, NULL, 'admin', '2009-01-01 17:22:33', '121.242.65.131', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(12, NULL, 'admin', '2009-01-01 17:22:33', '202.96.188.101', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(13, NULL, 'headoffice', '2009-01-01 17:22:33', '118.68.97.90', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(14, NULL, 'test', '2009-01-01 17:22:33', '125.160.32.182', 0, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(15, NULL, 'headoffice', '2009-01-01 17:22:33', '70.171.254.160', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(16, NULL, 'headoffice', '2009-01-01 17:22:33', '89.218.26.20', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(17, NULL, 'headoffice', '2009-01-01 17:22:33', '118.68.97.45', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(18, NULL, 'admin', '2009-01-01 17:22:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0);


