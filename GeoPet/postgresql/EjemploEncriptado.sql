--Selecionar los usuarios
SELECT * FROM public.usuarios
ORDER BY usuarioid ASC 

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
  'Isaias', 'DJ', 'Avi', 'IsaAvi@correo.com',
  crypt('Isaias123', gen_salt('bf', 12)),
  '5551234567', 'Calle Falsa 123', 'Ciudad',
  CURRENT_DATE, 'SuperAdmin', 'Alta'
);

-- Insertar un Admin
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Othon', 'Lozano', 'Vid', 'OthonVid@correo.com',
  crypt('Othon123', gen_salt('bf', 12)),
  '5551234567', 'Calle Jalapa 123', 'JalapaYork',
  CURRENT_DATE, 'Admin', 'Alta'
);

-- Insertar un Cliente
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Renata', 'Caro', 'Olmos', 'Renata@correo.com',
  crypt('MiPasswordSeguro123', gen_salt('bf', 12)),
  '5551234567', 'Calle Costa 123', 'Polvorin',
  CURRENT_DATE, 'Cliente', 'Alta'
);