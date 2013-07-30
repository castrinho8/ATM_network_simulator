CREATE TABLE Tarxeta(
	tcod VARCHAR(11),
	CONSTRAINT t_pk PRIMARY KEY (tcod)
);

CREATE TABLE Conta(
	ccod INTEGER,
	saldo INTEGER,
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
	importe INTEGER,
	data DATETIME,
	tmcod INTEGER,
	CONSTRAINT m_ccod_fk FOREIGN KEY (ccod) REFERENCES Conta(ccod) ON DELETE CASCADE,
	CONSTRAINT m_tmcod_fk FOREIGN KEY (tmcod) REFERENCES TipoMovemento(tmcod) ON DELETE SET NULL,
	CONSTRAINT m_pk PRIMARY KEY (ccod,mcod)
);

CREATE TABLE Execucion(
	ecod INTEGER AUTO_INCREMENT,
	CONSTRAINT e_pk PRIMARY KEY (ecod)
);


CREATE TABLE Sesion(
	scod INTEGER AUTO_INCREMENT,
	treintegros INTEGER DEFAULT 0,
	tabono INTEGER DEFAULT 0,
	ttraspasos INTEGER DEFAULT 0,
	CONSTRAINT s_pk PRIMARY KEY (scod)
);

CREATE TABLE Canle(
	scod INTEGER,
	cncod INTEGER,
	lastmsg INTEGER DEFAULT NULL,
	ocupado BOOLEAN DEFAULT 0,
	recuperado BOOLEAN DEFAULT 1,
	CONSTRAINT cn_scod_fk FOREIGN KEY (scod) REFERENCES Sesion(scod) ON DELETE CASCADE,
	CONSTRAINT s_pk PRIMARY KEY (scod,cncod)	
);

CREATE TABLE Mensaxe (
	mscod INTEGER AUTO_INCREMENT,
	scod INTEGER,
	cncod INTEGER,
	msnum INTEGER,
	tipo VARCHAR(20),
	enviado BOOLEAN,
	texto VARCHAR(100),
	ecod INTEGER NOT NULL,
	CONSTRAINT ms_pk PRIMARY KEY (mscod),
	CONSTRAINT ms_fk_canle FOREIGN KEY (scod,cncod) REFERENCES Canle(scod,cncod) ON DELETE CASCADE,
	CONSTRAINT ms_fk_execucion FOREIGN KEY (ecod) REFERENCES Execucion(ecod) ON DELETE CASCADE
);





-- Valores iniciales das contas do banco.

INSERT INTO Tarxeta VALUES ('pastor42001');
INSERT INTO Tarxeta VALUES ('pastor42002');
INSERT INTO Tarxeta VALUES ('pastor42003');
INSERT INTO Tarxeta VALUES ('pastor42004');
INSERT INTO Tarxeta VALUES ('pastor42005');

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

INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42001',1,1);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42001',2,2);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42001',3,3);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42002',1,1);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42002',2,3);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42003',1,4);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42003',2,5);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42003',3,6);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42004',1,7);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42004',2,8);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42005',1,0);
INSERT INTO ContaTarxeta(tcod,cnum,ccod)  VALUES('pastor42005',3,9);


-- Tipos de movimento.

INSERT INTO TipoMovemento VALUES(10,'Reintegro');
INSERT INTO TipoMovemento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovemento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovemento VALUES(13,'Pago recibo');
INSERT INTO TipoMovemento VALUES(50,'Abono');
INSERT INTO TipoMovemento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovemento VALUES(99,'Otros');


SELECT cncod canle, max(msnum) ultmsx
FROM Canle LEFT JOIN Mensaxe USING (cncod)
GROUP BY cncod
WHERE scod = 1
ORDER BY 1
