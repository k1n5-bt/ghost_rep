import logging

from api.server import get_app

import dash as ds
import dash.html as html

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from utils import make_pg_url

from config import config as cfg

logger = logging.getLogger(__name__)


def init_app():
    res = get_app()
    res.layout = html.Div(ds.page_container)
    return res


if __name__ == '__main__':
    app = init_app()
    server = app.server
    engine = create_engine(make_pg_url(**cfg.DB_CONFIG))
    session_factory = sessionmaker(bind=engine)
    server.session_factory = session_factory
    app.run(host=cfg.HOST, port=cfg.PORT, debug=True)
