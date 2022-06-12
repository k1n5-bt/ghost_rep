from aioredis import Redis
import aioredis
import config as cfg
from exception import *


class MetaSingleton(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super(MetaSingleton, cls).__call__(*args, **kwargs)
        return cls._instances[cls]


class Repository(metaclass=MetaSingleton):
    connection: Redis = None

    async def connect(self):
        if self.connection is None:
            try:
                self.connection = aioredis.Redis(host=cfg.REDIS_HOST,
                                                 port=cfg.REDIS_PORT,
                                                 password=cfg.REDIS_PASS,
                                                 decode_responses=True)
            except:
                raise ConnectionFailed
        return self.connection

    async def find_email(self, id: int) -> set:
        result = []
        i = 0
        curr_email = await self.connection.lindex(str(id), i)
        while curr_email is not None:
            result.append(curr_email)
            i += 1
            curr_email = await self.connection.lindex(str(id), i)
        return result

    async def add_email(self, id: int, email: str) -> None:
        i = 0
        curr = await self.connection.lindex(str(id), i)
        while curr is not None:
            i += 1
            curr = await self.connection.lindex(str(id), i)
            if curr == email:
                raise EmailExists
        await self.connection.rpush(str(id), str(email))

    async def flush(self):
        await self.connection.flushdb()
