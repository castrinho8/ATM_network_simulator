

-- tacod Codigo de la tarjeta
-- tagastoOffline Gasto de la tarjeta realizado offline
CREATE TABLE Tarjeta(
	codTarjeta VARCHAR(11) NOT NULL,
	tagastoOffline INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT ta_pk PRIMARY KEY (codTarjeta)
);


-- codTarjeta Codigo de la tarjeta
-- codCuenta Codigo de la cuenta
-- cusaldo Saldo actual de la cuenta
CREATE TABLE Cuenta(
	codTarjeta VARCHAR(11) NOT NULL,
	codCuenta INTEGER NOT NULL,
	cusaldo INTEGER NOT NULL DEFAULT 0,
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
	codTMovimiento INTEGER NOT NULL,
	tmnombre  CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT tm_pk PRIMARY KEY (codTMovimiento)
);


-- codEBanco Codigo del Estado del banco
-- ebnombre Nombre del estado del banco
CREATE TABLE EstadoBanco(
	codEBanco INTEGER NOT NULL,
	ebnombre CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT eb_pk PRIMARY KEY (codEBanco)
);

-- codigo El codigo generado por la BD que asegura la correcta identificacion del banco.
-- codBanco Codigo unico que identifica al banco(funciona como un nombre)
-- codEBanco Codigo del Estado del banco
-- bapuerto El puerto del banco
-- baip La ip del banco
-- bamaxCanales El numero maximo de canales
-- balastChannelUsed El número del ultimo canal que ha sido usado en este banco
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

-- codMovimiento El codigo que identifica al movimiento
-- codTarjeta La tarjeta que realiza el movimiento
-- codCuentaOrig La cuenta origen del movimiento 
-- codCuentaDest La cuenta destino del movimiento 
-- codTMovimiento El tipo de movimiento
-- mofecha La fecha en la que se realiza el movimiento
-- moimporte Importe del movimiento
-- moonline Booleano que indica si es online (FALSE=OFFLINE, TRUE=ONLINE)
-- codBanco El codigo que identifica al banco en el que se realiza el movimiento
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
	CONSTRAINT mo_codCuentaOrig_fk FOREIGN KEY (codCuentaOrig,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE CASCADE,
	CONSTRAINT mo_codCuentaDest_fk FOREIGN KEY (codCuentaDest,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE CASCADE,

	CONSTRAINT mo_codTMovimiento_fk FOREIGN KEY (codTMovimiento) REFERENCES TipoMovimiento(codTMovimiento) ON DELETE SET NULL,

	CONSTRAINT mo_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codigo) ON DELETE CASCADE,

	CONSTRAINT mo_pk PRIMARY KEY (codMovimiento)
);


-- codTOrigDest El codigo numerico que identifica al tipo de origen/destino
-- todnombre El nombre en caracteres del tipo de origen/destino
CREATE TABLE TipoOrigDest(
	codTOrigDest INTEGER NOT NULL,
	todnombre CHAR(30) NOT NULL UNIQUE,
	CONSTRAINT tod_pk PRIMARY KEY (codTOrigDest)
);


-- codMensaje El codigo del mensaje
-- meNumMensaje El numero de mensaje
-- codTOrigen El tipo del origen del mensaje(CAJERO,BANCO,CONSORCIO)
-- meorigen El origen del mensaje
-- codTDestino El tipo del destino del mensaje(CAJERO,BANCO,CONSORCIO)
-- medestino El destino del mensaje
-- codBanco El codigo del banco correspondiente al mensaje
-- meonline Booleano que indica si es online o no (FALSE=OFFLINE, TRUE=ONLINE)
-- mestringMensaje El mensaje en formato toString
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


-- codigoue El codigo del ultimo envio
--	ueNumUltimoEnvio  El numero de mensaje del ultimo envio
--	uecontestado Flag booleano que indica si el mensaje ha sido contestado
--	uecodCajero El identificador del cajero que envia el mensaje
--	uepuerto El puerto del cajero que envia el mensaje
--	ueip La ip del cajero que envia el mensaje
--	codBanco El codigo que identifica al banco.
--	codTarjeta La tarjeta que realiza el movimiento
--	codCuenta La cuenta destino del movimiento
--	uestringMensaje El mensaje en formato toString
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
	CONSTRAINT ue_Cuenta_fk FOREIGN KEY (codCuenta,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE CASCADE,
	CONSTRAINT ue_codCajero_fk FOREIGN KEY (uecodCajero) REFERENCES Cajero(codCajero) ON DELETE CASCADE,

	CONSTRAINT ue_pk PRIMARY KEY (codigoue)
);


-- codBanco El codigo del banco que lo identifica
-- codCanal El codigo del canal
-- cabloqueado Un flag booleano que indica si el canal se encuentra bloqueado (TRUE=BLOQUEADO, FALSE=DESBLOQUEADO)
-- caenRecuperacion True si el canal esta en recuperación o no.
-- codUltimoEnvio El codigo que identifica al ultimo envio de este canal
-- canext_numMensaje El siguiente numero de mensaje que se deberia asignar para este canal
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

INSERT INTO Cajero(cajNombre,cajIp,cajPuerto) VALUES ('       1','127.0.0.1',8928);


INSERT INTO Tarjeta VALUES ('pastor42001',0);
INSERT INTO Tarjeta VALUES ('pastor42002',0);
INSERT INTO Tarjeta VALUES ('pastor42003',0);
INSERT INTO Tarjeta VALUES ('pastor42004',0);
INSERT INTO Tarjeta VALUES ('pastor42005',0);


INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42005',0,0000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42001',1,1000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42002',1,1000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42001',2,2000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42001',3,3000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42002',3,3000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42003',4,4000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42003',5,5000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42003',6,6000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42004',7,7000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42004',8,8000);
INSERT INTO Cuenta(codTarjeta,codCuenta,cusaldo) VALUES ('pastor42005',9,9000);




-- DATOS INICIALES PARA TESTS MANUALES DE LA BASE DE DATOS

-- Estados del banco

INSERT INTO EstadoBanco VALUES(1,'Abierta');
INSERT INTO EstadoBanco VALUES(2,'Cerrada');
INSERT INTO EstadoBanco VALUES(3,'Trafico detenido');
INSERT INTO EstadoBanco VALUES(4,'En recuperacion');


-- Bancos
INSERT INTO Banco(codBanco,codEBanco,bapuerto,baip,bamaxCanales) VALUES(1,1,80,'127.0.0.1',3);
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
INSERT INTO UltimoEnvio(ueNumUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta,uestringMensaje) 
VALUES (1,'1',90,'192.168.0.1',1,'pastor42 01',0000,'El mensaje enviado');

INSERT INTO UltimoEnvio(ueNumUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta) 
VALUES (2,'1',90,'192.168.0.1',1,'pastor42 01',0000);

INSERT INTO UltimoEnvio(ueNumUltimoEnvio,uecodCajero,uepuerto,ueip,codBanco,codTarjeta,codCuenta) 
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









