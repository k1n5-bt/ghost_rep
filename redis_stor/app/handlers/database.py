from aiohttp import web
from storage import Repository


def validation_email_and_id(params: dict):
    if 'email' not in params.keys() or 'id' not in params.keys():
        return False
    return True


async def get_email(request: web.Request):
    params = request.query
    if 'id' not in params.keys():
        return web.json_response(text='The parameter is missing', status=400)
    try:
        await Repository().connect()
        emails = await Repository().find_email(id=params['id'])
    except Exception as err:
        return web.json_response(text=str(err), status=400)
    result = ''
    for i in emails:
        result += f"{i},"
    return web.json_response(text=result, status=200)


async def add_email(request: web):
    params = request.query
    if not validation_email_and_id(params):
        return web.json_response(text='The parameter is missing', status=400)
    try:
        await Repository().connect()
        email = await Repository().add_email(id=int(params['id']), email=params['email'])
    except Exception as err:
        return web.json_response(text=str(err), status=400)
    return web.json_response(text='ok', status=200)
