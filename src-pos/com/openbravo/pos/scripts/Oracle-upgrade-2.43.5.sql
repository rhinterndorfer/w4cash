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

-- Database upgrade script for ORACLE 2.43.5( -> 2.43.6)


-- --------------------------
-- branch tables
-- --------------------------
ALTER TABLE CLOSEDCASH ADD BRANCH_HOSTSEQUENCE INTEGER;

CREATE TABLE BRANCH_RECEIPTS (
    ID VARCHAR2(256) NOT NULL,
    MONEY VARCHAR2(256) NOT NULL,
    DATENEW TIMESTAMP NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY(ID),
    CONSTRAINT BR_RECEIPTS_FK_MONEY FOREIGN KEY (MONEY) REFERENCES CLOSEDCASH(MONEY)
);
CREATE INDEX BR_RECEIPTS_INX_1 ON BRANCH_RECEIPTS(DATENEW);

CREATE TABLE BRANCH_TICKETS (
    ID VARCHAR2(256) NOT NULL,
    TICKETTYPE INTEGER DEFAULT 0 NOT NULL,
    TICKETID INTEGER NOT NULL,
    PERSON VARCHAR2(256) NOT NULL,
    CUSTOMER VARCHAR2(256),
    STATUS INTEGER DEFAULT 0 NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT BR_TICKETS_FK_ID FOREIGN KEY (ID) REFERENCES BRANCH_RECEIPTS(ID),
    CONSTRAINT BR_TICKETS_FK_2 FOREIGN KEY (PERSON) REFERENCES PEOPLE(ID),
    CONSTRAINT BR_TICKETS_CUSTOMERS_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);
CREATE INDEX BR_TICKETS_TICKETID ON BRANCH_TICKETS(TICKETTYPE, TICKETID);

CREATE TABLE BRANCH_TICKETLINES (
    TICKET VARCHAR2(256) NOT NULL,
    LINE INTEGER NOT NULL,
    PRODUCT VARCHAR2(256),
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    PRICE DOUBLE PRECISION NOT NULL,
    TAXID VARCHAR2(256) NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY (TICKET, LINE),
    CONSTRAINT BR_TICKETLINES_FK_TICKET FOREIGN KEY (TICKET) REFERENCES BRANCH_TICKETS(ID),
    CONSTRAINT BR_TICKETLINES_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT BR_TICKETLINES_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT BR_TICKETLINES_FK_3 FOREIGN KEY (TAXID) REFERENCES TAXES(ID),
    UNIT VARCHAR2(25),
    ATTR1 VARCHAR2(25),
    ATTR2 VARCHAR2(25),
    ATTR3 VARCHAR2(25),
    ATTR4 VARCHAR2(25)
);

CREATE TABLE BRANCH_PAYMENTS (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    PAYMENT VARCHAR2(1024) NOT NULL,
    TOTAL DOUBLE PRECISION NOT NULL,
    TRANSID VARCHAR2(1024),
    RETURNMSG BLOB,
    DESCRIPTION VARCHAR(1024),
    PRIMARY KEY (ID),
    CONSTRAINT BR_PAYMENTS_FK_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES BRANCH_RECEIPTS(ID)
);
CREATE INDEX BR_PAYMENTS_INX_1 ON BRANCH_PAYMENTS(PAYMENT);

CREATE TABLE BRANCH_TAXLINES (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    TAXID VARCHAR2(256) NOT NULL, 
    BASE DOUBLE PRECISION NOT NULL, 
    AMOUNT DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT BR_TAXLINES_TAX FOREIGN KEY (TAXID) REFERENCES TAXES(ID),
    CONSTRAINT BR_TAXLINES_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES BRANCH_RECEIPTS(ID)
);






UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
UPDATE APPLICATIONS SET NAME = 'w4cashdb', VERSION = '2.43.6' WHERE ID = 'w4cashdb';
