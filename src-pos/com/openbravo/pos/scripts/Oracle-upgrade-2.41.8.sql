--    Openbravo POS is a point of sales application designed for touch screens.
--    Copyright (C) 2010 Openbravo, S.L.
--    http://sourceforge.net/projects/openbravopos
--
--    This file is part of Openbravo POS.
--
--    Openbravo POS is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    Openbravo POS is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

-- Database upgrade script for ORACLE 2.41.8( -> 2.41.9)

-- resources
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('44', 'System.AddressLine1', 0, utl_raw.cast_to_raw(''));
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('45', 'System.AddressLine2', 0, utl_raw.cast_to_raw(''));
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('46', 'System.Street', 0, utl_raw.cast_to_raw(''));
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('47', 'System.City', 0, utl_raw.cast_to_raw(''));
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('48', 'System.TAXID', 0, utl_raw.cast_to_raw(''));
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('49', 'System.Thanks', 0, utl_raw.cast_to_raw(''));

UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Menu.Root.txt} WHERE NAME='Menu.Root';
UPDATE ROLES SET PERMISSIONS= $FILE{/com/openbravo/pos/templates/Role.Administrator.xml} WHERE NAME='Administratoren';
UPDATE ROLES SET PERMISSIONS= $FILE{/com/openbravo/pos/templates/Role.Manager.xml} WHERE NAME='Manager';

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.41.9' WHERE ID = 'w4cashdb';
