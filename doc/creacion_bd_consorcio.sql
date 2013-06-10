
-- tacod Codigo tarjeta
CREATE TABLE Tarjeta(
	codTarjeta VARCHAR(11),
	tagastoOffline INTEGER,
	CONSTRAINT ta_pk PRIMARY KEY (codTarjeta)
);


-- cucod Codigo cuenta
-- cusaldo Saldo cuenta
CREATE TABLE Cuenta(
	codTarjeta VARCHAR(11),
	codCuenta INTEGER,
	cusaldo INTEGER DEFAULT 0,
	CONSTRAINT ct_tcod_fk FOREIGN KEY (codTarjeta) REFERENCES Tarjeta(codTarjeta) ON DELETE CASCADE,
	CONSTRAINT cu_pk PRIMARY KEY (codCuenta,codTarjeta)
);

/*-- tacod Codigo Tarjeta
-- ctnum Numero entre 1 y 3 que indica las 3 cuentas de cada tarjeta
-- cucod Codigo de Cuenta
CREATE TABLE CuentaTarjeta(
	codTarjeta CHAR(11),
	ctnum INTEGER,
	codCuenta INTEGER,
	CONSTRAINT ct_codCuenta_fk FOREIGN KEY (codCuenta) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT ct_codTarjeta_fk FOREIGN KEY (codTarjeta) REFERENCES Tarjeta(codTarjeta) ON DELETE CASCADE,
	CONSTRAINT ct_ctnum_check CHECK (ctnum IN (1,3)),
	CONSTRAINT ct_pk PRIMARY KEY (codTarjeta,ctnum)
);
*/

-- tmcod Cogido de tipo de movimiento
-- tmnombre Nombre del tipo 
CREATE TABLE TipoMovimiento(
	codTMovimiento INTEGER,
	tmnombre  CHAR(30) UNIQUE,
	CONSTRAINT tm_pk PRIMARY KEY (codTMovimiento)
);


-- codEBanco Codigo del Estado del banco
-- ebnombre Nombre del estado del banco
CREATE TABLE EstadoBanco(
	codEBanco INTEGER,
	ebnombre CHAR(30) UNIQUE,
	CONSTRAINT eb_pk PRIMARY KEY (codEBanco)
);

-- codBanco Codigo del banco
-- codEBanco Codigo del Estado del banco
-- bapuerto El puerto del banco
-- baip La ip del banco
-- bamaxCanales El numero maximo de canales
CREATE TABLE Banco(
	codigo INTEGER AUTO_INCREMENT,	
	codBanco VARCHAR(20) UNIQUE,
	codEBanco INTEGER,
	bapuerto INTEGER,
	baip VARCHAR(20),
	bamaxCanales INTEGER,
	CONSTRAINT ba_codEBanco_fk FOREIGN KEY (codEBanco) REFERENCES EstadoBanco(codEBanco) ON DELETE SET NULL,
	CONSTRAINT ba_pk PRIMARY KEY (codigo)
);

-- codCuentaOrig Codigo cuenta origen
-- codCuentaDest Codigo cuenta destino
-- mocod COdigo movimiento
-- tmcod COdigo de tipo de movimiento
-- moimporte Importe del movimiento
-- mooffline Booleano que indica si es offline
CREATE TABLE Movimiento(
	codMovimiento INTEGER AUTO_INCREMENT,
	codTarjeta VARCHAR(11),
	codCuentaOrig INTEGER,
	codCuentaDest INTEGER,
	codTMovimiento INTEGER,
	mofecha DATE,
	moimporte INTEGER,
	mooffline BOOLEAN,
	codBanco INTEGER,
	CONSTRAINT mo_codCuentaOrig_fk FOREIGN KEY (codCuentaOrig,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE NO ACTION,
	CONSTRAINT mo_codCuentaDest_fk FOREIGN KEY (codCuentaDest,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE NO ACTION,

	CONSTRAINT mo_codTMovimiento_fk FOREIGN KEY (codTMovimiento) REFERENCES TipoMovimiento(codTMovimiento) ON DELETE SET NULL,

	CONSTRAINT mo_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE NO ACTION,

	CONSTRAINT mo_pk PRIMARY KEY (codMovimiento)
);


CREATE TABLE TipoOrigDest(
	codTOrigDest INTEGER,
	todnombre CHAR(30) UNIQUE,
	CONSTRAINT tod_pk PRIMARY KEY (codTOrigDest)
);


-- codMensaje El codigo del mensaje
-- meoffline Booleano que indica si es offline o no
-- mees_envio Booleano que indica si es envio o no
-- 
-- codBanco El codigo del banco correspondiente al mensaje
CREATE TABLE Mensaje(
	codMensaje INTEGER AUTO_INCREMENT,
	codTOrigen INTEGER,
	meorigen VARCHAR(30),
	codTDestino INTEGER,
	medestino VARCHAR(30),
	meoffline BOOLEAN DEFAULT 0,
	mestringMensaje VARCHAR(500),

	CONSTRAINT me_codTOrigen_fk FOREIGN KEY (codTOrigen) REFERENCES TipoOrigDest(codTOrigDest) ON DELETE SET NULL,
	CONSTRAINT me_codTDestino_fk FOREIGN KEY (codTDestino) REFERENCES TipoOrigDest(codTOrigDest) ON DELETE SET NULL,
	CONSTRAINT me_pk PRIMARY KEY (codMensaje)
);


--	++++++++++++++++ mensaje
CREATE TABLE UltimoEnvio(
	codigoue INTEGER AUTO_INCREMENT,
	codUltimoEnvio INTEGER,
	uecontestado BOOLEAN DEFAULT 1,
	uecodCajero VARCHAR(20),
	uepuerto INTEGER,
	ueip VARCHAR(20),
	codBanco INTEGER,
	codTarjeta VARCHAR(11),
	codCuenta INTEGER,
	uestringMensaje VARCHAR(500),

	CONSTRAINT ue_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE NO ACTION,
	CONSTRAINT ue_Cuenta_fk FOREIGN KEY (codCuenta,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE NO ACTION,

	CONSTRAINT ue_pk PRIMARY KEY (codigoue)
);


CREATE TABLE Canal(
	codBanco INTEGER,
	codCanal INTEGER,
	cabloqueado BOOLEAN DEFAULT 0,
	codUltimoEnvio INTEGER DEFAULT NULL,
	canext_numMensaje INTEGER DEFAULT 1,

	CONSTRAINT ca_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,
	CONSTRAINT ca_codUltimoEnvio_fk FOREIGN KEY (codUltimoEnvio) REFERENCES UltimoEnvio(codigoue) ON DELETE CASCADE,
	CONSTRAINT ca_pk PRIMARY KEY (codBanco,codCanal)
);




-- Estados del banco

INSERT INTO EstadoBanco VALUES(1,'Abierta');
INSERT INTO EstadoBanco VALUES(2,'Cerrada');
INSERT INTO EstadoBanco VALUES(3,'Trafico detenido');
INSERT INTO EstadoBanco VALUES(4,'En recuperacion');

-- Bancos
INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES(1,1,80,'127.0.0.1',5);
INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES(2,1,80,'127.0.0.1',5);
INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES(3,2,80,'127.0.0.1',2);
INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES(4,1,80,'127.0.0.1',2);

-- Tarjetas

INSERT INTO Tarjeta VALUES ('pastor42 01',0);
INSERT INTO Tarjeta VALUES ('pastor42 02',0);
INSERT INTO Tarjeta VALUES ('pastor42 03',0);
INSERT INTO Tarjeta VALUES ('pastor42 04',0);
INSERT INTO Tarjeta VALUES ('pastor42 05',0);


-- Cuentas

INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 01',0000,0);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 01',0001,0);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 02',0002,0);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 02',0003,0);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 03',0005,0);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42 03',0010,0);


-- Tipos de movimento.

INSERT INTO TipoMovimiento VALUES(10,'Reintegro');
INSERT INTO TipoMovimiento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovimiento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovimiento VALUES(13,'Pago recibo');
INSERT INTO TipoMovimiento VALUES(50,'Abono');
INSERT INTO TipoMovimiento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovimiento VALUES(99,'Otros');

SELECT codMovimiento,moimporte,mofecha,codTMovimiento FROM Movimiento WHERE ((codCuentaOrig = 2) || (codCuentaDest = 2));

-- Movimientos

INSERT INTO Movimiento(codTMovimiento,codTarjeta,codCuentaOrig,codCuentaDest,moimporte,mooffline,mofecha,codBanco) 
VALUES (10,'pastor42 01',0000,0001,50,0,STR_TO_DATE('05/01/2010', '%m/%d/%Y'),1);

INSERT INTO Movimiento(codTMovimiento,codTarjeta,codCuentaOrig,codCuentaDest,moimporte,mooffline,mofecha,codBanco) 
VALUES (11,'pastor42 01',0000,0001,10,0,STR_TO_DATE('05/01/2010', '%m/%d/%Y'),1);

INSERT INTO Movimiento(codTMovimiento,codTarjeta,codCuentaOrig,codCuentaDest,moimporte,mooffline,mofecha,codBanco) 
VALUES (50,'pastor42 01',0000,0001,20,1,STR_TO_DATE('05/04/2010', '%m/%d/%Y'),1);

INSERT INTO Movimiento(codTMovimiento,codTarjeta,codCuentaOrig,codCuentaDest,moimporte,mooffline,mofecha,codBanco) 
VALUES (51,'pastor42 01',0000,0001,10,0,STR_TO_DATE('04/01/2011', '%m/%d/%Y'),1);

INSERT INTO Movimiento(codTMovimiento,codTarjeta,codCuentaOrig,codCuentaDest,moimporte,mooffline,mofecha,codBanco) 
VALUES (51,'pastor42 02',0003,0002,10,0,STR_TO_DATE('05/02/2010', '%m/%d/%Y'),1);


-- Canales

INSERT INTO Canal(codBanco,codCanal) VALUES (1,1);
INSERT INTO Canal(codBanco,codCanal) VALUES (1,2);
INSERT INTO Canal(codBanco,codCanal) VALUES (1,3);


-- Ultimos envios
INSERT INTO UltimoEnvio(codUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta,uestringMensaje) 
VALUES (1,'1',90,'192.168.0.1',1,'pastor42 01',0000,'El mensaje enviado');

INSERT INTO UltimoEnvio(codUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta) 
VALUES (2,'1',90,'192.168.0.1',1,'pastor42 01',0000);

INSERT INTO UltimoEnvio(codUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta) 
VALUES (3,'2',91,'192.168.0.2',1,'pastor42 01',0001);


--	TipoOrigDest
INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (1,'Banco');
INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (2,'Consorcio');
INSERT INTO TipoOrigDest(codTOrigDest,todnombre)
VALUES (3,'Cajero');


-- Mensajes 

-- BANCO-> CONSORCIO
INSERT INTO Mensaje(codTOrigen,meorigen,codTDestino,medestino,meoffline,mestringMensaje)
VALUES (1,'ID_BAN:11',2,'ID_CONS:21',0,'CODIGO MENSAJE:1 BANCO->CONSORCIO');

-- CONSORCIO->BANCO
INSERT INTO Mensaje(codTOrigen,meorigen,codTDestino,medestino,meoffline,mestringMensaje)
VALUES (2,'ID_CONS:21',1,'ID_BAN:11',0,'CODIGO MENSAJE:2 CONSORCIO->BANCO');

-- CAJERO->CONSORCIO
INSERT INTO Mensaje(codTOrigen,meorigen,codTDestino,medestino,meoffline,mestringMensaje)
VALUES (3,'ID_CAJERO:01',2,'ID_CONS:21',0,'CODIGO MENSAJE:3 CAJERO->CONSORCIO');

-- CAJERO->CONSORCIO
INSERT INTO Mensaje(codTOrigen,meorigen,codTDestino,medestino,meoffline,mestringMensaje)
VALUES (3,'ID_CAJERO:31',2,'ID_CONS:21',0,'CODIGO MENSAJE:4 CAJERO->CONSORCIO');

-- CONSORCIO->CAJERO
INSERT INTO Mensaje(codTOrigen,meorigen,codTDestino,medestino,meoffline,mestringMensaje)
VALUES (2,'ID_CONS:21',3,'ID_CAJERO:31',1,'CODIGO MENSAJE:5 CONSORCIO->CAJERO offline');









