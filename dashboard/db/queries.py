from typing import List, Dict
from datetime import date

from flask import current_app
from dashboard.db.models import Statistic

from sqlalchemy import func
from sqlalchemy.orm import Session

type_model = {
    'file_desc': Statistic.file_desc,
    'oks': Statistic.oks,
    'okpd': Statistic.okpd
}


def get_request_counts(start_date: date, end_date: date, type: str):
    session_factory = current_app.session_factory
    session: Session
    statistic: List
    model = type_model[type] if type in type_model else Statistic.file_desc
    with session_factory() as session:
        statistic = session.query(model, func.count())\
            .filter(Statistic.date > start_date)\
            .filter(Statistic.date < end_date)\
            .group_by(model)\
            .all()
    result: Dict = {}
    for (key, value) in statistic:
        result.update({key: value})
    print(statistic)
    return result
