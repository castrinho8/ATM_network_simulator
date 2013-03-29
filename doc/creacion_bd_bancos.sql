CREATE TABLE Banco(
	bcod  INTEGER AUTO_INCREMENT,
	bname CHAR(30) NOT NULL,
	CONSTRAINT banco_pk PRIMARY KEY(bcod),
	CONSTRAINT banco_name_uni UNIQUE(bname)
);

CREATE TABLE Tarxeta(
	bcod INTEGER,
	tcod INTEGER,
	CONSTRAINT tarx_banco_fk FOREIGN KEY (bcod) REFERENCES Banco(bcod),
	CONSTRAINT trax_pk PRIMARY KEY (bcod,tcod)
);

CREATE TABLE Cuenta(
	bcod INTEGER,
	tcod INTEGER,
	ccod INTEGER,
	saldo DECIMAL(10,2),
	CONSTRAINT cont_tarx_fk FOREIGN KEY (bcod,tcod) REFERENCES Tarxeta(bcod,tcod),
	CONSTRAINT cont_tarc_pk PRIMARY KEY (bcod,tcod,ccod)
);
