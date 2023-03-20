from typing import Dict
import datetime

from db.queries import get_group_counts

import dash
from dash import html, dcc, callback, Input, Output

dash.register_page(__name__, path='/counts')
column_names: Dict = {'file_desc': 'Обозначение госта', 'oks': 'Код ОКС', 'okpd': 'Код ОКПД2'}


def generate_table(dataframe: dict, type_name: str, search_input: str):
    return html.Table(
        # Header
        [html.Tr(children=[html.Th(column_names[type_name]), html.Th('Количество обращений')])] +

        # Body
        [html.Tr(children=[html.Td(key), html.Td(dataframe[key])]) for key in dataframe.keys() if
         search_input is None or search_input in key]
    )


def layout():
    return html.Div(children=[html.Div(children=[html.P(children="Статистика обращений")]),
                              html.Br(),
                              dcc.DatePickerRange(
                                  id="date-range",
                                  min_date_allowed=datetime.date.today() - datetime.timedelta(days=365),
                                  max_date_allowed=datetime.date.today() + datetime.timedelta(days=365),
                                  start_date=datetime.date.today() - datetime.timedelta(days=7),
                                  end_date=datetime.date.today() + datetime.timedelta(days=7),
                              ), html.Br(), dcc.Dropdown(
            id="type-filter",
            options=[
                {"label": column_names[key], "value": key}
                for key in column_names.keys()
            ],
            value='file_desc',
            clearable=False,
            searchable=False,
            className="dropdown",
        ), dcc.Input(id="search-filter", type='text', placeholder="Поиск"), html.Div(id='stat')], style={
        'width': '350px',
        'border': '1px',
        'solid': 'green',
        'margin': 'auto',
    })


@callback(
    Output('stat', 'children'),
    Input("date-range", "start_date"),
    Input("date-range", "end_date"),
    Input('type-filter', 'value'),
    Input("search-filter", 'value'),
)
def update(start_date, end_date, value, search_value):
    start_date: datetime = datetime.datetime.strptime(start_date, '%Y-%m-%d')
    end_date: datetime = datetime.datetime.strptime(end_date, '%Y-%m-%d')
    return generate_table(get_group_counts(start_date.date(), end_date.date(), value), value, search_value)
