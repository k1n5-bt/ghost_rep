class EmailExists(Exception):
    def __str__(self):
        return 'Данный email уже существует'


class EmailNotFound(Exception):
    def __str__(self):
        return 'По данному id не был найден email'


class ConnectionFailed(Exception):
    def __str__(self):
        return 'Не удалось произвести подключение к Redis серверу'
