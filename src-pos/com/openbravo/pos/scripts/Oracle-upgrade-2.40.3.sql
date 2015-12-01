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

-- Database upgrade script for ORACLE 2.40.3 -> 2.40.4


-- role update
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Administrator.xml} WHERE ID='0';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Manager.xml} WHERE ID='1';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Employee.xml} WHERE ID='2';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Guest.xml} WHERE ID='3';

-- resources
UPDATE RESOURCES SET CONTENT=$FILE{/com/openbravo/pos/templates/Ticket.Buttons.xml} WHERE NAME='Ticket.Buttons';
UPDATE RESOURCES SET CONTENT=$FILE{/com/openbravo/pos/templates/ticket.save.txt} WHERE NAME='ticket.save';
DELETE FROM RESOURCES WHERE NAME='Script.DiscountPercentLine';
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('35', 'Script.DiscountPercentLine', 0, $FILE{/com/openbravo/pos/templates/Script.DiscountPercentLine.txt});

-- final script

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.40.4' WHERE ID = 'w4cashdb';
