from typing import List, Dict

import datetime

from db.queries import get_action_counts

from config import config as cfg

import dash
from dash import html, dcc, callback, Input, Output

dash.register_page(__name__, path='/stat')
column_names = {'New': 'Новые', 'Edit': 'Измененные', 'Archive': 'Архивированные', 'Replace': 'Замененные'}


def generate_table(dataframe: List[Dict]):
    counts = len(dataframe)
    return html.Div(children=[html.B(f'Общее кол-во: {counts}'),
                              html.Table(
                                  # Header
                                  [html.Tr(children=[html.Td('Гост'), html.Td('Действие'), html.Td('Дата')])] +

                                  # Body
                                  [html.Tr(id='items', children=[
                                      html.Td(
                                          html.A('Ссылка на гост', href=f'{cfg.SPRING_DATA_URL}{items["data_id"]}')),
                                      html.Td(items['action_id']),
                                      html.Td(datetime.datetime.strptime(items['date'], '%Y-%m-%d %X.%f').date())]) for items
                                   in dataframe]
                              )])


def layout():
    return html.Div(children=[dcc.DatePickerRange(
        id="date-range",
        min_date_allowed=datetime.date.today() - datetime.timedelta(days=365),
        max_date_allowed=datetime.date.today() + datetime.timedelta(days=365),
        start_date=datetime.date.today() - datetime.timedelta(days=7),
        end_date=datetime.date.today() + datetime.timedelta(days=7),
    ), dcc.Dropdown(
                    id="type-filter",
                    options=[
                        {"label": column_names[key], "value": key}
                        for key in column_names.keys()
                    ],
                    value='New',
                    clearable=False,
                    searchable=False,
                    className="dropdown",
                ),
        html.Div(id='act_stat')], style={
        'width': '350px',
        'border': '1px',
        'solid': 'green',
        'margin': 'auto',
    })


@callback(
    Output(component_id='act_stat', component_property='children'),
    Input("date-range", "start_date"),
    Input("date-range", "end_date"),
    Input('type-filter', 'value')
)
def update_date(start_date, end_date, value):
    start_date: datetime = datetime.datetime.strptime(start_date, '%Y-%m-%d')
    end_date: datetime = datetime.datetime.strptime(end_date, '%Y-%m-%d')
    return generate_table(get_action_counts(start_date.date(), end_date.date(), value))
