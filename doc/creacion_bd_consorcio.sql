

CREATE TABLE Tarjeta(
	codTarjeta VARCHAR(11) NOT NULL,
	tagastoOffline INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT ta_pk PRIMARY KEY (codTarjeta)
);


CREATE TABLE Cuenta(
	codCuenta INTEGER NOT NULL,
	cusaldo INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT cu_pk PRIMARY KEY (codCuenta)
);


CREATE TABLE CuentaTarjeta(
	codTarjeta CHAR(11),
	cnum INTEGER,
	codCuenta INTEGER,
	CONSTRAINT ct_codCuenta_fk FOREIGN KEY (codCuenta) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT ct_codTarjeta_fk FOREIGN KEY (codTarjeta) REFERENCES Tarjeta(codTarjeta) ON DELETE CASCADE,
	CONSTRAINT ct_cnum_check CHECK (cnum IN (1,3)),
	CONSTRAINT ct_pk PRIMARY KEY (codTarjeta,cnum)
);


CREATE TABLE TipoMovimiento(
	codTMovimiento INTEGER NOT NULL,
	tmnombre  CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT tm_pk PRIMARY KEY (codTMovimiento)
);


CREATE TABLE EstadoBanco(
	codEBanco INTEGER NOT NULL,
	ebnombre CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT eb_pk PRIMARY KEY (codEBanco)
);


CREATE TABLE Banco(
	codigo INTEGER NOT NULL AUTO_INCREMENT,
	codBanco VARCHAR(20) NOT NULL UNIQUE,
	codEBanco INTEGER DEFAULT 2,
	bapuerto INTEGER,
	baip VARCHAR(20),
	bamaxCanales INTEGER NOT NULL DEFAULT 0 ,
	balastChannelUsed INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT ba_codEBanco_fk FOREIGN KEY (codEBanco) REFERENCES EstadoBanco(codEBanco) ON DELETE SET NULL,
	CONSTRAINT ba_pk PRIMARY KEY (codigo)
);


CREATE TABLE Cajero(
	codCajero INTEGER NOT NULL AUTO_INCREMENT,
	cajNombre VARCHAR(20) NOT NULL UNIQUE,
	cajIp VARCHAR(20) NOT NULL,
	cajPuerto INTEGER NOT NULL,

	CONSTRAINT caj_pk PRIMARY KEY (codCajero)
);


CREATE TABLE Movimiento(
	codMovimiento INTEGER NOT NULL AUTO_INCREMENT,
	codTarjeta VARCHAR(11) NOT NULL,
	codCuentaOrig INTEGER,
	codCuentaDest INTEGER NOT NULL,
	codTMovimiento INTEGER DEFAULT 99,
	mofecha DATE,
	moimporte INTEGER NOT NULL DEFAULT 0,
	moonline BOOLEAN NOT NULL DEFAULT 0,
	codBanco INTEGER NOT NULL,
	CONSTRAINT mo_codTarjeta_fk FOREIGN KEY (codTarjeta) REFERENCES Tarjeta(codTarjeta) ON DELETE CASCADE,
	CONSTRAINT mo_codCuentaOrig_fk FOREIGN KEY (codCuentaOrig) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT mo_codCuentaDest_fk FOREIGN KEY (codCuentaDest) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT mo_codTMovimiento_fk FOREIGN KEY (codTMovimiento) REFERENCES TipoMovimiento(codTMovimiento) ON DELETE SET NULL,
	CONSTRAINT mo_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,
	CONSTRAINT mo_pk PRIMARY KEY (codMovimiento)
);


CREATE TABLE TipoOrigDest(
	codTOrigDest INTEGER NOT NULL,
	todnombre CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT tod_pk PRIMARY KEY (codTOrigDest)
);


CREATE TABLE Mensaje(
	codMensaje INTEGER NOT NULL AUTO_INCREMENT,
	meNumMensaje INTEGER DEFAULT NULL,
	codTOrigen INTEGER,
	meorigen VARCHAR(30) NOT NULL,
	codTDestino INTEGER,
	medestino VARCHAR(30) NOT NULL,
	codBanco INTEGER,
	meonline BOOLEAN DEFAULT NULL,
	mestringMensaje VARCHAR(500) NOT NULL,
	CONSTRAINT me_codTOrigen_fk FOREIGN KEY (codTOrigen) REFERENCES TipoOrigDest(codTOrigDest) ON DELETE SET NULL,
	CONSTRAINT me_codTDestino_fk FOREIGN KEY (codTDestino) REFERENCES TipoOrigDest(codTOrigDest) ON DELETE SET NULL,
	CONSTRAINT me_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,
	CONSTRAINT me_pk PRIMARY KEY (codMensaje)
);


CREATE TABLE UltimoEnvio(
	codigoue INTEGER NOT NULL AUTO_INCREMENT,
	ueNumUltimoEnvio INTEGER DEFAULT NULL,
	uecontestado BOOLEAN NOT NULL DEFAULT 1,
	uecodCajero INTEGER,
	codBanco INTEGER NOT NULL,
	codTarjeta VARCHAR(11),
	codCuenta INTEGER,
	uestringMensaje VARCHAR(500) NOT NULL,
	CONSTRAINT ue_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,
	CONSTRAINT ue_Tarjeta_fk FOREIGN KEY (codTarjeta) REFERENCES Tarjeta(codTarjeta) ON DELETE CASCADE,
	CONSTRAINT ue_Cuenta_fk FOREIGN KEY (codCuenta) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT ue_codCajero_fk FOREIGN KEY (uecodCajero) REFERENCES Cajero(codCajero) ON DELETE CASCADE,
	CONSTRAINT ue_pk PRIMARY KEY (codigoue)
);


CREATE TABLE Canal(
	codBanco INTEGER NOT NULL,
	codCanal INTEGER NOT NULL,
	cabloqueado BOOLEAN NOT NULL DEFAULT 0,
	caenRecuperacion BOOLEAN NOT NULL DEFAULT 0,
	codUltimoEnvio INTEGER DEFAULT NULL,
	canext_numMensaje INTEGER NOT NULL DEFAULT 1,
	CONSTRAINT ca_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,
	CONSTRAINT ca_codUltimoEnvio_fk FOREIGN KEY (codUltimoEnvio) REFERENCES UltimoEnvio(codigoue) ON DELETE SET NULL,
	CONSTRAINT ca_pk PRIMARY KEY (codBanco,codCanal)
);


-- DATOS INICIALES REALES

INSERT INTO EstadoBanco VALUES(1,'Abierta');
INSERT INTO EstadoBanco VALUES(2,'Cerrada');
INSERT INTO EstadoBanco VALUES(3,'Trafico detenido');
INSERT INTO EstadoBanco VALUES(4,'En recuperacion');

INSERT INTO TipoMovimiento VALUES(10,'Reintegro');
INSERT INTO TipoMovimiento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovimiento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovimiento VALUES(13,'Pago recibo');
INSERT INTO TipoMovimiento VALUES(50,'Abono');
INSERT INTO TipoMovimiento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovimiento VALUES(99,'Otros');

INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (1,'Banco');
INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (2,'Consorcio');
INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (3,'Cajero');


INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES('pastor42',2,80,'127.0.0.1',3);

INSERT INTO Cajero(cajNombre,cajIp,cajPuerto) VALUES ('cajero01','127.0.0.1',8928);


INSERT INTO Tarjeta VALUES ('pastor42001',0);
INSERT INTO Tarjeta VALUES ('pastor42002',0);
INSERT INTO Tarjeta VALUES ('pastor42003',0);
INSERT INTO Tarjeta VALUES ('pastor42004',0);
INSERT INTO Tarjeta VALUES ('pastor42005',0);


INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (0,0000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (1,1000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (2,2000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (3,3000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (4,4000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (5,5000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (6,6000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (7,7000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (8,8000);
INSERT INTO Cuenta(codCuenta,cusaldo) VALUES (9,9000);

INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42001',1,1);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42001',2,2);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42001',3,3);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42002',1,1);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42002',2,3);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42003',1,4);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42003',2,5);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42003',3,6);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42004',1,7);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42004',2,8);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42005',1,0);
INSERT INTO CuentaTarjeta(codTarjeta,cnum,codCuenta)  VALUES('pastor42005',3,9);








