--Selecionar los usuarios
SELECT * FROM public.usuarios
ORDER BY usuarioid ASC 

--En PostgreSQL sirve para instalar o habilitar la extensión pgcrypto si no está ya instalada en la base de datos actual.
CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- Actualizar la contraseña del usuario 1
UPDATE Usuarios
SET contrasenia = crypt('12345', gen_salt('bf', 12))
WHERE UsuarioID = 1;

-- Insertar un SuperAdmin
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Juan', 'Pérez', 'García', 'juan@correo.com',
  crypt('MiPasswordSeguro123', gen_salt('bf', 12)),
  '5551234567', 'Calle Falsa 123', 'Ciudad',
  CURRENT_DATE, 'SuperAdmin', 'Activo'
);

-- Insertar un Admin
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Juan', 'Pérez', 'García', 'juan@correo.com',
  crypt('MiPasswordSeguro123', gen_salt('bf', 12)),
  '5551234567', 'Calle Falsa 123', 'Ciudad',
  CURRENT_DATE, 'Admin', 'Activo'
);

-- Insertar un Cliente
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Juan', 'Pérez', 'García', 'juan@correo.com',
  crypt('MiPasswordSeguro123', gen_salt('bf', 12)),
  '5551234567', 'Calle Falsa 123', 'Ciudad',
  CURRENT_DATE, 'Cliente', 'Activo'
);