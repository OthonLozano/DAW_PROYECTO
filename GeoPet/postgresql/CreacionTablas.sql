CREATE TYPE tipo_usuario AS ENUM ('SuperAdmin','Admin', 'Cliente');
CREATE TYPE tipo_estatus AS ENUM ('Alta','Baja');
CREATE TABLE Usuarios(
	UsuarioID serial primary key,
	Nombre varchar (64),
	ApellidoPat varchar(64),
	ApellidoMat varchar(64),
	Email varchar(64) unique,
	Contrasenia varchar(64),
	Telefono varchar(64),
	Direccion text,
	Ciudad varchar(64),
	Fecha_Registro date,
	Usuario tipo_usuario,
	Estatus tipo_estatus
	);
CREATE TABLE Especie(
	EspecieID serial primary key,
	Nombre varchar(20),
	Descripcion text,
	Estatus tipo_estatus);

CREATE TABLE Raza(
	RazaID serial primary key,
	Nombre varchar(20),
	Descripcion text,
	R_Especie int,
	Estatus tipo_estatus,
	FOREIGN KEY (R_Especie) REFERENCES Especie(EspecieID) );

CREATE TYPE sexo_animal AS ENUM ('Macho', 'Hembra');
CREATE TYPE estado_mascota AS ENUM ('Perdida', 'Encontrada', 'En casa');

CREATE TABLE Mascotas(
	MascotaID serial primary key,
	R_Usuario int,
	Nombre varchar(64),
	R_Especie int,
	Edad int,
	Sexo sexo_animal,
	Color varchar(64),
	CaracteristicasDistintivas text,
	Microchip boolean,
	Numero_Microchip varchar(64),
	Estado estado_mascota,
	Fecha_Registro date,
	Estatus tipo_estatus,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Especie) references Especie(EspecieID) );

CREATE TABLE ImagenMascota (
	ImagenID serial primary key,
	R_Mascota int,
	URL_Imagen text,
	Fecha_Carga date,
	Estatus tipo_estatus,
	foreign key (R_Mascota) references Mascotas(MascotaID) );

CREATE TYPE reporte AS ENUM ('Activo', 'Cerrado', 'Cancelado');

CREATE TABLE ReporteDesaparicion(
	ReporteID serial primary key,
	R_Usuario int,
	R_Mascota int,
	FechaDesaparicion date,
	UbicacionUltimaVez varchar(64),
	DescripcionSituacion varchar(64),
	Recompensa float,
	EstadoReporte reporte,
	Fecha_Registro date,
	Estatus tipo_estatus,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Mascota) references Mascotas(MascotaID) );

CREATE TABLE Avistamiento (
	AvistamientoID serial primary key,
	R_Reporte int,
	R_UsuarioReportante int,
	Fecha_Avistamiento date,
	Ubicacion varchar(64),
	Descripcion text,
	Contacto varchar(64),
	Fecha_Registro date,
	R_Imagen int,
	Estatus tipo_estatus,
	foreign key (R_Reporte) references ReporteDesaparicion(ReporteID),
	foreign key (R_UsuarioReportante) references Usuarios(UsuarioID),
	foreign key (R_Imagen) references ImagenMascota(ImagenID) );

CREATE TABLE Comentarios (
	ComentarioID serial primary key,
	R_Usuario int,
	R_Reporte int,
	Contenido text,
	Fecha_Comentario date,
	Estatus tipo_estatus,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Reporte) references ReporteDesaparicion(ReporteID) );