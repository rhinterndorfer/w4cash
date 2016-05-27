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

-- Database upgrade script for ORACLE 2.43.2( -> 2.43.3)

DROP TABLE PRICEZONES_PRICES;

CREATE TABLE PRICEZONES_PRICES (
    ID VARCHAR2(256) NOT NULL,
    PRICEZONE VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(1024) NOT NULL,
    PRICESELLGROSS DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PRICEZONES_PRICES_FK_PRODUCT FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PRICEZONES_PRICES_FK_RICEZONES FOREIGN KEY (PRICEZONE) REFERENCES PRICEZONES(ID)
);
CREATE UNIQUE INDEX PRICEZONES_PRICES_INX_ZONEPRO ON PRICEZONES_PRICES(PRODUCT, PRICEZONE);
CREATE INDEX PRICEZONES_PRICES_INX_PRODUCT ON PRICEZONES_PRICES(PRODUCT);
CREATE INDEX PRICEZONES_PRICES_INX_PRICEZO ON PRICEZONES_PRICES(PRICEZONE);


UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.43.3' WHERE ID = 'w4cashdb';
