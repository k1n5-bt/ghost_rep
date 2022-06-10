import aiohttp.web as web
from exception import *
from routes import setup_routes
import config as cfg
import logging
import asyncio


def init_app():
    logging.log(cfg.LOGGER_LEVEL, f"App: {cfg.HOST=}, {cfg.PORT=}")
    logging.log(cfg.LOGGER_LEVEL, "Running...")
    app = web.Application()
    setup_routes(app)
    return app


def cancel_tasks() -> None:
    for task in asyncio.Task.all_tasks():
        task.cancel()


async def main():
    logging.basicConfig(level=cfg.LOGGER_LEVEL)
    app = init_app()
    runner = web.AppRunner(app)
    await runner.setup()
    site = web.TCPSite(runner, cfg.HOST, cfg.PORT)
    await site.start()
    while True:
        await asyncio.sleep(3600)


if __name__ == "__main__":
    asyncio.run(main())
