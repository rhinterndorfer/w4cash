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

-- Database upgrade script for ORACLE 2.44.4( -> 2.44.5)


CREATE TABLE RESERVATION_PLACES (
    ID VARCHAR2(256) NOT NULL,
    PLACE VARCHAR2(256) NOT NULL,
    CONSTRAINT RES_PLACE_FK_1 FOREIGN KEY (ID) REFERENCES RESERVATIONS(ID),
    CONSTRAINT RES_PLACE_FK_2 FOREIGN KEY (PLACE) REFERENCES PLACES(ID)
);

ALTER TABLE RESERVATIONS ADD DATETILL TIMESTAMP DEFAULT TO_DATE('2999-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL;

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.44.5' WHERE ID = 'w4cashdb';
