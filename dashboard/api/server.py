import dash as ds


def get_app():
    external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']
    app = ds.Dash(name=__name__, external_stylesheets=external_stylesheets, use_pages=True)

    return app
