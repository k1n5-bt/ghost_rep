import dash as ds
from dashboard.db.queries import get_request_counts


def get_app():
    external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']
    main_page = open("common.html", 'r').read()
    app = ds.Dash(name=__name__, external_stylesheets=external_stylesheets, index_string=main_page, use_pages=True)

    return app
