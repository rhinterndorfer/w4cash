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

-- Database upgrade script for ORACLE 2.42.5( -> 2.42.6)

-- resources

ALTER TABLE PRODUCTS ADD (BGCOLOR VARCHAR2(25));
ALTER TABLE CATEGORIES ADD (BGCOLOR VARCHAR2(25));
ALTER TABLE TICKETLINES ADD (UNIT VARCHAR2(25), ATTR1 VARCHAR2(25), ATTR2 VARCHAR2(25), ATTR3 VARCHAR2(25), ATTR4 VARCHAR2(25));

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.42.6' WHERE ID = 'w4cashdb';
