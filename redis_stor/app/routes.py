from aiohttp import web
from handlers.database import get_email, add_email


def setup_routes(app: web.Application):
    routes = [web.post('/get_email', get_email), web.post('/add_email', add_email)]
    app.add_routes(routes=routes)
