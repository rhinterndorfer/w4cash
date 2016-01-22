--    Openbravo POS is a point of sales application designed for touch screens.
--    Copyright (C) 2007-2010 Openbravo, S.L.
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

-- Database script for ORACLE
-- V2.41.6 (set in line 30!!!)

CREATE TABLE APPLICATIONS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    VERSION VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
INSERT INTO APPLICATIONS(ID, NAME, VERSION) VALUES($APP_ID{}, $APP_NAME{}, $APP_VERSION{});
INSERT INTO APPLICATIONS(ID, NAME, VERSION) VALUES('w4cashdb', 'w4cashdb', '2.41.6');

CREATE TABLE ROLES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PERMISSIONS BLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX ROLES_NAME_INX ON ROLES(NAME);
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('0', 'Administratoren', $FILE{/com/openbravo/pos/templates/Role.Administrator.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('1', 'Manager', $FILE{/com/openbravo/pos/templates/Role.Manager.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('2', 'Angestellte', $FILE{/com/openbravo/pos/templates/Role.Employee.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('3', 'Aushilfen', $FILE{/com/openbravo/pos/templates/Role.Guest.xml} );

CREATE TABLE PEOPLE (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    APPPASSWORD VARCHAR2(1024),
    CARD VARCHAR2(1024),
    ROLE VARCHAR2(256) NOT NULL,
    VISIBLE NUMERIC(1) NOT NULL,
    IMAGE BLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PEOPLE_FK_1 FOREIGN KEY (ROLE) REFERENCES ROLES(ID)
);
CREATE UNIQUE INDEX PEOPLE_NAME_INX ON PEOPLE(NAME);
CREATE INDEX PEOPLE_CARD_INX ON PEOPLE(CARD);

INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('0', 'Administrator', NULL, '0', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('1', 'Manager', NULL, '1', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('2', 'Kellner', NULL, '2', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('3', 'Aushilfe', NULL, '3', 1, NULL);

CREATE TABLE RESOURCES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    RESTYPE INTEGER NOT NULL,
    CONTENT BLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX RESOURCES_NAME_INX ON RESOURCES(NAME);
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('0', 'Printer.Start', 0, $FILE{/com/openbravo/pos/templates/Printer.Start.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('1', 'Printer.Ticket', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('2', 'Printer.Ticket2', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('3', 'Printer.TicketPreview', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('4', 'Printer.TicketTotal', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketTotal.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('5', 'Printer.OpenDrawer', 0, $FILE{/com/openbravo/pos/templates/Printer.OpenDrawer.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('6', 'Printer.Ticket.Logo', 1, $FILE{/com/openbravo/pos/templates/Printer.Ticket.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('7', 'Printer.TicketLine', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketLine.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('8', 'Printer.CloseCash', 0, $FILE{/com/openbravo/pos/templates/Printer.CloseCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('9', 'Window.Logo', 1, $FILE{/com/openbravo/pos/templates/Window.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('10', 'Window.Title', 0, $FILE{/com/openbravo/pos/templates/Window.Title.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('11', 'Ticket.Buttons', 0, $FILE{/com/openbravo/pos/templates/Ticket.Buttons.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('12', 'Ticket.Line', 0, $FILE{/com/openbravo/pos/templates/Ticket.Line.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('13', 'Printer.Inventory', 0, $FILE{/com/openbravo/pos/templates/Printer.Inventory.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('14', 'Menu.Root', 0, $FILE{/com/openbravo/pos/templates/Menu.Root.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('15', 'Printer.CustomerPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('16', 'Printer.CustomerPaid2', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('17', 'payment.cash', 0, $FILE{/com/openbravo/pos/templates/payment.cash.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('18', 'banknote.50euro', 1, $FILE{/com/openbravo/pos/templates/banknote.50euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('19', 'banknote.20euro', 1, $FILE{/com/openbravo/pos/templates/banknote.20euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('20', 'banknote.10euro', 1, $FILE{/com/openbravo/pos/templates/banknote.10euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('21', 'banknote.5euro', 1, $FILE{/com/openbravo/pos/templates/banknote.5euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('22', 'coin.2euro', 1, $FILE{/com/openbravo/pos/templates/coin.2euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('23', 'coin.1euro', 1, $FILE{/com/openbravo/pos/templates/coin.1euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('24', 'coin.50cent', 1, $FILE{/com/openbravo/pos/templates/coin.50cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('25', 'coin.20cent', 1, $FILE{/com/openbravo/pos/templates/coin.20cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('26', 'coin.10cent', 1, $FILE{/com/openbravo/pos/templates/coin.10cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('27', 'coin.5cent', 1, $FILE{/com/openbravo/pos/templates/coin.5cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('28', 'coin.2cent', 1, $FILE{/com/openbravo/pos/templates/coin.2cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('29', 'coin.1cent', 1, $FILE{/com/openbravo/pos/templates/coin.1cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('30', 'Printer.PartialCash', 0, $FILE{/com/openbravo/pos/templates/Printer.PartialCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('31', 'ticket.save', 0, $FILE{/com/openbravo/pos/templates/ticket.save.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('32', 'ticket.addline', 0, $FILE{/com/openbravo/pos/templates/ticket.addline.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('33', 'ticket.close', 0, $FILE{/com/openbravo/pos/templates/ticket.close.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('34', 'Reports.Address', 0, $FILE{/com/openbravo/pos/templates/reports.address.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('35', 'Script.DiscountPercentLine', 0, $FILE{/com/openbravo/pos/templates/Script.DiscountPercentLine.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('36', 'Window.Logout', 0, $FILE{/com/openbravo/pos/templates/Window.Logout.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('37', 'Printer.TicketPreview.80mm', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.80mm.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('38', 'Printer.TicketPreview.57mm', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.57mm.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('39', 'Printer.TicketPreview.A4', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.A4.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('40', 'Printer.Ticket.80mm', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.80mm.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('41', 'Printer.Ticket.57mm', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.57mm.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('42', 'Printer.Ticket.A4', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.A4.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('43', 'Printer.AdditionalPrinter', 0, $FILE{/com/openbravo/pos/templates/Printer.AdditionalPrinter.xml});

CREATE TABLE TAXCUSTCATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX TAXCUSTCAT_NAME_INX ON TAXCUSTCATEGORIES(NAME);

CREATE TABLE CUSTOMERS (
    ID VARCHAR2(256) NOT NULL,
    SEARCHKEY VARCHAR2(1024) NOT NULL,
    TAXID VARCHAR2(1024),
    NAME VARCHAR2(1024) NOT NULL,
    TAXCATEGORY VARCHAR2(256),
    CARD VARCHAR2(1024),
    MAXDEBT DOUBLE PRECISION DEFAULT 0 NOT NULL,
    ADDRESS VARCHAR2(1024),
    ADDRESS2 VARCHAR2(1024),
    POSTAL VARCHAR2(1024),
    CITY VARCHAR2(1024),
    REGION VARCHAR2(1024),
    COUNTRY VARCHAR2(1024),
    FIRSTNAME VARCHAR2(1024),
    LASTNAME VARCHAR2(1024),
    EMAIL VARCHAR2(1024),
    PHONE VARCHAR2(1024),
    PHONE2 VARCHAR2(1024),
    FAX VARCHAR2(1024),
    NOTES VARCHAR2(1024),
    VISIBLE NUMERIC(1) DEFAULT 1 NOT NULL,
    CURDATE TIMESTAMP,
    CURDEBT DOUBLE PRECISION,
    PRIMARY KEY (ID),
    CONSTRAINT CUSTOMERS_TAXCAT FOREIGN KEY (TAXCATEGORY) REFERENCES TAXCUSTCATEGORIES(ID)
);
CREATE UNIQUE INDEX CUSTOMERS_SKEY_INX ON CUSTOMERS(SEARCHKEY);
CREATE INDEX CUSTOMERS_TAXID_INX ON CUSTOMERS(TAXID);
CREATE INDEX CUSTOMERS_NAME_INX ON CUSTOMERS(NAME);
CREATE INDEX CUSTOMERS_CARD_INX ON CUSTOMERS(CARD);

CREATE TABLE CATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PARENTID VARCHAR2(256),
    IMAGE BLOB,
    SORTORDER INT,
    PRINTER INT DEFAULT -1,
    PRIMARY KEY(ID),
    CONSTRAINT CATEGORIES_FK_1 FOREIGN KEY (PARENTID) REFERENCES CATEGORIES(ID)
);
CREATE UNIQUE INDEX CATEGORIES_NAME_INX ON CATEGORIES(NAME);

CREATE TABLE TAXCATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX TAXCAT_NAME_INX ON TAXCATEGORIES(NAME);
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('000', '0%');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('001', '10%');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('002', '13%');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('003', '20%');

CREATE TABLE TAXES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    VALIDFROM TIMESTAMP DEFAULT TO_DATE('2001-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL,
    CATEGORY VARCHAR2(256) NOT NULL,
    CUSTCATEGORY VARCHAR2(256),
    PARENTID VARCHAR2(256),
    RATE DOUBLE PRECISION NOT NULL,
    RATECASCADE NUMERIC(1) DEFAULT 0 NOT NULL,
    RATEORDER INTEGER,
    PRIMARY KEY(ID),
    CONSTRAINT TAXES_CAT_FK FOREIGN KEY (CATEGORY) REFERENCES TAXCATEGORIES(ID),
    CONSTRAINT TAXES_CUSTCAT_FK FOREIGN KEY (CUSTCATEGORY) REFERENCES TAXCUSTCATEGORIES(ID),
    CONSTRAINT TAXES_TAXES_FK FOREIGN KEY (PARENTID) REFERENCES TAXES(ID)
);
CREATE UNIQUE INDEX TAXES_NAME_INX ON TAXES(NAME);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('000', '0%', '000', NULL, NULL, 0, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('001', '10%', '001', NULL, NULL, 0.10, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('002', '13%', '002', NULL, NULL, 0.13, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('003', '20%', '003', NULL, NULL, 0.20, 0, NULL);

CREATE TABLE ATTRIBUTE (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ATTRIBUTEVALUE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTE_ID VARCHAR2(256) NOT NULL,
    VALUE VARCHAR2(1024),
    PRIMARY KEY (ID),
    CONSTRAINT ATTVAL_ATT FOREIGN KEY (ATTRIBUTE_ID) REFERENCES ATTRIBUTE(ID) ON DELETE CASCADE
);

CREATE TABLE ATTRIBUTESET (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ATTRIBUTEUSE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256) NOT NULL,
    ATTRIBUTE_ID VARCHAR2(256) NOT NULL,
    LINENO INTEGER,
    PRIMARY KEY (ID),
    CONSTRAINT ATTUSE_SET FOREIGN KEY (ATTRIBUTESET_ID) REFERENCES ATTRIBUTESET(ID) ON DELETE CASCADE,
    CONSTRAINT ATTUSE_ATT FOREIGN KEY (ATTRIBUTE_ID) REFERENCES ATTRIBUTE(ID)
);
CREATE UNIQUE INDEX ATTUSE_LINE ON ATTRIBUTEUSE(ATTRIBUTESET_ID, LINENO);

CREATE TABLE ATTRIBUTESETINSTANCE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256),
    DESCRIPTION VARCHAR2(1024),
    PRIMARY KEY (ID)
);

CREATE TABLE ATTRIBUTEINSTANCE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    ATTRIBUTE_ID VARCHAR2(256),
    VALUE VARCHAR2(1024),
    PRIMARY KEY (ID)
);

CREATE TABLE PRODUCTS (
    ID VARCHAR2(256) NOT NULL,
    REFERENCE VARCHAR2(1024) NOT NULL,
    CODE VARCHAR2(1024) NOT NULL,
    CODETYPE VARCHAR2(1024),
    NAME VARCHAR2(1024) NOT NULL,
    PRICEBUY DOUBLE PRECISION NOT NULL,
    PRICESELL DOUBLE PRECISION NOT NULL,
    CATEGORY VARCHAR2(256) NOT NULL,
    TAXCAT VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256),
    STOCKCOST DOUBLE PRECISION,
    STOCKVOLUME DOUBLE PRECISION,
    IMAGE BLOB,
    ISCOM  NUMERIC(1) DEFAULT 0 NOT NULL,
    ISSCALE  NUMERIC(1) DEFAULT 0 NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PRODUCTS_FK_1 FOREIGN KEY (CATEGORY) REFERENCES CATEGORIES(ID),
    CONSTRAINT PRODUCTS_TAXCAT_FK FOREIGN KEY (TAXCAT) REFERENCES TAXCATEGORIES(ID),
    CONSTRAINT PRODUCTS_ATTRSET_FK FOREIGN KEY (ATTRIBUTESET_ID) REFERENCES ATTRIBUTESET(ID)
);
CREATE UNIQUE INDEX PRODUCTS_INX_0 ON PRODUCTS(REFERENCE);
CREATE UNIQUE INDEX PRODUCTS_INX_1 ON PRODUCTS(CODE);
CREATE UNIQUE INDEX PRODUCTS_NAME_INX ON PRODUCTS(NAME);

CREATE TABLE PRODUCTS_CAT (
    PRODUCT VARCHAR2(256) NOT NULL,
    CATORDER INTEGER,
    PRIMARY KEY (PRODUCT),
    CONSTRAINT PRODUCTS_CAT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID)
);
CREATE INDEX PRODUCTS_CAT_INX_1 ON PRODUCTS_CAT(CATORDER);

CREATE TABLE PRODUCTS_COM (
    ID VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    PRODUCT2 VARCHAR2(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PRODUCTS_COM_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PRODUCTS_COM_FK_2 FOREIGN KEY (PRODUCT2) REFERENCES PRODUCTS(ID)
);
CREATE UNIQUE INDEX PCOM_INX_PROD ON PRODUCTS_COM(PRODUCT, PRODUCT2);

CREATE TABLE LOCATIONS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    ADDRESS VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX LOCATIONS_NAME_INX ON LOCATIONS(NAME);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('0', 'Gaststube', NULL);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('1', 'Restaurant', NULL);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('2', 'Bar', NULL);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('3', 'Gastgarten', NULL);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('4', 'Festsaal', NULL);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('5', 'Festhalle', NULL);


CREATE TABLE STOCKDIARY (
    ID VARCHAR2(256) NOT NULL,
    DATENEW TIMESTAMP NOT NULL,
    REASON INTEGER NOT NULL,
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    PRICE DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT STOCKDIARY_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKDIARY_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT STOCKDIARY_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);
CREATE INDEX STOCKDIARY_INX_1 ON STOCKDIARY(DATENEW);

CREATE TABLE STOCKLEVEL (
    ID VARCHAR2(256) NOT NULL,
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    STOCKSECURITY DOUBLE PRECISION,
    STOCKMAXIMUM DOUBLE PRECISION,
    PRIMARY KEY (ID),
    CONSTRAINT STOCKLEVEL_PRODUCT FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKLEVEL_LOCATION FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);

CREATE TABLE STOCKCURRENT (
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    CONSTRAINT STOCKCURRENT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKCURRENT_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT STOCKCURRENT_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);
CREATE UNIQUE INDEX STOCKCURRENT_INX ON STOCKCURRENT(LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID);

CREATE TABLE CLOSEDCASH (
    MONEY VARCHAR2(256) NOT NULL,
    HOST VARCHAR2(1024) NOT NULL,
    HOSTSEQUENCE INTEGER NOT NULL,
    DATESTART TIMESTAMP NOT NULL,
    DATEEND TIMESTAMP,
    PRIMARY KEY(MONEY)
);
CREATE INDEX CLOSEDCASH_INX_1 ON CLOSEDCASH(DATESTART);
CREATE UNIQUE INDEX CLOSEDCASH_INX_SEQ2 ON CLOSEDCASH(HOSTSEQUENCE);

CREATE TABLE RECEIPTS (
    ID VARCHAR2(256) NOT NULL,
    MONEY VARCHAR2(256) NOT NULL,
    DATENEW TIMESTAMP NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY(ID),
    CONSTRAINT RECEIPTS_FK_MONEY FOREIGN KEY (MONEY) REFERENCES CLOSEDCASH(MONEY)
);
CREATE INDEX RECEIPTS_INX_1 ON RECEIPTS(DATENEW);

CREATE TABLE TICKETS (
    ID VARCHAR2(256) NOT NULL,
    TICKETTYPE INTEGER DEFAULT 0 NOT NULL,
    TICKETID INTEGER NOT NULL,
    PERSON VARCHAR2(256) NOT NULL,
    CUSTOMER VARCHAR2(256),
    STATUS INTEGER DEFAULT 0 NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT TICKETS_FK_ID FOREIGN KEY (ID) REFERENCES RECEIPTS(ID),
    CONSTRAINT TICKETS_FK_2 FOREIGN KEY (PERSON) REFERENCES PEOPLE(ID),
    CONSTRAINT TICKETS_CUSTOMERS_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);
CREATE INDEX TICKETS_TICKETID ON TICKETS(TICKETTYPE, TICKETID);

CREATE TABLE TICKETLINES (
    TICKET VARCHAR2(256) NOT NULL,
    LINE INTEGER NOT NULL,
    PRODUCT VARCHAR2(256),
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    PRICE DOUBLE PRECISION NOT NULL,
    TAXID VARCHAR2(256) NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY (TICKET, LINE),
    CONSTRAINT TICKETLINES_FK_TICKET FOREIGN KEY (TICKET) REFERENCES TICKETS(ID),
    CONSTRAINT TICKETLINES_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT TICKETLINES_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT TICKETLINES_FK_3 FOREIGN KEY (TAXID) REFERENCES TAXES(ID)
);

CREATE TABLE PAYMENTS (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    PAYMENT VARCHAR2(1024) NOT NULL,
    TOTAL DOUBLE PRECISION NOT NULL,
    TRANSID VARCHAR2(1024),
    RETURNMSG BLOB,
    DESCRIPTION VARCHAR(1024),
    PRIMARY KEY (ID),
    CONSTRAINT PAYMENTS_FK_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES RECEIPTS(ID)
);
CREATE INDEX PAYMENTS_INX_1 ON PAYMENTS(PAYMENT);

CREATE TABLE TAXLINES (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    TAXID VARCHAR2(256) NOT NULL, 
    BASE DOUBLE PRECISION NOT NULL, 
    AMOUNT DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT TAXLINES_TAX FOREIGN KEY (TAXID) REFERENCES TAXES(ID),
    CONSTRAINT TAXLINES_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES RECEIPTS(ID)
);

CREATE TABLE FLOORS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    IMAGE BLOB,
    SORTORDER INT,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX FLOORS_NAME_INX ON FLOORS(NAME);
INSERT INTO FLOORS(ID, NAME, IMAGE) VALUES ('0', 'Restaurant', $FILE{/com/openbravo/pos/templates/restaurantsample.png});

CREATE TABLE PLACES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL,
    FLOOR VARCHAR2(256) NOT NULL,
    WIDTH INT,
    HEIGHT INT,
    FONTSIZE INT,
    PRIMARY KEY (ID),
    CONSTRAINT PLACES_FK_1 FOREIGN KEY (FLOOR) REFERENCES FLOORS(ID)
);
CREATE UNIQUE INDEX PLACES_NAME_INX ON PLACES(NAME);
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('1', 'Tisch 1', 133, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('2', 'Tisch 2', 532, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('3', 'Tisch 3', 133, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('4', 'Tisch 4', 266, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('5', 'Tisch 5', 399, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('6', 'Tisch 6', 532, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('7', 'Tisch 7', 133, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('8', 'Tisch 8', 266, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('9', 'Tisch 9', 399, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('10', 'Tisch 10', 532, 377, '0');

CREATE TABLE RESERVATIONS (
    ID VARCHAR2(256) NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    DATENEW TIMESTAMP DEFAULT TO_DATE('2001-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL,
    TITLE VARCHAR2(1024) NOT NULL,
    CHAIRS INTEGER NOT NULL,
    ISDONE NUMERIC(1) NOT NULL,
    DESCRIPTION VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE INDEX RESERVATIONS_INX_1 ON RESERVATIONS(DATENEW);

CREATE TABLE RESERVATION_CUSTOMERS (
    ID VARCHAR2(256) NOT NULL,
    CUSTOMER VARCHAR2(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT RES_CUST_FK_1 FOREIGN KEY (ID) REFERENCES RESERVATIONS(ID),
    CONSTRAINT RES_CUST_FK_2 FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);

CREATE TABLE THIRDPARTIES (
    ID VARCHAR2(256) NOT NULL,
    CIF VARCHAR2(1024) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    ADDRESS VARCHAR2(1024),
    CONTACTCOMM VARCHAR2(1024),
    CONTACTFACT VARCHAR2(1024),
    PAYRULE VARCHAR2(1024),
    FAXNUMBER VARCHAR2(1024),
    PHONENUMBER VARCHAR2(1024),
    MOBILENUMBER VARCHAR2(1024),
    EMAIL VARCHAR2(1024),
    WEBPAGE VARCHAR2(1024),
    NOTES VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX THIRDPARTIES_CIF_INX ON THIRDPARTIES(CIF);
CREATE UNIQUE INDEX THIRDPARTIES_NAME_INX ON THIRDPARTIES(NAME);

CREATE TABLE SHAREDTICKETS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    CONTENT BLOB,
    PRIMARY KEY(ID)
);


CREATE TABLE DEVICE (
    ID VARCHAR2(255) NOT NULL,
    INSTALL VARCHAR2(255) NOT NULL,
    LICENSE VARCHAR(255)
);



