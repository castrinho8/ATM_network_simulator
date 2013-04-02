CREATE TABLE Tarxeta(
	tcod CHAR(11),
	CONSTRAINT t_pk PRIMARY KEY (tcod)
);

CREATE TABLE Conta(
	ccod INTEGER,
	saldo DECIMAL(10,2),
	CONSTRAINT c_pk PRIMARY KEY (ccod)
);

CREATE TABLE ContaTarxeta(
	tcod CHAR(11),
	cnum INTEGER,
	ccod INTEGER,
	CONSTRAINT ct_ccod_fk FOREIGN KEY (ccod) REFERENCES Conta(ccod) ON DELETE CASCADE,
	CONSTRAINT ct_tcod_fk FOREIGN KEY (tcod) REFERENCES Tarxeta(tcod) ON DELETE CASCADE,
	CONSTRAINT ct_cnum_check CHECK (cnum IN (1,3)),
	CONSTRAINT ct_pk PRIMARY KEY (tcod,cnum)
);

CREATE TABLE TipoMovemento(
	tmcod INTEGER,
	tmnome  CHAR(30) UNIQUE,
	CONSTRAINT tm_pk PRIMARY KEY (tmcod)
);

CREATE TABLE Movemento(
	ccod INTEGER,
	mcod INTEGER,
	importe DECIMAL(10,2),
	data DATE,
	tmcod INTEGER,
	CONSTRAINT m_ccod_fk FOREIGN KEY (ccod) REFERENCES Conta(ccod) ON DELETE CASCADE,
	CONSTRAINT m_tmcod_fk FOREIGN KEY (tmcod) REFERENCES TipoMovemento(tmcod) ON DELETE SET NULL,
	CONSTRAINT m_pk PRIMARY KEY (ccod,mcod)
);

-- Valores iniciales das contas do banco.

INSERT INTO Tarxeta VALUES ('pastor42 01');
INSERT INTO Tarxeta VALUES ('pastor42 02');
INSERT INTO Tarxeta VALUES ('pastor42 03');
INSERT INTO Tarxeta VALUES ('pastor42 04');
INSERT INTO Tarxeta VALUES ('pastor42 05');

INSERT INTO Conta(ccod,saldo) VALUES (0,0000);
INSERT INTO Conta(ccod,saldo) VALUES (1,1000);
INSERT INTO Conta(ccod,saldo) VALUES (2,2000);
INSERT INTO Conta(ccod,saldo) VALUES (3,3000);
INSERT INTO Conta(ccod,saldo) VALUES (4,4000);
INSERT INTO Conta(ccod,saldo) VALUES (5,5000);
INSERT INTO Conta(ccod,saldo) VALUES (6,6000);
INSERT INTO Conta(ccod,saldo) VALUES (7,7000);
INSERT INTO Conta(ccod,saldo) VALUES (8,8000);
INSERT INTO Conta(ccod,saldo) VALUES (9,9000);

INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',1,1);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',2,2);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 01',3,3);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 02',1,1);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 02',2,3);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',1,4);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',2,5);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 03',3,6);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 04',1,7);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 04',2,8);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 05',1,0);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42 05',3,9);


-- Tipos de movimento.

INSERT INTO TipoMovemento VALUES(10,'Reintegro');
INSERT INTO TipoMovemento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovemento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovemento VALUES(13,'Pago recibo');
INSERT INTO TipoMovemento VALUES(50,'Abono');
INSERT INTO TipoMovemento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovemento VALUES(99,'Otros');
