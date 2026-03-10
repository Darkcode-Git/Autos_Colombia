-- Crear base de datos
CREATE DATABASE autos_colombia;

-- Conectar a la base de datos
\c autos_colombia;

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    tipo VARCHAR(20) NOT NULL
);

-- Tabla de Vehículos
CREATE TABLE vehiculos (
    placa VARCHAR(20) PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL
);

-- Tabla de Celdas
CREATE TABLE celdas (
    numero SERIAL PRIMARY KEY,
    ocupada BOOLEAN DEFAULT FALSE,
    placa_vehiculo VARCHAR(20) REFERENCES vehiculos(placa) ON DELETE SET NULL
);

-- Tabla de Registros Entrada/Salida
CREATE TABLE registros_entrada_salida (
    id SERIAL PRIMARY KEY,
    placa_vehiculo VARCHAR(20) REFERENCES vehiculos(placa),
    id_usuario VARCHAR(50) REFERENCES usuarios(id),
    numero_celda INTEGER REFERENCES celdas(numero),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    es_entrada BOOLEAN NOT NULL
);

-- Tabla de Novedades
CREATE TABLE novedades (
    id SERIAL PRIMARY KEY,
    placa_vehiculo VARCHAR(20) REFERENCES vehiculos(placa),
    descripcion TEXT NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Pagos
CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    placa_vehiculo VARCHAR(20) REFERENCES vehiculos(placa),
    valor DECIMAL(10, 2) NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
