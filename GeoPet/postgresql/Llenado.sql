--Selecionar los usuarios
SELECT * FROM public.usuarios
ORDER BY usuarioid ASC 

CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- Actualizar la contraseña del usuario 1
UPDATE Usuarios
SET contrasenia = crypt('12345', gen_salt('bf', 12))
WHERE UsuarioID = 1;


-- Insertar un Admin
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Othon', 'Lozano', 'Vidal', 'othonlozano@gmail.com',
  crypt('othon lozano 8', gen_salt('bf', 12)),
  '5551234567', 'Calle Jalapa 123', 'JalapaYork',
  CURRENT_DATE, 'Admin', 'Alta'
);

-- Insertar un Cliente
INSERT INTO Usuarios (
  Nombre, ApellidoPat, ApellidoMat, Email,
  Contrasenia, Telefono, Direccion, Ciudad,
  Fecha_Registro, Usuario, Estatus
) VALUES (
  'Renata', 'Caro', 'Olmos', 'renata@gmail.com',
  crypt('renata carolina', gen_salt('bf', 12)),
  '5551234567', 'Calle Costa 123', 'Polvorin',
  CURRENT_DATE, 'Cliente', 'Alta'
);


-- Insertar especies
INSERT INTO especie (nombre, descripcion, Estatus)
VALUES
('Perro', 'Animal doméstico leal', 'Alta'),
('Gato', 'Felino independiente', 'Alta'),
('Canario', 'Ave pequeña y colorida', 'Alta');