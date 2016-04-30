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

-- Database upgrade script for ORACLE 2.42.6( -> 2.42.7)

-- role update
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Administrator.xml} WHERE ID='0';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Manager.xml} WHERE ID='1';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Employee.xml} WHERE ID='2';
UPDATE ROLES SET PERMISSIONS=$FILE{/com/openbravo/pos/templates/Role.Guest.xml} WHERE ID='3';

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('50', 'Script.Note', 0, $FILE{/com/openbravo/pos/templates/Script.Note.txt});
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.57mm.xml} WHERE NAME='Printer.TicketPreview.57mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.80mm.xml} WHERE NAME='Printer.TicketPreview.80mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.A4.xml} WHERE NAME='Printer.TicketPreview.A4';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.57mm.xml} WHERE NAME='Printer.Ticket.57mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.80mm.xml} WHERE NAME='Printer.Ticket.80mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.A4.xml} WHERE NAME='Printer.Ticket.A4';

ALTER TABLE CLOSEDCASH ADD LOCATION VARCHAR2(256);

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.42.7' WHERE ID = 'w4cashdb';
