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

-- Database upgrade script for ORACLE 2.43.0( -> 2.43.1)

CREATE TABLE PRODUCTS_CODES (
    PRODUCT VARCHAR2(256) NOT NULL,
    CODE VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (CODE),
    CONSTRAINT PRODUCTS_CODES_FK_PRODUCT FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID)
);

CREATE TABLE PRICEZONES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    ISACTIV NUMERIC(1) DEFAULT 1 NOT NULL,
    ISCUSTOMER NUMERIC(1) DEFAULT 0 NOT NULL,
    DATEFROM TIMESTAMP,
    DATETILL TIMESTAMP,
    LOCATION VARCHAR2(256),
    PRIMARY KEY (ID),
    CONSTRAINT PRICEZONES_FK_LOCATIONS FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);

CREATE TABLE PRICEZONES_PRICES (
    PRICEZONE VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(1024) NOT NULL,
    PRICESELL DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (PRICEZONE, PRODUCT),
    CONSTRAINT PRICEZONES_PRICES_FK_PRODUCT FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PRICEZONES_PRICES_FK_RICEZONES FOREIGN KEY (PRICEZONE) REFERENCES PRICEZONES(ID)
);
CREATE INDEX PRICEZONES_PRICES_INX_PRODUCT ON PRICEZONES_PRICES(PRODUCT);
CREATE INDEX PRICEZONES_PRICES_INX_PRICEZO ON PRICEZONES_PRICES(PRICEZONE);

ALTER TABLE CUSTOMERS ADD PRICES_ZONE VARCHAR2(256);

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.43.1' WHERE ID = 'w4cashdb';
