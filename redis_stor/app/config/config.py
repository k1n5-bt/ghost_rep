import os
import logging


HOST = os.environ.get("HOST", "0.0.0.0")
PORT = os.environ.get("PORT", 8085)

REDIS_HOST = os.environ.get("REDIS_HOST", "redis")
REDIS_PORT = os.environ.get("REDIS_PORT", "6379")
REDIS_PASS = os.environ.get("REDIS_PASS")

LOGGER_LEVEL = logging.DEBUG
