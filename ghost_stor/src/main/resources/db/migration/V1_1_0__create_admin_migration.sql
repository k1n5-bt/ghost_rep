INSERT INTO usr (id, username, password, email, active, name, surname, patronymic, company, field, division)
VALUES (0, 'Администратор', 'qwerty', 'admin@gstorage.com', true, 'Иван', 'Петров', 'Васильевич', 'UrFU', 'Образование',
        'Кафедра математики');

INSERT INTO user_role (user_id, roles)
values (0, 'USER'),
       (0, 'ADMIN')