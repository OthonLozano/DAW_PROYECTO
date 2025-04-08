CREATE TABLE Usuarios(
	UsuarioID serial primary key,
	Nombre varchar (30),
	ApellidoPat varchar(30),
	ApellidoMat varchar(30),
	Email varchar(50) unique,
	Contrasenia varchar(255),
	Telefono varchar(10),
	Direccion varchar(30),
	Ciudad varchar(30),
	Fecha_Registro date,
	Ultimo_Acceso date );

CREATE TABLE Especie(
	EspecieID serial primary key,
	Nombre varchar(20),
	Descripcion varchar(30) );

CREATE TABLE Raza(
	RazaID serial primary key,
	Nombre varchar(20),
	Descripcion varchar(30),
	R_Especie int,
	FOREIGN KEY (R_Especie) REFERENCES Especie(EspecieID) );

CREATE TYPE sexo_animal AS ENUM ('macho', 'hembra');
CREATE TYPE estado_mascota AS ENUM ('Perdida', 'Encontrada', 'En casa');

CREATE TABLE Mascotas(
	MascotaID serial primary key,
	R_Usuario int,
	Nombre varchar(20),
	R_Especie int,
	Edad int,
	Sexo sexo_animal,
	Color varchar(20),
	CaracteristicasDistintivas text,
	Microchip boolean,
	Numero_Microchip varchar(20),
	Estado estado_mascota,
	Fecha_Registro date,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Especie) references Especie(EspecieID) );

CREATE TABLE ImagenMascota (
	ImagenID serial primary key,
	R_Mascota int,
	URL_Imagen text,
	Fecha_Carga date,
	foreign key (R_Mascota) references Mascotas(MascotaID) );

CREATE TYPE reporte AS ENUM ('Activo', 'Cerrado', 'Cancelado');

CREATE TABLE ReporteDesaparicion(
	ReporteID serial primary key,
	R_Usuario int,
	R_Mascota int,
	FechaDesaparicion date,
	UbicacionUltimaVez varchar(50),
	DescripcionSituacion varchar(50),
	Recompensa double,
	EstadoReporte reporte,
	Fecha_Registro date,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Mascota) references Mascotas(MascotaID) );

CREATE TABLE Avistamiento (
	AvistamientoID serial primary key,
	R_Reporte int,
	R_UsuarioReportante int,
	Fecha_Avistamiento date,
	Ubicacion varchar(50),
	Descripcion text,
	Contacto varchar(50),
	Fecha_Registro date,
	R_Imagen int,
	foreign key (R_Reporte) references ReporteDesaparicion(ReporteID),
	foreign key (R_UsuarioReportante) references Usuarios(UsuarioID),
	foreign key (R_Imagen) references ImagenMascota(ImagenID) );

CREATE TABLE Comentarios (
	ComentarioID serial primary key,
	R_Usuario int,
	R_Reporte int,
	Contenido text,
	Fecha_Comentario date,
	foreign key (R_Usuario) references Usuarios(UsuarioID),
	foreign key (R_Reporte) references ReporteDesaparicion(ReporteID) );