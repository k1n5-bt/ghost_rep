INSERT INTO usr (id, username, password, email, active) VALUES (1, 'admin', 'qwerty', 'admin@gstorage.com', true);

INSERT INTO user_role (user_id, roles) values (1, 'USER'), (1, 'ADMIN')