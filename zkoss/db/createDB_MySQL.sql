/*==============================================================*/
/* DBMS name:      MySQL 5.1                                    */
/* Thanks to AndyX                                              */
/* Created on:     31.08.2010 13:07:33                          */
/* Changes:  */
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
DROP TABLE IF EXISTS hibernate_entity_statistics;
DROP TABLE IF EXISTS hibernate_statistics;

drop table if exists youtube_link;


DROP TABLE IF EXISTS sequenztable;
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
/* Index: idx_ytb_url                                           */
/*==============================================================*/
create unique index idx_ytb_url on youtube_link 
(
   ytb_url
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
   gub_text             TEXT                 ,
   version              INT                 ,
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
       
