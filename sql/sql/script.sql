CREATE TABLE USUARIO(
		id SERIAL PRIMARY KEY,
		usuario VARCHAR(1024) NOT NULL,
		contrasena VARCHAR(16) NOT NULL
	);
INSERT INTO USUARIO (usuario,contrasena) VALUES ('usuario','clave');