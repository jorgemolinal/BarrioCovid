INSERT INTO roles(id,name)  values(1,'COMPRADOR');
INSERT INTO roles(id,name)  values(2,'VENDEDOR');
INSERT INTO roles(id,name)  values(3,'VOLUNTARIO');

-- Creación de usuarios
-- -- COMPRADORES
INSERT INTO usuarios (id,username, password, nombre, email, direccion, vulnerable, tipo) VALUES(1,'davidmartin07','$2a$10$pcFoO2GfPeyzllU1A/C7GuSKMUJC61zQ.DyyJIdF3/xzCMgvHoKwm','David','david@gmail.com','Francisco de Sale, 9',true, 'COMPRADOR');
INSERT INTO usuarios (id,username, password, nombre, email, direccion, vulnerable, tipo) VALUES(2,'fernandoguillen','$2a$10$pcFoO2GfPeyzllU1A/C7GuSKMUJC61zQ.DyyJIdF3/xzCMgvHoKwm','Fernando','fernando@gmail.com','Alberto Aguilera, 34',false, 'COMPRADOR');
-- VENDEDORES
INSERT INTO usuarios (id,username, password, nombre, email, direccion, vulnerable, tipo) VALUES(3,'pabloclavero01','$2a$10$pcFoO2GfPeyzllU1A/C7GuSKMUJC61zQ.DyyJIdF3/xzCMgvHoKwm','Pescadería Pablo','pablo@gmail.com','Leonardo Prieto Castro, 6',false, 'VENDEDOR');
INSERT INTO usuarios (id,username, password, nombre, email, direccion, vulnerable, tipo) VALUES(4,'pabloclavero02','$2a$10$pcFoO2GfPeyzllU1A/C7GuSKMUJC61zQ.DyyJIdF3/xzCMgvHoKwm','Alimentación Pablo','pablo1@gmail.com','Calle de Gaztambide, 6',false, 'VENDEDOR');
--
-- -- VOLUNTARIOS
INSERT INTO usuarios (id,username, password, nombre, email, direccion, vulnerable, tipo) VALUES(5,'jorgemolina34','$2a$10$pcFoO2GfPeyzllU1A/C7GuSKMUJC61zQ.DyyJIdF3/xzCMgvHoKwm','Jorge','jorge@gmail.com','Andrés Mellado, 16',false, 'VOLUNTARIO');
--
INSERT  INTO  users_roles(user_id,role_id) values(1,1);
INSERT  INTO  users_roles(user_id,role_id) values(2,1);
INSERT  INTO  users_roles(user_id,role_id) values(3,2);
INSERT  INTO  users_roles(user_id,role_id) values(4,2);
INSERT  INTO  users_roles(user_id,role_id) values(5,3);
-- -- PRODUCTOS
INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (1,'Manzana','La manzana es una fruta redonda con una piel fina y brillante de color amarillo, verde o rojo',0.5,3,3,false);
INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (2,'Pera','La pera es una fruta con forma de lágrima con una piel fina y suave de color amarillo, verde o rojo',0.3,6,3,false);
INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (3,'Naranja','La naranja es una fruta cítrica redonda con una piel gruesa y rugosa de color naranja brillante',0.2,1,3,false);

INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (4,'Arroz','El arroz es un cereal que se cultiva en todo el mundo y es una importante fuente de alimento para muchas personas',0.5,3,4,false);
INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (5,'Leche',' La leche es un líquido producido por las glándulas mamarias de los mamíferos y es rica en nutrientes como el calcio y la proteína',0.3,6,4,false);
INSERT INTO productos (id, nombre, descripcion, precio, stock, usuario_id,is_deleted) VALUES (6,'Agua','El agua es una sustancia inodora, insípida e incolora que es esencial para la vida y se encuentra en todos los organismos vivos.',0.2,10,4,false);

