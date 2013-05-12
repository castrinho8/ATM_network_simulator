
-- tacod Codigo tarjeta
CREATE TABLE Tarjeta(
	codTarjeta VARCHAR(11),
	CONSTRAINT ta_pk PRIMARY KEY (codTarjeta)
);


-- cucod Codigo cuenta
-- cusaldo Saldo cuenta
CREATE TABLE Cuenta(
	codCuenta INTEGER,
	codTarjeta VARCHAR(11),
	CONSTRAINT cu_pk PRIMARY KEY (codCuenta,codTarjeta)
);

/*
-- tacod Codigo Tarjeta
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
	codBanco INTEGER,
	codEBanco INTEGER,
	bapuerto INTEGER,
	baip		VARCHAR(20),
	bamaxCanales INTEGER,
	CONSTRAINT ba_codEBanco_fk FOREIGN KEY (codEBanco) REFERENCES EstadoBanco(codEBanco) ON DELETE SET NULL,

	CONSTRAINT ba_pk PRIMARY KEY (codBanco)
);

-- codCuentaOrig Codigo cuenta origen
-- codCuentaDest Codigo cuenta destino
-- mocod COdigo movimiento
-- tmcod COdigo de tipo de movimiento
-- moimporte Importe del movimiento
-- mooffline Booleano que indica si es offline
CREATE TABLE Movimiento(
	codMovimiento INTEGER,
	codCuentaOrig INTEGER,
	codCuentaDest INTEGER,
	codTMovimiento INTEGER,
	moimporte INTEGER,
	mooffline BOOLEAN,
	codBanco CHAR(50),
	CONSTRAINT mo_codCuentaOrig_fk FOREIGN KEY (codCuentaOrig) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,
	CONSTRAINT mo_codCuentaDest_fk FOREIGN KEY (codCuentaDest) REFERENCES Cuenta(codCuenta) ON DELETE CASCADE,

	CONSTRAINT mo_codTMovimiento_fk FOREIGN KEY (codTMovimiento) REFERENCES TipoMovimiento(codTMovimiento) ON DELETE SET NULL,

	CONSTRAINT mo_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codBanco) ON DELETE SET NULL,

	CONSTRAINT mo_pk PRIMARY KEY (codMovimiento)
);


-- codMensaje El codigo del mensaje
-- meoffline Booleano que indica si es offline o no
-- mees_envio Booleano que indica si es envio o no
-- 
-- codBanco El codigo del banco correspondiente al mensaje
CREATE TABLE Mensaje(
	codMensaje INTEGER,
	meoffline BOOLEAN,
	mees_envio BOOLEAN,
--	++++++++++++++++ mensaje
	codBanco CHAR(50),
	CONSTRAINT me_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codBanco) ON DELETE SET NULL,

	CONSTRAINT me_pk PRIMARY KEY (codMensaje),
);


CREATE TABLE UltimoEnvio(
	codUltimoEnvio INTEGER,
--	++++++++++++++++ mensaje
	uecontestado BOOLEAN DEFAULT 0,
	uecodCajero INTEGER,
	uepuerto INTEGER,
	ueip VARCHAR(20),
	codBanco CHAR(50),
	codCanal INTEGER,
	codTarjeta VARCHAR(11),
	codCuenta INTEGER,

	CONSTRAINT ue_codBanco_fk FOREIGN KEY (codBanco) REFERENCES Banco(codBanco) ON DELETE SET NULL,
	CONSTRAINT ue_codCanal_fk FOREIGN KEY (codCanal) REFERENCES Canal(codCanal) ON DELETE SET NULL,

	CONSTRAINT ue_Cuenta_fk FOREIGN KEY (codCuenta,codTarjeta) REFERENCES Cuenta(codCuenta,codTarjeta) ON DELETE SET NULL,

	CONSTRAINT ue_pk PRIMARY KEY (codUltimoEnvio),
);

CREATE TABLE Canal(
	codBanco CHAR(50),
	codCanal INTEGER,
	cabloqueado BOOLEAN DEFAULT 0,
	codUltimoEnvio INTEGER DEFAULT NULL,
	CONSTRAINT me_codUltimoEnvio_fk FOREIGN KEY (codUltimoEnvio) REFERENCES UltimoEnvio(codUltimoEnvio) ON DELETE SET NULL,
	CONSTRAINT ca_pk PRIMARY KEY (codBanco,codCanal),
);




-- Tipos de movimento.

INSERT INTO TipoMovemento VALUES(10,'Reintegro');
INSERT INTO TipoMovemento VALUES(11,'Transpaso emitido');
INSERT INTO TipoMovemento VALUES(12,'Transpaso recibido');
INSERT INTO TipoMovemento VALUES(13,'Pago recibo');
INSERT INTO TipoMovemento VALUES(50,'Abono');
INSERT INTO TipoMovemento VALUES(51,'Cobro de cheque');
INSERT INTO TipoMovemento VALUES(99,'Otros');




