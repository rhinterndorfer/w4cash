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

-- Database upgrade script for ORACLE 2.45.5 -> 2.45.6

INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('004', '5%');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('005', '19%');
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('004', '5%', '004', NULL, NULL, 0.05, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('005', '19%', '005', NULL, NULL, 0.19, 0, NULL);

UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.57mm.xml} WHERE NAME='Printer.TicketPreview.57mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.80mm.xml} WHERE NAME='Printer.TicketPreview.80mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.A4.xml} WHERE NAME='Printer.TicketPreview.A4';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.57mm.xml} WHERE NAME='Printer.Ticket.57mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.80mm.xml} WHERE NAME='Printer.Ticket.80mm';
UPDATE RESOURCES SET CONTENT= $FILE{/com/openbravo/pos/templates/Printer.Ticket.A4.xml} WHERE NAME='Printer.Ticket.A4';

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{} AND DBServerName = SYS_CONTEXT('USERENV', 'SERVER_HOST');
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.45.6' WHERE ID = 'w4cashdb' AND DBServerName = SYS_CONTEXT('USERENV', 'SERVER_HOST');

INSERT INTO LOGS (logtime, loghost, loglevel, logmessage) 
values (sysdate, SYS_CONTEXT('USERENV', 'SERVER_HOST'), 'INFO', 'Update to version ' || $APP_VERSION{});
