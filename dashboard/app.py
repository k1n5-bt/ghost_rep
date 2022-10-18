import logging

import dash as ds
import dash.html as html
from dash import dcc

import sqlalchemy
from utils import make_pg_url

import json
import datetime

from config import config as cfg


main_page = open("common.html", 'r').read()
logger = logging.getLogger(__name__)


def get_data_from_json():
    with open('test_data.json', 'r') as data:
        result: dict = json.loads(data.read())
    return result


def generate_table(dataframe: dict):
    return html.Table(
        # Header
        [html.Tr(children=[html.Th("Number ghost"), html.Th("Date")])] +

        # Body
        [html.Tr(children=[html.Td(key), html.Td(dataframe[key]["date"])]) for key in dataframe.keys()]
    )


def init_app():
    external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']
    res = ds.Dash(name=__name__, external_stylesheets=external_stylesheets,
                  index_string=main_page)
    data_for_table = get_data_from_json()
    res.layout = html.Div(children=[dcc.DatePickerRange(
        id="date-range",
        min_date_allowed=datetime.date.today() - datetime.timedelta(days=365),
        max_date_allowed=datetime.date.today() + datetime.timedelta(days=365),
        start_date=datetime.date.today() - datetime.timedelta(days=7),
        end_date=datetime.date.today() + datetime.timedelta(days=7),
    ),
        generate_table(data_for_table)], style={
        'width': '300px',
        'border': '1px',
        'solid': 'green',
        'margin': 'auto',
    })
    return res


if __name__ == '__main__':
    app = init_app()
    db = sqlalchemy.create_engine(make_pg_url(**cfg.DB_CONFIG))
    with db.connect() as conn:
        result = conn.execution_options(stream_results=True).execute("select * from usr")
        logger.info(result.one())
    app.run(host=cfg.HOST, port=cfg.PORT, debug=True)
