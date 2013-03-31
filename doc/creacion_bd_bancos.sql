CREATE TABLE Tarxeta(
	tcod INTEGER,
	CONSTRAINT t_pk PRIMARY KEY (tcod)
);

CREATE TABLE Conta(
	ccod INTEGER,
	saldo DECIMAL(10,2),
	CONSTRAINT c_pk PRIMARY KEY (ccod)
);

CREATE TABLE ContaTarxeta(
	ccod INTEGER,
	tcod INTEGER,
	CONSTRAINT ct_ccod_fk FOREIGN KEY (ccod) REFERENCES Conta(ccod) ON DELETE CASCADE,
	CONSTRAINT ct_tcod_fk FOREIGN KEY (tcod) REFERENCES Tarxeta(tcod) ON DELETE CASCADE,
	CONSTRAINT ct_pk PRIMARY KEY (ccod,tcod)
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

INSERT INTO Tarxeta VALUES (1);
INSERT INTO Tarxeta VALUES (2);
INSERT INTO Tarxeta VALUES (3);
INSERT INTO Tarxeta VALUES (4);
INSERT INTO Tarxeta VALUES (5);

INSERT INTO Conta VALUES (0,0000);
INSERT INTO Conta VALUES (1,1000);
INSERT INTO Conta VALUES (2,2000);
INSERT INTO Conta VALUES (3,3000);
INSERT INTO Conta VALUES (4,4000);
INSERT INTO Conta VALUES (5,5000);
INSERT INTO Conta VALUES (6,6000);
INSERT INTO Conta VALUES (7,7000);
INSERT INTO Conta VALUES (8,8000);
INSERT INTO Conta VALUES (9,9000);

INSERT INTO ContaTarxeta VALUES(1,1);
INSERT INTO ContaTarxeta VALUES(2,1);
INSERT INTO ContaTarxeta VALUES(3,1);
INSERT INTO ContaTarxeta VALUES(1,2);
INSERT INTO ContaTarxeta VALUES(3,2);
INSERT INTO ContaTarxeta VALUES(4,3);
INSERT INTO ContaTarxeta VALUES(5,3);
INSERT INTO ContaTarxeta VALUES(6,3);
INSERT INTO ContaTarxeta VALUES(7,4);
INSERT INTO ContaTarxeta VALUES(8,4);
INSERT INTO ContaTarxeta VALUES(0,5);
INSERT INTO ContaTarxeta VALUES(9,5);


-- Tipos de movimento.

INSERT INTO TipoMovemento VALUES(10,'Reintegro');
INSERT INTO TipoMovemento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovemento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovemento VALUES(13,'Pago recibo');
INSERT INTO TipoMovemento VALUES(50,'Abono');
INSERT INTO TipoMovemento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovemento VALUES(99,'Otros');
