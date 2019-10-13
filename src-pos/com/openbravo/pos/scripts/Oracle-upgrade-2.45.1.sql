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

-- Database upgrade script for ORACLE 2.45.1( -> 2.45.2)

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{} AND DBServerName = SYS_CONTEXT('USERENV', 'SERVER_HOST');
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.45.2' WHERE ID = 'w4cashdb' AND DBServerName = SYS_CONTEXT('USERENV', 'SERVER_HOST');

INSERT INTO LOGS (logtime, loghost, loglevel, logmessage) 
values (sysdate, SYS_CONTEXT('USERENV', 'SERVER_HOST'), 'INFO', 'Update to version ' || '2.45.2');
