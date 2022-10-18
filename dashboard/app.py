import os

import dash as ds
import dash.html as html
from dash import dcc
import json
import datetime


main_page = open("common.html", 'r').read()


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
    app.run(host=os.getenv('HOST', '0.0.0.0'), port=os.getenv('PORT', 8085), debug=True)
