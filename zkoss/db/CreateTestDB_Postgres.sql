/* Test Datenbank
 1. Erstellen der Guppenrolle 'toledogruppe', 
    falls schon vorhanden dann erst loeschen.
 2. Erstellen des Users 'toledo'
    falls schon vorhanden dann erst loeschen.
 3. Neuerstellung der Datenbank unter User 'postgres', 
    falls schon vorhanden dann erst löschen
 */



/****************** Gruppenrolle ********************/
DROP ROLE IF EXISTS toledogruppe;
CREATE ROLE toledogruppe
  ENCRYPTED PASSWORD 'toledo'
  NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
  
/****************** User ********************/
DROP USER IF EXISTS toledo;
CREATE ROLE toledo LOGIN
  ENCRYPTED PASSWORD 'toledo'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
GRANT toledogruppe TO toledo;
/****************** Datenbank ********************/
CREATE DATABASE test_db
  WITH OWNER = toledo
       ENCODING = 'UNICODE';

