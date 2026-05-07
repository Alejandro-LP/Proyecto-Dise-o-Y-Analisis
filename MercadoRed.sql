DROP DATABASE IF exists mercadored;
CREATE DATABASE mercadored;
USE mercadored;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO roles (nombre) VALUES
('COMPRADOR'),
('VENDEDOR'),
('ADMIN');

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    celular VARCHAR(15) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    id_rol INT NOT NULL,
    estado ENUM('PENDIENTE', 'ACTIVO', 'BLOQUEADO') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE credenciales (
    id_credencial INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE confirmaciones_email (
    id_confirmacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    token VARCHAR(100),
    confirmado BOOLEAN DEFAULT FALSE,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE compradores (
    id_comprador INT PRIMARY KEY,
    FOREIGN KEY (id_comprador) REFERENCES usuarios(id_usuario)
);

CREATE TABLE vendedores (
    id_vendedor INT PRIMARY KEY,
    cuenta_bancaria VARCHAR(50) NOT NULL,
    contrato_aceptado BOOLEAN NOT NULL,
    validado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario)
);

CREATE TABLE documentos_vendedor (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_vendedor INT,
    archivo VARCHAR(255) NOT NULL,
    estado ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO') DEFAULT 'PENDIENTE',
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vendedor) REFERENCES vendedores(id_vendedor)
);

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE publicaciones (
    id_publicacion INT AUTO_INCREMENT PRIMARY KEY,
    id_vendedor INT NOT NULL,
    id_categoria INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    stock INT NOT NULL,
    estado_producto ENUM('NUEVO', 'USADO') NOT NULL,
    politicas_devolucion TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vendedor) REFERENCES vendedores(id_vendedor),
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

CREATE TABLE imagenes_publicacion (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    id_publicacion INT NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_publicacion) REFERENCES publicaciones(id_publicacion) ON DELETE CASCADE
);

INSERT INTO categorias (nombre) VALUES
('Electrónica'),
('Ropa'),
('Hogar'),
('Deportes'),
('Libros');

Select * FROM usuarios;

-- Compradores
INSERT INTO usuarios (nombre_completo, documento, correo, celular, direccion, id_rol, estado) VALUES
('Carlos Mendoza', '1234567890', 'carlos@gmail.com', '3101234567', 'Calle 10 # 5-20, Bogotá', 1, 'ACTIVO'),
('Laura Gómez',    '0987654321', 'laura@gmail.com',  '3209876543', 'Carrera 7 # 12-30, Medellín', 1, 'ACTIVO');

INSERT INTO credenciales (id_usuario, password) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'carlos@gmail.com'), '1234'),
((SELECT id_usuario FROM usuarios WHERE correo = 'laura@gmail.com'),  '1234');

INSERT INTO compradores (id_comprador) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'carlos@gmail.com')),
((SELECT id_usuario FROM usuarios WHERE correo = 'laura@gmail.com'));

-- Vendedores
INSERT INTO usuarios (nombre_completo, documento, correo, celular, direccion, id_rol, estado) VALUES
('Pedro Ramírez',  '1122334455', 'pedro@gmail.com', '3151122334', 'Av. 68 # 22-10, Bogotá',      2, 'ACTIVO'),
('Sofía Torres',   '5566778899', 'sofia@gmail.com', '3005566778', 'Calle 50 # 40-15, Cali',       2, 'ACTIVO');

INSERT INTO credenciales (id_usuario, password) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'pedro@gmail.com'), '1234'),
((SELECT id_usuario FROM usuarios WHERE correo = 'sofia@gmail.com'), '1234');

INSERT INTO vendedores (id_vendedor, cuenta_bancaria, contrato_aceptado, validado) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'pedro@gmail.com'), '001-123456789', TRUE, TRUE),
((SELECT id_usuario FROM usuarios WHERE correo = 'sofia@gmail.com'), '002-987654321', TRUE, TRUE);

-- Publicaciones de prueba para Pedro
INSERT INTO publicaciones (id_vendedor, id_categoria, titulo, descripcion, precio, stock, estado_producto, politicas_devolucion) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'pedro@gmail.com'),
 (SELECT id_categoria FROM categorias WHERE nombre = 'Electrónica'),
 'Audífonos Bluetooth Sony',
 'Audífonos inalámbricos con cancelación de ruido, batería de 30 horas.',
 189900, 15, 'NUEVO', 'Devolución dentro de 15 días con empaque original.'),

((SELECT id_usuario FROM usuarios WHERE correo = 'pedro@gmail.com'),
 (SELECT id_categoria FROM categorias WHERE nombre = 'Electrónica'),
 'Teclado mecánico RGB',
 'Teclado mecánico gamer con switches azules y retroiluminación RGB.',
 245000, 8, 'NUEVO', 'No se aceptan devoluciones por uso.');

-- Publicaciones de prueba para Sofía
INSERT INTO publicaciones (id_vendedor, id_categoria, titulo, descripcion, precio, stock, estado_producto, politicas_devolucion) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'sofia@gmail.com'),
 (SELECT id_categoria FROM categorias WHERE nombre = 'Ropa'),
 'Chaqueta impermeable',
 'Chaqueta para lluvia, talla M, color negro. Muy poco uso.',
 85000, 3, 'USADO', 'Cambios en 8 días si hay defecto de fábrica.'),

((SELECT id_usuario FROM usuarios WHERE correo = 'sofia@gmail.com'),
 (SELECT id_categoria FROM categorias WHERE nombre = 'Hogar'),
 'Cafetera italiana Bialetti',
 'Cafetera moka de 6 tazas, aluminio, nueva en caja.',
 120000, 5, 'NUEVO', 'Devolución en 10 días con factura.');
 
 CREATE TABLE carritos (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_comprador INT NOT NULL UNIQUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_comprador) REFERENCES compradores(id_comprador)
);

CREATE TABLE carrito_items (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL,
    id_publicacion INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id_carrito) REFERENCES carritos(id_carrito) ON DELETE CASCADE,
    FOREIGN KEY (id_publicacion) REFERENCES publicaciones(id_publicacion)
);

CREATE TABLE metodos_pago (
    id_metodo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO metodos_pago (nombre) VALUES
('Tarjeta de crédito'),
('Tarjeta de débito'),
('PSE'),
('Efectivo contra entrega');

CREATE TABLE ordenes (
    id_orden INT AUTO_INCREMENT PRIMARY KEY,
    id_comprador INT NOT NULL,
    direccion_envio VARCHAR(200) NOT NULL,
    id_metodo_pago INT NOT NULL,
    total DECIMAL(14,2) NOT NULL,
    estado ENUM('PENDIENTE','PAGADA','CANCELADA') DEFAULT 'PENDIENTE',
    fecha_orden DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_comprador) REFERENCES compradores(id_comprador),
    FOREIGN KEY (id_metodo_pago) REFERENCES metodos_pago(id_metodo)
);

CREATE TABLE orden_items (
    id_orden_item INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT NOT NULL,
    id_publicacion INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (id_orden) REFERENCES ordenes(id_orden),
    FOREIGN KEY (id_publicacion) REFERENCES publicaciones(id_publicacion)
);