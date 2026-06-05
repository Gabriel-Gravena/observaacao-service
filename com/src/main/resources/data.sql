INSERT INTO usuarios (id, name, email, cpf, telefone, password, role, created_at)
VALUES (
           RANDOM_UUID(),
           'Admin Servidor',
           'servidor@email.com',
           '000.000.000-00',
           '44999999999',
           '$2a$10$Xr11C.IP1T7NsTTUCZFKReppYWe94fzjGcAcfdO5x2FUFejtF60B2',
           'SERVIDOR',
           NOW()
       );